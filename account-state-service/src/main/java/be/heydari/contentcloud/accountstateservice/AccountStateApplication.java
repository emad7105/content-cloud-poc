package be.heydari.contentcloud.accountstateservice;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

//import be.heydari.contentcloud.accountstateservice.drivers.*;
import be.heydari.contentcloud.accountstatecommon.drivers.*;
import be.heydari.contentcloud.accountstateservice.provisioning.HardcodedProvisioner;
import be.heydari.lib.converters.solr.SolrUtils;
import be.heydari.lib.expressions.Disjunction;
import brave.SpanCustomizer;
import com.example.abac_spike.ABACContext;
import com.example.abac_spike.EnableAbac;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.content.rest.config.ContentRestConfigurer;
import org.springframework.content.rest.config.RestConfiguration;
import org.springframework.content.solr.AttributeProvider;
import org.springframework.content.solr.FilterQueryProvider;
import org.springframework.content.solr.SolrProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.web.context.annotation.RequestScope;


@SpringBootApplication
@EnableAspectJAutoProxy()
@EnableAbac
//@EnableWebSecurity
public class AccountStateApplication {
    private static final DatabaseDrivers drivers = new DatabaseDrivers(new H2Driver(), new MSSQLDriver(), new PostgresDriver(), new AzureSQLDriver());

    public static void main(String[] args) {
        DatabaseDriver driver = drivers.getByEnv();
        driver.before();
//        System.setProperty("spring.jpa.properties.hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
//        System.setProperty("spring.jpa.hibernate.ddl-auto", "create");
//
//
//        try {
//
//            Connection connection =
//                DriverManager.getConnection("jdbc:sqlserver://localhost:1433;user=SA;password=s3cr3t_p@ssw0rd");
//            Statement stmt = connection.createStatement();
//
//            String query = "IF NOT EXISTS(SELECT * FROM sys.Databases WHERE Name = 'AccountStates')\n" +
//                "BEGIN\n" +
//                "  CREATE DATABASE AccountStates;\n" +
//                "END;\n";
//            System.out.println("query:\n" + query);
//
//            stmt.execute(query);
//
//            connection.commit();
//            connection.close();
//
//        } catch (Exception e) {
//           throw new RuntimeException(e);
//        }
//
//        System.out.println("created database");
//
//        System.setProperty("spring.jpa.properties.hibernate.dialect", "org.hibernate.dialect.SQLServer2012Dialect");
//        System.setProperty("spring.jpa.hibernate.ddl-auto", "create");
////        System.setProperty("spring.jpa.hibernate.hbm2ddl.auto", "create");


        ConfigurableApplicationContext applicationContext = SpringApplication.run(AccountStateApplication.class, args);
        HardcodedProvisioner provisioner = applicationContext.getBean(HardcodedProvisioner.class);

        String scaleString = System.getenv().getOrDefault("DB_SCALER", "100");
        int scale = Integer.parseInt(scaleString);
        provisioner.setScale(scale);
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
    public class JpaConfig {
        @Bean
        public DataSource getDataSource() {
            return drivers.getByEnv().getDataSource();
//            String driverName = System.getenv("DB_DRIVER");
//
//            driverName = driverName == null ? "H2" : driverName;
//
//            switch (driverName) {
//                case "H2":
//                    return getH2DataSource();
//                case "Postgres":
//                    return getPostgresDataSource();
//                case "MSSQL":
//                    return getSqlServerDataSource();
//                default:
//                    throw new IllegalArgumentException("unknown database driver: " + driverName);
//            }
        }

        public DataSource getH2DataSource() {
            DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
            dataSourceBuilder.driverClassName("org.h2.Driver");
            dataSourceBuilder.url("jdbc:h2:mem:");
            dataSourceBuilder.username("sa");
            dataSourceBuilder.password("");
            return dataSourceBuilder.build();
        }

        public DataSource getPostgresDataSource() {
            Map<String, String> env = System.getenv();
            String dbURL = env.getOrDefault("DB_URL", "localhost:5432/account-states");
            String username = env.getOrDefault("DB_USERNAME", "username");
            String password = env.getOrDefault("DB_PASSWORD", "password");

//            DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
//            dataSourceBuilder.driverClassName("org.postgresql.Driver");
//            dataSourceBuilder.url(String.format("jdbc:postgresql://%s", dbURL));
//            dataSourceBuilder.username(username);
//            dataSourceBuilder.password(password);
//            return dataSourceBuilder.build();

            PGSimpleDataSource ds = new PGSimpleDataSource();
            ds.setDatabaseName("account-state");
            ds.setServerNames(new String[]{"localhost"});
            ds.setUser("username");
            ds.setPassword("password");
            return ds;
        }

        public DataSource getSqlServerDataSource() {
            Map<String, String> env = System.getenv();
            String dbURL = env.getOrDefault("DB_URL", "localhost:1433;databaseName=AccountStates");
            String username = env.getOrDefault("DB_USERNAME", "SA");
            String password = env.getOrDefault("DB_PASSWORD", "s3cr3t_p@ssw0rd");

            DataSourceBuilder dataSourceBuilder  = DataSourceBuilder.create();
            dataSourceBuilder.driverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            dataSourceBuilder.url(String.format("jdbc:sqlserver://%s", dbURL));
            dataSourceBuilder.username(username);
            dataSourceBuilder.password(password);
            return dataSourceBuilder.build();
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

//        @Bean
//        public AttributeProvider<AccountState> syncer() {
//            return new AttributeProvider<AccountState>() {
//
//                @Override
//                public Map<String, String> synchronize(AccountState entity) {
//                    Map<String, String> attrs = new HashMap<>();
//                    attrs.put("broker.id", entity.getBroker().getId().toString());
//                    return attrs;
//                }
//            };
//        }

        @Bean
        public AttributeProvider<AccountState> syncer() {
            return new AttributeProvider<AccountState>() {
                @Override
                public Map<String, String> synchronize(AccountState entity) {
                    Map<String, String> attrs = new HashMap<>();
                    attrs.put("broker.id", entity.getBroker().getId().toString());
                    return attrs;
                }
            };
        }

        @Bean
        @RequestScope
        public FilterQueryProvider fqProvider() {
            return new FilterQueryProvider() {

                private @Autowired
                HttpServletRequest request;

                @Override
                public String[] filterQueries(Class<?> entity) {
                    Disjunction abacContext = ABACContext.getCurrentAbacContext();
                    return SolrUtils.from(abacContext, "");
                }
            };
        }
    }
}
