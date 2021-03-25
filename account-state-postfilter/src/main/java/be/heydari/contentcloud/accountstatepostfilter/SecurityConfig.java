package be.heydari.contentcloud.accountstatepostfilter;

import brave.SpanCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;

import javax.servlet.*;
import java.io.IOException;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        // more info or filter order see the FilterComparator.class
        // for this implementation we just picked the first and the last one.
        http.authorizeRequests()
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(new Filter() {
                @Override
                public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
                    SpanCustomizer span = (SpanCustomizer) servletRequest.getAttribute("brave.SpanCustomizer");
                    span.annotate("start authn verification");
                    filterChain.doFilter(servletRequest, servletResponse);
                }
            }, ChannelProcessingFilter.class)
            .addFilterAfter(new Filter() {
                @Override
                public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
                    SpanCustomizer span = (SpanCustomizer) servletRequest.getAttribute("brave.SpanCustomizer");
                    span.annotate("end authn verification");
                    filterChain.doFilter(servletRequest, servletResponse);
                }
            }, SwitchUserFilter.class)
            // validate jwt token
            .oauth2ResourceServer(oauth -> oauth.jwt());
    }
}
