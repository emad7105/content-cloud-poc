package be.heydari.lazyabacfilter;

import be.heydari.lazyabacfilter.config.Config;
import be.heydari.lazyabacfilter.config.MultiConfig;
import be.heydari.lazyabacfilter.config.SingleConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@ConditionalOnProperty(name = "opa.service.enabled", havingValue = "true")
public class ABACPolicyGatewayFilterFactory extends AbstractGatewayFilterFactory<Config> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ABACPolicyGatewayFilterFactory.class);

    public static final String ABAC_QUERY = "abacQuery";
    public static final String ABAC_UNKNOWNS = "abacUnknowns";

    private OPAClient opaClient;
    private OPAClientAsync opaClientAsync;
    private boolean enabled;

    public ABACPolicyGatewayFilterFactory() {
        super(Config.class);
    }

    public void setOpaClient(OPAClient opaClient) {
        this.opaClient = opaClient;
    }
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
        if (!this.enabled) {
            System.out.println("gateway filter disabled");
            return new OrderedGatewayFilter((exchange, chain) -> chain.filter(exchange), 1);
        }

        System.out.println("Running gateway in " + config.getMode() + " mode");
        GatewayFilter filter = selectFilter(config);
        return new OrderedGatewayFilter(filter, 1);
    }

    private GatewayFilter selectFilter(Config config) {
        switch (config.getMode()) {
            case "multi":
                return new MultiFilter(this.opaClientAsync, new MultiConfig(config));
            case "single":
            default:
                return new SingleFilter(this.opaClientAsync, new SingleConfig(config));
        }
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
}
