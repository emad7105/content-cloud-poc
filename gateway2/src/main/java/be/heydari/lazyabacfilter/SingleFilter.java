package be.heydari.lazyabacfilter;

import be.heydari.lazyabacfilter.config.SingleConfig;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

public class SingleFilter extends Filter {

    private SingleConfig config;
    private OPAClientAsync client;

    public SingleFilter(OPAClientAsync client, SingleConfig config) {
        this.config = config;
        this.client = client;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Optional<String> bearerToken = fetchBearerToken(exchange);
        if (!bearerToken.isPresent()) {
            return chain.filter(exchange);
        }
        return client.queryOPA(config.getAbacQuery(), new OpaInput(bearerToken.get()), config.getAbacUnknowns())
            .map(abacContextEncoded -> {
                exchange.getRequest()
                    .mutate()
                    .header("X-ABAC-Context", abacContextEncoded);
                return exchange;
            }).flatMap(chain::filter);
    }
}
