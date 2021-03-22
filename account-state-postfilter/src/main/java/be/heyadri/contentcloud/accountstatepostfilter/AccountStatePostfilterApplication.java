package be.heyadri.contentcloud.accountstatepostfilter;

import be.heyadri.contentcloud.accountstatepostfilter.provisioning.Provisioner;
import brave.SpanCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.servlet.*;
import javax.sql.DataSource;
import java.io.IOException;

@SpringBootApplication
@EntityScan
public class AccountStatePostfilterApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(AccountStatePostfilterApplication.class, args);
        Provisioner provisioner = applicationContext.getBean(Provisioner.class);
        provisioner.provision();
    }
//
//
//    @Configuration
//    public class SecurityConfig extends WebSecurityConfigurerAdapter {
//        @Override
//        public void configure(HttpSecurity http) throws Exception {
//            // more info or filter order see the FilterComparator.class
//            // for this implementation we just picked the first and the last one.
//            http.authorizeRequests()
//                .anyRequest().authenticated()
//                .and()
//                .addFilterBefore(new Filter() {
//                    @Override
//                    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//                        SpanCustomizer span = (SpanCustomizer) servletRequest.getAttribute("brave.SpanCustomizer");
//                        span.annotate("start authn verification");
//                        filterChain.doFilter(servletRequest, servletResponse);
//                    }
//                }, ChannelProcessingFilter.class)
//                .addFilterAfter(new Filter() {
//                    @Override
//                    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//                        SpanCustomizer span = (SpanCustomizer) servletRequest.getAttribute("brave.SpanCustomizer");
//                        span.annotate("end authn verification");
//                        filterChain.doFilter(servletRequest, servletResponse);
//                    }
//                }, SwitchUserFilter.class)
////                 validate jwt token
//                .oauth2ResourceServer(oauth -> oauth.jwt());
//        }
//    }


//    @Configuration
//    @EnableJpaRepositories
//    @EnableTransactionManagement
//    public class AccountStateConfig {
//
//        @Bean
//        public DataSource dataSource() {
//            EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
//            return builder.setType(EmbeddedDatabaseType.HSQL).build();
//        }
//
//        @Bean
//        public EntityManagerFactory entityManagerFactory() {
//            HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//            vendorAdapter.setGenerateDdl(true);
//
//            LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
//            factory.setJpaVendorAdapter(vendorAdapter);
//            factory.setPackagesToScan("be.heydari.contentcloud.accountstatepostfilter");
//            factory.setDataSource(dataSource());
//            factory.afterPropertiesSet();
//            return factory.getObject();
//        }
//
//        @Bean
//        public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
//            JpaTransactionManager txManager = new JpaTransactionManager();
//            txManager.setEntityManagerFactory(entityManagerFactory);
//            return txManager;
//        }
//    }



}
