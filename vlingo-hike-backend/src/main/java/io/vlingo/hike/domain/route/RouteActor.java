package io.vlingo.hike.domain.route;

import io.vlingo.hike.domain.route.events.WalkedThrough;
import io.vlingo.lattice.model.sourcing.EventSourced;

import java.util.UUID;
import java.util.function.BiConsumer;

public class RouteActor extends EventSourced<String> implements Route {
    private final RouteId id;

    public RouteActor(UUID hikerId) {
        this.id = RouteId.newFor(hikerId);
    }

    @Override
    public void walkedThrough(RoutePoint routePoint) {
        apply(WalkedThrough.through(routePoint.x, routePoint.y, routePoint.z));
    }

    static {
        BiConsumer<RouteActor, WalkedThrough> applyWalkedThroughFn = RouteActor::applyWalkedThrough;
        EventSourced.registerConsumer(RouteActor.class, WalkedThrough.class, applyWalkedThroughFn);
    }


    private void applyWalkedThrough(WalkedThrough walkedThrough) {

    }

    @Override
    protected String streamName() {
        return streamNameFrom(":", id.hiker.toString(), id.route.toString());
    }
}
