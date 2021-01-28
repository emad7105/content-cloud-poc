package be.heydari.contentcloud.gateway.lazyabac;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "opa.service")
public class OPAClientConfig {

    private String baseUrl;
}
