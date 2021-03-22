package be.heyadri.contentcloud.accountstatepostfilter.opa;

import brave.Tracer;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "opa.service")
@Configuration
public class OPAClientConfig {
    private String baseUrl;

    private String query;

    private Tracer tracer;

    @Autowired
    public OPAClientConfig(Tracer tracer) {
        this.tracer = tracer;
    }
}
