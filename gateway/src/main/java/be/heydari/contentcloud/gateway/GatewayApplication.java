package be.heydari.contentcloud.gateway;

import be.heydari.contentcloud.gateway.filters.AuthnEndWebFilter;
import be.heydari.contentcloud.gateway.filters.AuthnStartWebFilter;
import be.heydari.lazyabacfilter.EnableOPAFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import reactor.core.publisher.Mono;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebSession;

@SpringBootApplication
@RestController
@EnableOPAFilter
@ComponentScan("be.heydari.lazyabacfilter")
public class GatewayApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(GatewayApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@GetMapping(value = "/token")
	public Mono<String> getHome(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) {
		return Mono.just(authorizedClient.getAccessToken().getTokenValue());
	}

	@GetMapping("/")
	public Mono<String> index(WebSession session) {
		return Mono.just(session.getId());
	}

	@Bean
	public AuthnStartWebFilter authnStartWebFilter() {
		return new AuthnStartWebFilter();
	}

	@Bean
	public AuthnEndWebFilter authnEndWebFilter() {
		return new AuthnEndWebFilter();
	}
}
