package be.heydari.contentcloud.accountstateservice;

import java.io.IOException;
import java.net.URI;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

import be.heydari.contentcloud.accountstateservice.provisioning.Provisioner;
import be.heydari.lib.converters.solr.SolrUtils;
import be.heydari.lib.expressions.Disjunction;
import brave.Span;
import brave.SpanCustomizer;
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
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.context.annotation.RequestScope;

import static org.springframework.security.config.Customizer.withDefaults;

@SpringBootApplication
@EnableAspectJAutoProxy()
@EnableAbac
//@EnableWebSecurity
public class AccountStateApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(AccountStateApplication.class, args);

        Provisioner provisioner = applicationContext.getBean(Provisioner.class);
        provisioner.provision();
    }

    @Configuration
//    @EnableWebSecurity
    public static class SecurityConfig extends WebSecurityConfigurerAdapter {
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


//            http.authorizeExchange(exchanges -> exchanges.anyExchange().authenticated())
//                .oauth2Login(withDefaults());
//            http.csrf().disable();
//
//            http.addFilterAfter(new AuthnStartWebFilter(), SecurityWebFiltersOrder.FIRST);
//
//            http.addFilterBefore(new AuthnEndWebFilter(), SecurityWebFiltersOrder.LAST);
//
//            return http.build();
        }
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
