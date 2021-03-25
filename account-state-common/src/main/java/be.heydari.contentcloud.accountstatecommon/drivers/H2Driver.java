package be.heydari.contentcloud.accountstatecommon.drivers;

import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;
import java.util.Map;

public class H2Driver implements DatabaseDriver {

    @Override
    public String name() {
        return "H2";
    }

    @Override
    public DataSource getDataSource() {
        DataSourceBuilder datasourceBuilder = DataSourceBuilder.create();
        datasourceBuilder.driverClassName("org.h2.driver");
        datasourceBuilder.url(url());
        datasourceBuilder.username(userName());
        datasourceBuilder.password(password());
        return datasourceBuilder.build();
    }

    private String url() {
        return env().getOrDefault("DATABASE_URL", "jdbc:h2:mem");
    }

    private String userName() {
        return env().getOrDefault("DATABASE_USERNAME", "sa");
    }

    private String password() {
        return env().getOrDefault("DATABASE_PASSWORD", "");
    }

    private Map<String, String> env() {
        return System.getenv();
    }
}
