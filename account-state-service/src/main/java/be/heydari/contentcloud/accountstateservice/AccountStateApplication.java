package be.heydari.contentcloud.accountstateservice;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import be.heydari.lib.converters.solr.SolrUtils;
import be.heydari.lib.expressions.Disjunction;
import com.example.abac_spike.ABACContext;
import com.example.abac_spike.EnableAbac;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.content.rest.config.ContentRestConfigurer;
import org.springframework.content.rest.config.RestConfiguration;
import org.springframework.content.solr.AttributeProvider;
import org.springframework.content.solr.FilterQueryProvider;
import org.springframework.content.solr.SolrProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.context.annotation.RequestScope;

@SpringBootApplication
@EnableAspectJAutoProxy()
@EnableAbac
public class AccountStateApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountStateApplication.class, args);
    }

    @Configuration
    public static class Config {

        @Bean
        public SolrClient solrClient(
                SolrProperties props) {
            props.setUser("solr");
            props.setPassword("SolrRocks");
            return new HttpSolrClient.Builder(props.getUrl()).build();
        }

        @Bean
        public ContentRestConfigurer restConfigurer() {
            return new ContentRestConfigurer() {
                @Override
                public void configure(RestConfiguration config) {
                    config.setBaseUri(URI.create("/content"));
                }
            };
        }

        @Bean
        public AttributeProvider<AccountState> syncer() {
            return new AttributeProvider<AccountState>() {

                @Override
                public Map<String, String> synchronize(AccountState entity) {
                    Map<String,String> attrs = new HashMap<>();
                    attrs.put("broker.id", entity.getBroker().getId().toString());
                    return attrs;
                }
            };
        }

        @Bean
        @RequestScope
        public FilterQueryProvider fqProvider() {
            return new FilterQueryProvider() {

                private @Autowired HttpServletRequest request;

                @Override
                public String[] filterQueries(Class<?> entity) {
                    Disjunction abacContext = ABACContext.getCurrentAbacContext();
                    return SolrUtils.from(abacContext,"");
                }
            };
        }
    }
}
