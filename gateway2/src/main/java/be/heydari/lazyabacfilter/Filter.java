package be.heydari.lazyabacfilter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;
import java.util.Optional;

public abstract class Filter implements GatewayFilter {
    protected Optional<String> fetchBearerToken(ServerWebExchange exchange) {
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
