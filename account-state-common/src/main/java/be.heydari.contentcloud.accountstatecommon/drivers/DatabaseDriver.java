package be.heydari.contentcloud.accountstatecommon.drivers;

import javax.sql.DataSource;

public interface DatabaseDriver {

    String name();

    DataSource getDataSource();

    default void before() { }
}
