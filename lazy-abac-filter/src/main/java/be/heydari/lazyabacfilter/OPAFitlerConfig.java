package be.heydari.lazyabacfilter;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@Data
@ConfigurationProperties(prefix = "opa.service")
public class OPAFitlerConfig {

    private String baseUrl;

    @Bean
    public ABACPolicyGatewayFilterFactory abacPolicyGatewayFilterFactory() {
        ABACPolicyGatewayFilterFactory abacPolicyGatewayFilterFactory = new ABACPolicyGatewayFilterFactory();
        abacPolicyGatewayFilterFactory.setOpaClient(new OPAClient(baseUrl));
        return abacPolicyGatewayFilterFactory;
    }

    @Bean
    public OpaABACWebFilter opaABACWebFilter() {
        return new OpaABACWebFilter();
    }
}
