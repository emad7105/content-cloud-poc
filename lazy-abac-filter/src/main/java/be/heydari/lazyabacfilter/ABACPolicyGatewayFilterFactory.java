package be.heydari.lazyabacfilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@ConditionalOnProperty(name = "opa.service.enabled", havingValue = "true")
public class ABACPolicyGatewayFilterFactory extends AbstractGatewayFilterFactory<ABACPolicyGatewayFilterFactory.Config> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ABACPolicyGatewayFilterFactory.class);

    public static final String ABAC_QUERY = "abacQuery";
    public static final String ABAC_UNKNOWNS = "abacUnknowns";

    //private OPAClient opaClient;
    private OPAClientAsync opaClientAsync;
    private boolean enabled;

    public ABACPolicyGatewayFilterFactory() {
        super(Config.class);
    }

    /*public void setOpaClient(OPAClient opaClient) {
        this.opaClient = opaClient;
    }*/
    public void setOpaClientAsync(OPAClientAsync opaClientAsync) {
        this.opaClientAsync = opaClientAsync;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList(ABAC_QUERY, ABAC_UNKNOWNS);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return new OrderedGatewayFilter((exchange, chain) -> {
            Optional<String> bearerToken = fetchBearerToken(exchange);
            if ((!bearerToken.isPresent()) || (!this.enabled)) {
                return chain.filter(exchange);
            }

            return opaClientAsync.queryOPA(config.getAbacQuery(), new OpaInput(bearerToken.get()), config.getAbacUnknowns())
                    .map(abacContextEncoded -> {
                        exchange.getRequest()
                                    .mutate()
                                    .header("X-ABAC-Context", abacContextEncoded);
                        return exchange;
                    }).flatMap(chain::filter);
        }, 1);
    }

    private Optional<String> fetchBearerToken(ServerWebExchange exchange) {
        List<String> authorization = exchange.getRequest().getHeaders().get("Authorization");
        if (authorization != null && authorization.size() > 0) {
            String bearer = authorization.get(0);
            if (bearer.startsWith("Bearer")) {
                String[] bearerArray = bearer.split("\\s+");
                return Optional.ofNullable(bearerArray[1]);
            }
        }
        return Optional.empty();
    }


    public static class Config {
        private String abacQuery;
        private List<String> abacUnknowns;

        public Config() {
        }

        public Config(String abacQuery, List<String> abacUnknowns) {
            this.abacQuery = abacQuery;
            this.abacUnknowns = abacUnknowns;
        }

        public String getAbacQuery() {
            return abacQuery;
        }

        public void setAbacQuery(String abacQuery) {
            this.abacQuery = abacQuery;
        }

        public List<String> getAbacUnknowns() {
            return abacUnknowns;
        }

        public void setAbacUnknowns(List<String> abacUnknowns) {
            this.abacUnknowns = abacUnknowns;
        }
    }
}
