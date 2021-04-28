package be.heydari.contentcloud.accountstatepostfilter;

import be.heydari.contentcloud.accountstatepostfilter.opa.OPAClient;
import be.heydari.contentcloud.accountstatepostfilter.opa.OPAClientConfig;
import be.heydari.contentcloud.accountstatepostfilter.opa.OPAInput;
import brave.Span;
import brave.Tracer;
import org.javatuples.Pair;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@RestController
public class AccountStateController {

    private static final Logger LOGGER = Logger.getLogger(AccountStateController.class);
    private final AccountStateRepository accountStateRepository;
    private OPAClient opaClient;
    private final ExecutorService executor;
    private final Tracer tracer;

    @Autowired
    public AccountStateController(AccountStateRepository accountStateRepository, OPAClientConfig config, Tracer tracer) {
        this.accountStateRepository = accountStateRepository;
        this.opaClient = new OPAClient(config);
        this.executor = Executors.newCachedThreadPool();
        this.tracer = tracer;
    }

    @GetMapping("/accountStates")
    public List<AccountState> accountStates(@RequestHeader("Authorization") String token, @RequestParam("size") Optional<Integer> size) {
        Tracer tracer = this.tracer;
        Span span = tracer.nextSpan().name("postfilter-opa");
        try (Tracer.SpanInScope ws = tracer.withSpanInScope(span.start())) {
            return sequentialAccountStates(token, size.orElse(20));
        } finally {
            span.finish();
        }
    }

    private List<AccountState> sequentialAccountStates(String token, int size) {
        List<AccountState> states = new ArrayList<>();
        for(AccountState entry: accountStateRepository.findAll()) {
            String[] bearerArray = token.split("\\s+");
            OPAInput input = new OPAInput(bearerArray[1], entry);

            try {
                OPAClient.OPAResponse decision = this.opaClient.queryOPA(input);
                if (decision.result == true) {
                    states.add(entry);
                }
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }

            if (states.size() == size) {
                break;
            }
        }

        return states;
    }

    private List<AccountState> parallelAccountStates(String token) {
        List<Future<Pair<Boolean, AccountState>>> futures = new ArrayList<>();
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
        }

        return futures.stream().map(future -> {
            try {
                return future.get();
            } catch (Exception e) {
                e.printStackTrace();
                return new Pair<Boolean, AccountState>(false, null);
            }
        })
            .filter(Pair::getValue0)
            .map(Pair::getValue1)
            .collect(Collectors.toList());
    }
}
