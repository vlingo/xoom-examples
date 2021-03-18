package io.vlingo.cars;

import io.vlingo.common.config.EnvVarProperties;

import java.io.IOException;
import java.util.Properties;

public class Config {
    public final String databaseDriver;
    public final String databaseUrl;
    public final String databaseName;
    public final String databaseUsername;
    public final String databasePassword;
    public final String databaseOriginator;

    public static Config load() throws IOException {
        final Properties properties = new EnvVarProperties();
        final String propertiesFile = "/vlingo-cars.properties";

        properties.load(Config.class.getResourceAsStream(propertiesFile));

        return new Config(
                properties.getProperty("database.driver"),
                properties.getProperty("database.url"),
                properties.getProperty("database.name"),
                properties.getProperty("database.username"),
                properties.getProperty("database.password"),
                properties.getProperty("database.originator"));
    }

    public Config(String databaseDriver, String databaseUrl, String databaseName, String databaseUsername, String databasePassword, String databaseOriginator) {
        this.databaseDriver = databaseDriver;
        this.databaseUrl = databaseUrl;
        this.databaseName = databaseName;
        this.databaseUsername = databaseUsername;
        this.databasePassword = databasePassword;
        this.databaseOriginator = databaseOriginator;
    }
}
