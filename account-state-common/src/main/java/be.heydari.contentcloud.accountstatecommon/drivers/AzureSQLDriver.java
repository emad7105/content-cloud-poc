package be.heydari.contentcloud.accountstatecommon.drivers;

import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;
import java.util.Map;

public class AzureSQLDriver implements DatabaseDriver {

    @Override
    public void before() {
        System.setProperty("spring.jpa.properties.hibernate.dialect", "org.hibernate.dialect.SQLServer2012Dialect");
        System.setProperty("spring.jpa.hibernate.ddl-auto", "create");
    }

    @Override
    public String name() {
        return "AzureSQL";
    }

    @Override
    public DataSource getDataSource() {
        DataSourceBuilder builder = DataSourceBuilder.create();
        builder.driverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        builder.url(String.format("jdbc:sqlserver://%s.database.windows.net:1433;database=%s;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;", databaseServer(), databaseName()));
        builder.username(username());
        builder.password(password());
        return builder.build();
    }

    private String databaseServer() {
        return env().get("DB_SERVER");
    }

    private String databaseName() {
        return env().get("DB_NAME");
    }

    private String username() {
        String server = databaseServer();
        return String.format("spring@%s", server);
    }

    private String password() {
        return env().get("DB_PASSWORD");
    }

    private Map<String, String> env() {
        return System.getenv();
    }
}
