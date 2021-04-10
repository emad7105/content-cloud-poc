package be.heydari.lazyabacfilter;

//import brave.Tracer;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Data
@ConfigurationProperties(prefix = "opa.service")
@ConditionalOnProperty(name = "opa.service.enabled", havingValue = "true")
public class OPAFilterConfig {
    private boolean enabled = true;

    private String baseUrl;

//    @Autowired
//    private Tracer tracer;

    @Bean
    public WebClient webClient() {
        System.out.println("webClient");

        HttpClient client = HttpClient.create();

        return WebClient
            .builder()
            .clientConnector(new ReactorClientHttpConnector(client))
            .baseUrl(baseUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }

    @Bean
    public ABACPolicyGatewayFilterFactory abacPolicyGatewayFilterFactory(WebClient webClient) {
        ABACPolicyGatewayFilterFactory abacPolicyGatewayFilterFactory = new ABACPolicyGatewayFilterFactory();
        abacPolicyGatewayFilterFactory.setOpaClient(new OPAClient(baseUrl/*, tracer*/));
        abacPolicyGatewayFilterFactory.setOpaClientAsync(new OPAClientAsync(baseUrl/*, tracer*/, webClient));
        abacPolicyGatewayFilterFactory.setEnabled(enabled);
        return abacPolicyGatewayFilterFactory;
    }


    @Bean
    public OpaABACWebFilter opaABACWebFilter() {
        return new OpaABACWebFilter();
    }
}
