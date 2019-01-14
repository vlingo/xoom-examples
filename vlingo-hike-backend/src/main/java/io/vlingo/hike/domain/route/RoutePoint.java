package io.vlingo.hike.domain.route;

import lombok.Value;

@Value(staticConstructor = "of")
public class RoutePoint {
    public final double x, y, z;
}
