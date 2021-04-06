package be.heydari.contentcloud.gateway.filters;

import brave.Span;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class AuthnEndWebFilter implements WebFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthnEndWebFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
//        LOGGER.info("---END OF AUTHN VERIFICATION---");

        Span span = (Span) serverWebExchange.getAttributes().get("org.springframework.cloud.sleuth.instrument.web.TraceWebFilter.TRACE");
        span.annotate("end of verification");
        return webFilterChain.filter(serverWebExchange);
    }
}
