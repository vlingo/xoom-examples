package io.vlingo.hike.domain.route;

import io.vlingo.hike.ActorTest;
import io.vlingo.hike.domain.route.events.WalkedThrough;
import io.vlingo.symbio.Entry;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class RouteTest extends ActorTest {
    private Route route;
    private double x, y, z;

    @Before
    public void setUp() throws Exception {
        route = world().actorFor(Route.class, RouteActor.class, UUID.randomUUID()).actor();

        x = new Random().nextDouble();
        y = new Random().nextDouble();
        z = new Random().nextDouble();
    }

    @Test
    public void shouldAggregateRoutePoints() {
        route.walkedThrough(RoutePoint.of(x, y, z));

        Entry<String> entry = entry(0);
        assertEquals(WalkedThrough.class.getCanonicalName(), entry.type);
    }
}
