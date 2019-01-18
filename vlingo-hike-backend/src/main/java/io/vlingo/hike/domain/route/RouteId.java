package io.vlingo.hike.domain.route;

import lombok.Value;

import java.util.UUID;

@Value(staticConstructor = "of")
public class RouteId {
    public final UUID hiker, route;

    public static RouteId newFor(UUID hiker) {
        return RouteId.of(hiker, UUID.randomUUID());
    }
}
