package be.heydari.lazyabacfilter;

import brave.Tracer;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@Data
@ConfigurationProperties(prefix = "opa.service")
public class OPAFilterConfig {

    private String baseUrl;

    @Autowired
    private Tracer tracer;

    @Bean
    public ABACPolicyGatewayFilterFactory abacPolicyGatewayFilterFactory() {
        ABACPolicyGatewayFilterFactory abacPolicyGatewayFilterFactory = new ABACPolicyGatewayFilterFactory();
        abacPolicyGatewayFilterFactory.setOpaClient(new OPAClient(baseUrl, tracer));
        return abacPolicyGatewayFilterFactory;
    }

    @Bean
    public OpaABACWebFilter opaABACWebFilter() {
        return new OpaABACWebFilter();
    }
}
