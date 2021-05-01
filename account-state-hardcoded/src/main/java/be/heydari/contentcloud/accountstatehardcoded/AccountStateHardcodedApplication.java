package be.heydari.contentcloud.accountstatehardcoded;

import be.heydari.contentcloud.accountstatecommon.drivers.*;
import be.heydari.contentcloud.accountstatehardcoded.provisioning.Provisioner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@SpringBootApplication
@EntityScan
public class AccountStateHardcodedApplication {

    public static final DatabaseDrivers drivers = new DatabaseDrivers(
        new H2Driver(),
        new MSSQLDriver(),
        new PostgresDriver(),
        new AzureSQLDriver()
    );

    public static void main(String[] args) {
        DatabaseDriver driver = drivers.getByEnv();
        driver.before();
        ConfigurableApplicationContext applicationContext = SpringApplication.run(AccountStateHardcodedApplication.class, args);
        Provisioner provisioner = applicationContext.getBean(Provisioner.class);

        String resetString = System.getenv().getOrDefault("DB_RESET", "false");
        boolean reset = Boolean.parseBoolean(resetString);
        String recordCountString = System.getenv().getOrDefault("DB_RECORD_COUNT", "10000");
        int recordCount = Integer.parseInt(recordCountString);
        String brokerCountString = System.getenv().getOrDefault("DB_BROKER_COUNT", "10");
        int brokerCount = Integer.parseInt(brokerCountString);
        provisioner.provision(recordCount, brokerCount, reset);
    }

    @Bean
    public ActiveRedactor policySelector() {
        return new ActiveRedactor();
    }

    @Configuration
    public class JpaConfig {
        @Bean
        public DataSource getDataSource() {
            return drivers.getByEnv().getDataSource();
        }
    }
}
