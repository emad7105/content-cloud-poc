package be.heydari.contentcloud.accountstatepostfilter;

import be.heydari.contentcloud.accountstatepostfilter.opa.OPAClient;
import be.heydari.contentcloud.accountstatepostfilter.opa.OPAClientConfig;
import be.heydari.contentcloud.accountstatepostfilter.opa.OPAInput;
import brave.Span;
import brave.Tracer;
import org.javatuples.Pair;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@RestController
public class AccountStateController {

    private static final Logger LOGGER = Logger.getLogger(AccountStateController.class);
    private final AccountStateRepository accountStateRepository;
    private OPAClient opaClient;
    private final ExecutorService executor;
    private final Tracer tracer;
    private Postfilter filter;

    @Autowired
    public AccountStateController(AccountStateRepository accountStateRepository, OPAClientConfig config, Tracer tracer) {
        this.accountStateRepository = accountStateRepository;
        this.opaClient = new OPAClient(config);
        this.executor = Executors.newCachedThreadPool();
        this.tracer = tracer;
        this.setFilterByEnv();
    }

    @GetMapping("/accountStates")
    public List<AccountState> accountStates(@RequestHeader("Authorization") String token, @RequestParam("size") Optional<Integer> size) {
        Tracer tracer = this.tracer;
        Span span = tracer.nextSpan().name("postfilter-opa");
        try (Tracer.SpanInScope ws = tracer.withSpanInScope(span.start())) {
            return this.filter.filter(token, size.orElse(20));
        } finally {
            span.finish();
        }
    }

    private void setFilterByEnv() {
        String mode = System.getenv().getOrDefault("POSTFILTER_MODE", "sequential");
        LOGGER.info("using postfilter mode: " + mode);
        switch (mode) {
            case "sequential":
                this.filter = this::sequentialAccountStates;
                break;
            case "parallel":
                this.filter = this::parallelAccountStates;
                break;
            default:
                throw new RuntimeException("unknown filter: " + mode);
        }
    }

    private List<AccountState> sequentialAccountStates(String token, int size) {
        List<AccountState> states = new ArrayList<>();
        int cursorSize = 100;
        int offset = 0;
        while (true) {
            Slice<AccountState> slice = accountStateRepository.findAllSlice(PageRequest.of(offset, cursorSize));
            for (AccountState state : slice.getContent()) {
                String[] bearerArray = token.split("\\s+");
                OPAInput input = new OPAInput(bearerArray[1], state);
                try {
                    OPAClient.OPAResponse decision = this.opaClient.queryOPA(input);
                    if (decision.result == true) {
                        states.add(state);
                    }
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                }

                if (states.size() == size) {
                    return states;
                }
            }

            if (slice.isLast()) {
                return states;
            }

            offset++;
        }

//        for(AccountState entry: accountStateRepository.findAll()) {
//            String[] bearerArray = token.split("\\s+");
//            OPAInput input = new OPAInput(bearerArray[1], entry);
//
//            try {
//                OPAClient.OPAResponse decision = this.opaClient.queryOPA(input);
//                if (decision.result == true) {
//                    states.add(entry);
//                }
//            } catch (IOException e) {
//                LOGGER.error(e.getMessage(), e);
//            }
//
//            if (states.size() == size) {
//                break;
//            }
//        }
//
//        return states;
    }

    private List<AccountState> parallelAccountStates(String token, int size) {
        ConcurrentLinkedQueue<Future<Pair<Boolean, AccountState>>> futures = new ConcurrentLinkedQueue<>();
        // to signal the amount of data in the queue
        Semaphore sema = new Semaphore(0);
        Future<Integer> sender = executor.submit(() -> {
            for(AccountState entry: accountStateRepository.findAll()) {
                AccountState captured = entry;
                Future<Pair<Boolean, AccountState>> future = executor.submit(() -> {
                    String[] bearerArray = token.split("\\s+");
                    OPAInput input = new OPAInput(bearerArray[1], captured);

                    try {
                        OPAClient.OPAResponse decision = this.opaClient.queryOPA(input);
                        return new Pair<>(decision.result, captured);
                    } catch (IOException e) {
                        LOGGER.error(e.getMessage(), e);
                    }

                    return new Pair<>(false, null);
                });
                futures.add(future);
                System.out.println("added data");
                sema.release();
            }

            return 0;
        });

        List<AccountState> accountStates = new ArrayList<>();
        while (!sender.isDone()) {
            // first try to acquire semaphore
            try {
                if (!sema.tryAcquire(1, TimeUnit.MILLISECONDS)) {
                    continue;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                continue;
            }

            Future<Pair<Boolean, AccountState>> future = futures.poll();
            Assert.notNull(future, "semaphore should guarantee the presence of data");

            try {
                Pair<Boolean,AccountState> result = future.get();
                if (result.getValue0()) {
                    accountStates.add(result.getValue1());

                    if (accountStates.size() == size) {
                        sender.cancel(true);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return accountStates;
    }
}
