package io.vlingo.eventjournal;

import io.vlingo.actors.Definition;
import io.vlingo.actors.World;
import io.vlingo.eventjournal.counter.Counter;
import io.vlingo.eventjournal.counter.CounterActor;
import io.vlingo.eventjournal.counter.CounterQuery;
import io.vlingo.eventjournal.counter.CounterQueryActor;
import io.vlingo.eventjournal.interest.NoopConfigurationInterest;
import io.vlingo.eventjournal.interest.NoopEventJournalListener;
import io.vlingo.symbio.store.eventjournal.EventJournal;
import io.vlingo.symbio.store.state.StateStore;
import io.vlingo.symbio.store.state.jdbc.Configuration;
import io.vlingo.symbio.store.state.jdbc.postgres.eventjournal.PostgresEventJournalActor;
import org.flywaydb.core.Flyway;

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
        EventJournal journal = world.actorFor(
                Definition.has(PostgresEventJournalActor.class, Definition.parameters(configuration, new NoopEventJournalListener())),
                EventJournal.class
        );

        final Counter counter = world.actorFor(
                Definition.has(CounterActor.class, Definition.parameters(DB_NAME, journal)),
                Counter.class
        );

        final CounterQuery counterQuery = world.actorFor(
                Definition.has(CounterQueryActor.class, Definition.parameters(journal.eventJournalReader(DB_NAME).await())),
                CounterQuery.class
        );

        for (int i = 0; i < 5000; i++) {
            if (i % 10 == 0) {
                counter.decrease();
            } else {
                counter.increase();
            }

            counterQuery.counter().andThenConsume(System.out::println);
            pause();
        }
    }

    static void pause() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {

        }
    }
}
