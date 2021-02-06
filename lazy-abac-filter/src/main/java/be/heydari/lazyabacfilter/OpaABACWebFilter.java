package be.heydari.lazyabacfilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class OpaABACWebFilter implements WebFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpaABACWebFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {

        LOGGER.info("---- Hello there!! ----");

        return webFilterChain.filter(serverWebExchange);
    }
}
