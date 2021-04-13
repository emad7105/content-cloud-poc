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

        String scaleString = System.getenv().getOrDefault("DB_SCALER", "100");
        int scale = Integer.parseInt(scaleString);
        provisioner.setScale(scale);

        provisioner.provision();
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
