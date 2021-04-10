package be.heydari.contentcloud.gateway2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange(exchanges -> exchanges.anyExchange().authenticated())
                .oauth2Login(withDefaults());
        http.csrf().disable();

        //http.addFilterAfter(new AuthnStartWebFilter(), SecurityWebFiltersOrder.FIRST);

        //http.addFilterBefore(new AuthnEndWebFilter(), SecurityWebFiltersOrder.LAST);

        //http.addFilterBefore(new OpaABACWebFilter(), SecurityWebFiltersOrder.LAST);

        return http.build();
    }

}
