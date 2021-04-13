package be.heydari.contentcloud.accountstatepostfilter;

import be.heydari.contentcloud.accountstatecommon.drivers.*;
import be.heydari.contentcloud.accountstatepostfilter.provisioning.Provisioner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@SpringBootApplication
@EntityScan
public class AccountStatePostfilterApplication {

    private static final DatabaseDrivers drivers = new DatabaseDrivers(new H2Driver(), new MSSQLDriver(), new PostgresDriver(), new AzureSQLDriver());

    public static void main(String[] args) {
        DatabaseDriver driver = drivers.getByEnv();

        driver.before();

        ConfigurableApplicationContext applicationContext = SpringApplication.run(AccountStatePostfilterApplication.class, args);
        Provisioner provisioner = applicationContext.getBean(Provisioner.class);

        String scaleString = System.getenv().getOrDefault("DB_SCALER", "100");
        int scale = Integer.parseInt(scaleString);
        provisioner.setScale(scale);

        provisioner.provision();
    }

    @Configuration
    public class JpaConfig {
        @Bean
        public DataSource getDataSource() {
            return drivers.getByEnv().getDataSource();
        }
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
