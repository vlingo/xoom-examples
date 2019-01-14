package io.vlingo.hike.domain.route;

import io.vlingo.hike.domain.route.events.EmergencyRaised;
import io.vlingo.hike.domain.route.events.WalkedThrough;
import io.vlingo.lattice.model.sourcing.EventSourced;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

public class RouteActor extends EventSourced<String> implements Route {
    private final RouteId id;
    private final List<WalkedThrough> path;
    private boolean isOnEmergency;

    public RouteActor(UUID hikerId) {
        this.id = RouteId.newFor(hikerId);
        this.path = new ArrayList<>();
        this.isOnEmergency = false;
    }

    @Override
    public void walkedThrough(RoutePoint routePoint) {
        apply(WalkedThrough.through(LocalDateTime.now(), routePoint.x, routePoint.y, routePoint.z));
    }

    @Override
    public void emergency() {
        if (!path.isEmpty()) {
            apply(EmergencyRaised.on(LocalDateTime.now()));
        }
    }

    static {
        BiConsumer<RouteActor, WalkedThrough> applyWalkedThroughFn = RouteActor::applyWalkedThrough;
        EventSourced.registerConsumer(RouteActor.class, WalkedThrough.class, applyWalkedThroughFn);

        BiConsumer<RouteActor, EmergencyRaised> applyEmergencyRaisedFn = RouteActor::applyEmergencyRaised;
        EventSourced.registerConsumer(RouteActor.class, EmergencyRaised.class, applyEmergencyRaisedFn);
    }

    private void applyWalkedThrough(WalkedThrough walkedThrough) {
        this.path.add(walkedThrough);
    }

    private void applyEmergencyRaised(EmergencyRaised emergency) {
        this.isOnEmergency = true;
    }

    @Override
    protected String streamName() {
        return streamNameFrom(":", id.hiker.toString(), id.route.toString());
    }
}
