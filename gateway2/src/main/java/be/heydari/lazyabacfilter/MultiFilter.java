package be.heydari.lazyabacfilter;

import be.heydari.lazyabacfilter.config.MultiConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.Optional;

public class MultiFilter extends Filter {

    private MultiConfig config;
    private OPAClientAsync client;

    private Scheduler scheduler = Schedulers.boundedElastic();

    public MultiFilter(OPAClientAsync client, MultiConfig config) {
        this.config = config;
        this.client = client;
    }

    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Optional<String> bearerToken = fetchBearerToken(exchange);
        if (!bearerToken.isPresent()) {
            return chain.filter(exchange);
        }

        ObjectMapper mapper = new ObjectMapper();
        return Flux.fromStream(this.config.getRules().stream())
            .flatMap(rule -> {
                return client.queryOPA(rule.getPolicy(), new OpaInput(bearerToken.get()), rule.getUnknowns())
                    .map(abacContextEncoded -> new ReducedRule(rule.getPath(), abacContextEncoded));
            })
            .reduce(new HashMap<String, String>(), (reduced, rule) -> {
                reduced.put(rule.getPath(), rule.getAbacEncoded());
                return reduced;
            })
            .map(reduced -> {
                try {
                   return mapper.writeValueAsString(reduced);
                } catch (JsonProcessingException e) {
                   throw new RuntimeException(e);
                }
            })
            .map(policies -> {
                exchange.getRequest()
                    .mutate()
                    .header("X-ABAC-Mode", "multi")
                    .header("X-ABAC-Policies", policies);
                return exchange;
            })
            .flatMap(chain::filter);
}

    @Data
    static class ReducedRule {
        private String path;
        private String abacEncoded;

        public ReducedRule(String path, String abacEncoded) {
            this.path = path;
            this.abacEncoded = abacEncoded;
        }
    }
}
