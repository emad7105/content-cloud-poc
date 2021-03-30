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
    public void before() {
//        System.setProperty("spring.jpa.properties.hibernate.dialect", "org.hibernate.dialect.H2Dialect");
//        System.setProperty("spring.jpa.hibernate.ddl-auto", "create");
    }

    @Override
    public DataSource getDataSource() {
        DataSourceBuilder datasourceBuilder = DataSourceBuilder.create();
        datasourceBuilder.driverClassName("org.h2.Driver");
        datasourceBuilder.url(url());
        datasourceBuilder.username(userName());
        datasourceBuilder.password(password());
        return datasourceBuilder.build();
    }

    private String url() {
         return "jdbc:h2:mem:";
    }

    private String userName() {
        return "SA";
    }

    private String password() {
        return "";
    }
}
