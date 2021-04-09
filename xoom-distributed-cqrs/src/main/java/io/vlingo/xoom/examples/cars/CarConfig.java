package io.vlingo.xoom.examples.cars;

import io.vlingo.xoom.common.config.EnvVarProperties;

import java.io.IOException;
import java.util.Properties;

public class CarConfig {
    public final String databaseDriver;
    public final String databaseUrl;
    public final String databaseName;
    public final String databaseUsername;
    public final String databasePassword;
    public final String databaseOriginator;

    public static CarConfig load() throws IOException {
        final Properties properties = new EnvVarProperties();
        final String propertiesFile = "/xoom-cars.properties";

        properties.load(CarConfig.class.getResourceAsStream(propertiesFile));

        return new CarConfig(
                properties.getProperty("database.driver"),
                properties.getProperty("database.url"),
                properties.getProperty("database.name"),
                properties.getProperty("database.username"),
                properties.getProperty("database.password"),
                properties.getProperty("database.originator"));
    }

    public CarConfig(String databaseDriver, String databaseUrl, String databaseName, String databaseUsername, String databasePassword, String databaseOriginator) {
        this.databaseDriver = databaseDriver;
        this.databaseUrl = databaseUrl;
        this.databaseName = databaseName;
        this.databaseUsername = databaseUsername;
        this.databasePassword = databasePassword;
        this.databaseOriginator = databaseOriginator;
    }
}
