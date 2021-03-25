package be.heydari.contentcloud.accountstatecommon.drivers;

import org.jboss.logging.Logger;

import java.util.HashMap;
import java.util.Map;

public class DatabaseDrivers {
    Logger LOGGER = Logger.getLogger(DatabaseDriver.class);

    private final Map<String, DatabaseDriver> drivers = new HashMap<>();

    public DatabaseDrivers() { }

    public DatabaseDrivers(DatabaseDriver ...drivers) {
        registerDrivers(drivers);
    }

    public void registerDriver(DatabaseDriver driver) {
        this.drivers.put(driver.name(), driver);
    }

    public void registerDrivers(DatabaseDriver ...drivers) {
        for (DatabaseDriver driver: drivers) {
            registerDriver(driver);
        }
    }

    public DatabaseDriver getByEnv() {
        String driverName = System.getenv("DB_DRIVER");
        LOGGER.info("using driver " + driverName);
        return getDriver(driverName);
    }

    public DatabaseDriver getDriver(String driverName) {
        DatabaseDriver driver = drivers.get(driverName);
        if (driver == null) {
            throw new RuntimeException("unknown driver: " + driverName);
        }

        return driver;
    }
}
