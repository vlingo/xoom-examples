package io.vlingo.eventjournal;

import org.flywaydb.core.Flyway;

import io.vlingo.actors.Definition;
import io.vlingo.actors.World;
import io.vlingo.eventjournal.counter.Counter;
import io.vlingo.eventjournal.counter.CounterActor;
import io.vlingo.eventjournal.counter.CounterQuery;
import io.vlingo.eventjournal.counter.CounterQueryActor;
import io.vlingo.eventjournal.interest.NoopConfigurationInterest;
import io.vlingo.eventjournal.interest.NoopEventJournalListener;
import io.vlingo.symbio.store.journal.Journal;
import io.vlingo.symbio.store.journal.jdbc.postgres.PostgresJournalActor;
import io.vlingo.symbio.store.state.StateStore;
import io.vlingo.symbio.store.state.jdbc.Configuration;

public class Bootstrap {
    private static final String DB_URL = "jdbc:postgresql://[::1]:5432/";
    private static final String DB_USER = "vlingo_test";
    private static final String DB_PWD = "vlingo123";
    private static final String DB_NAME = "vlingo_test";

    public static void main(String[] args) throws Exception {
        Flyway.configure().dataSource(DB_URL, DB_USER, DB_PWD).load().migrate();
        final Configuration configuration = new Configuration(
                new NoopConfigurationInterest(),
                "org.postgresql.Driver",
                StateStore.DataFormat.Text,
                DB_URL,
                DB_NAME,
                DB_USER,
                DB_PWD,
                false,
                "",
                false
        );

        final World world = World.startWithDefaults("event-journal");
        @SuppressWarnings("unchecked")
        Journal<String> journal = world.actorFor(
                Definition.has(PostgresJournalActor.class, Definition.parameters(configuration, new NoopEventJournalListener())),
                Journal.class
        );

        final Counter counter = world.actorFor(
                Definition.has(CounterActor.class, Definition.parameters(DB_NAME, journal)),
                Counter.class
        );

        final CounterQuery counterQuery = world.actorFor(
                Definition.has(CounterQueryActor.class, Definition.parameters(journal.journalReader(DB_NAME).await())),
                CounterQuery.class
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
