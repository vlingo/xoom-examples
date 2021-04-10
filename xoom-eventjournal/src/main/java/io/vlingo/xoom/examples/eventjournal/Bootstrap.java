package io.vlingo.xoom.examples.eventjournal;

import io.vlingo.xoom.symbio.store.journal.jdbc.JDBCDispatcherControlDelegate;
import io.vlingo.xoom.symbio.store.journal.jdbc.JDBCJournalInstantWriter;
import org.flywaydb.core.Flyway;

import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.examples.eventjournal.counter.Counter;
import io.vlingo.xoom.examples.eventjournal.counter.CounterActor;
import io.vlingo.xoom.examples.eventjournal.counter.CounterQuery;
import io.vlingo.xoom.examples.eventjournal.counter.CounterQueryActor;
import io.vlingo.xoom.examples.eventjournal.interest.NoopConfigurationInterest;
import io.vlingo.xoom.examples.eventjournal.interest.NoopEventJournalDispatcher;
import io.vlingo.xoom.symbio.Entry;
import io.vlingo.xoom.symbio.EntryAdapterProvider;
import io.vlingo.xoom.symbio.store.DataFormat;
import io.vlingo.xoom.symbio.store.common.jdbc.Configuration;
import io.vlingo.xoom.symbio.store.common.jdbc.DatabaseType;
import io.vlingo.xoom.symbio.store.journal.Journal;
import io.vlingo.xoom.symbio.store.journal.JournalReader;
import io.vlingo.xoom.symbio.store.journal.jdbc.JDBCJournalActor;

import java.util.Arrays;

public class Bootstrap {
    private static final String DB_URL = "jdbc:postgresql://[::1]:5432/";
    private static final String DB_USER = "xoom_test";
    private static final String DB_PWD = "xoom123";
    private static final String DB_NAME = "xoom_test";

    public static void main(String[] args) throws Exception {
        Flyway.configure().dataSource(DB_URL, DB_USER, DB_PWD).load().migrate();
        final Configuration configuration = new Configuration(
        		DatabaseType.Postgres,
                new NoopConfigurationInterest(),
                "org.postgresql.Driver",
                DataFormat.Text,
                DB_URL,
                DB_NAME,
                DB_USER,
                DB_PWD,
                false,
                "",
                false
        );

        final World world = World.startWithDefaults("event-journal");

        final NoopEventJournalDispatcher journalDispatcher = new NoopEventJournalDispatcher();
        Journal<String> journal = Journal.using(world.stage(), JDBCJournalActor.class, journalDispatcher, configuration);

        final Counter counter = world.actorFor(
                Counter.class,
                Definition.has(CounterActor.class, Definition.parameters(DB_NAME, journal))
        );

        final CounterQuery counterQuery = world.actorFor(
                CounterQuery.class,
                Definition.has(CounterQueryActor.class, Definition.parameters(journal.journalReader(DB_NAME).<JournalReader<Entry<?>>>await(), new EntryAdapterProvider()))
        );

        for (int i = 0; i < 5000; i++) {
            if (i % 10 == 0) {
                counter.decrease();
            } else {
                counter.increase();
            }

            pause();
            counterQuery.counter().andThenConsume(System.out::println);
        }

        world.terminate();
    }

    // This shouldn't be done in production code. It's to simulate some load on the
    // reader
    static void pause() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {

        }
    }
}
