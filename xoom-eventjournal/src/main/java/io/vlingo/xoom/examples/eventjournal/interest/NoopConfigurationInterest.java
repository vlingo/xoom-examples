package io.vlingo.xoom.examples.eventjournal.interest;

import java.sql.Connection;

import io.vlingo.xoom.symbio.store.common.jdbc.Configuration;

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
