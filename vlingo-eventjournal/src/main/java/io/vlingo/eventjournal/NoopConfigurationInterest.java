package io.vlingo.eventjournal;

import io.vlingo.symbio.store.state.jdbc.Configuration;

import java.sql.Connection;

public class NoopConfigurationInterest implements Configuration.ConfigurationInterest {
    @Override
    public void afterConnect(Connection connection) throws Exception {

    }

    @Override
    public void beforeConnect(Configuration configuration) throws Exception {

    }

    @Override
    public void createDatabase(Connection connection, String databaseName) throws Exception {

    }

    @Override
    public void dropDatabase(Connection connection, String databaseName) throws Exception {

    }
}
