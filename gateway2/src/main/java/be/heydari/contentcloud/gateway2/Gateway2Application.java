package be.heydari.contentcloud.gateway2;

import be.heydari.lazyabacfilter.ABACPolicyGatewayFilterFactory;
import be.heydari.lazyabacfilter.EnableOPAFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebSession;
import reactor.blockhound.BlockHound;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@SpringBootApplication
@RestController
@EnableOPAFilter
@ComponentScan("be.heydari.lazyabacfilter")
public class Gateway2Application {
	private static final Logger LOGGER = LoggerFactory.getLogger(Gateway2Application.class);


	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(Gateway2Application.class, args);
		Environment env = ctx.getEnvironment();
		String property = env.getProperty("spring.sleuth.sampler.probability");
		System.out.println("sampling rate: " + property);

		Arrays.stream(
				ctx.getBeanDefinitionNames()
		).sorted().forEach(component -> System.out.println("component: " + component));

	}

	@GetMapping(value = "/token")
	public Mono<String> getHome(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) {
		return Mono.just(authorizedClient.getAccessToken().getTokenValue());
	}

	@GetMapping("/")
	public Mono<String> index(WebSession session) {
		return Mono.just(session.getId());
	}

}
