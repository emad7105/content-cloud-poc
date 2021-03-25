package be.heydari.contentcloud.accountstateservice.drivers;

import javax.sql.DataSource;

public interface DatabaseDriver {

    String name();

    DataSource getDataSource();

    default void before() { }
}
