package be.heydari.contentcloud.accountstatecommon.drivers;

import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Map;

public class MSSQLDriver implements DatabaseDriver {

    @Override
    public String name() {
        return "MSSQL";
    }

    @Override
    public DataSource getDataSource() {
        DataSourceBuilder builder = DataSourceBuilder.create();
        builder.driverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        builder.url(String.format("jdbc:sqlserver://%s:1433;databaseName=%s", serverName(), databaseName()));
        builder.username(username());
        builder.password(password());
        return builder.build();
    }

    @Override
    public void before() {
        createDatabaseIfNotExists();
        setHibernateProperties();
    }

    private void createDatabaseIfNotExists() {
        String connectionURL = String.format("jdbc:sqlserver://%s:1433;user=%s;password=%s", serverName(), username(), password());

        try {
            Connection connection =
                DriverManager.getConnection(connectionURL);
            Statement stmt = connection.createStatement();

            String query = String.format("IF NOT EXISTS(SELECT * FROM sys.Databases WHERE Name = '%s')\n" +
                "BEGIN\n" +
                "  CREATE DATABASE %s;\n" +
                "END;\n", databaseName(), databaseName());

            System.out.println("creating database:\n" + query);

            stmt.execute(query);
            connection.commit();
            connection.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setHibernateProperties() {
        System.setProperty("spring.jpa.properties.hibernate.dialect", "org.hibernate.dialect.SQLServer2012Dialect");
        System.setProperty("spring.jpa.hibernate.ddl-auto", "create");
    }

    private String serverName() {
        return env().getOrDefault("DB_SERVER", "localhost");
    }

    private String databaseName() {
        return env().getOrDefault("DB_NAME", "AccountStates");
    }

    private String username() {
        return env().getOrDefault("DB_USER", "SA");
    }

    private String password() {
        return env().getOrDefault("DB_PASSWORD", "s3cr3t_p@ssw0rd");
    }

    private Map<String, String> env() {
        return System.getenv();
    }
}
