package be.heydari.contentcloud.accountstatecommon.drivers;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.util.Map;

public class PostgresDriver implements DatabaseDriver {

    @Override
    public String name() {
        return "Postgres";
    }

    @Override
    public DataSource getDataSource() {
        PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setDatabaseName(databaseName());
        ds.setServerNames(new String[]{serverName()});
        ds.setUser(username());
        ds.setPassword(password());
        return ds;
    }

    @Override
    public void before() {
        System.setProperty("spring.jpa.properties.hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        System.setProperty("spring.jpa.hibernate.ddl-auto", "create-drop");
    }

    private String serverName() {
        return env().getOrDefault("DB_SERVER", "localhost");
    }

    private String databaseName() {
        return env().getOrDefault("DB_NAME", "AccountState");
    }

    private String username() {
        return env().getOrDefault("DB_USER", "username");
    }

    private String password() {
        return env().getOrDefault("DB_PASSWORD", "password");
    }

    public Map<String, String> env() {
        return System.getenv();
    }
}
