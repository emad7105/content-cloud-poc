package be.heydari.contentcloud.gateway.filters;

import brave.Span;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class AuthnStartWebFilter implements WebFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthnStartWebFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        LOGGER.info("---START OF AUTHN VERIFICATION---");
        System.out.println(serverWebExchange.getAttributes());

        Span span = (Span) serverWebExchange.getAttributes().get("org.springframework.cloud.sleuth.instrument.web.TraceWebFilter.TRACE");
        span.annotate("start of verification");

        return webFilterChain.filter(serverWebExchange);
    }
}
