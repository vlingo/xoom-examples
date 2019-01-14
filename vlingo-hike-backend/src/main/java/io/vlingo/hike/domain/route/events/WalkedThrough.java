package io.vlingo.hike.domain.route.events;

import io.vlingo.lattice.model.DomainEvent;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value(staticConstructor = "through")
public class WalkedThrough extends DomainEvent  {
    public final double x, y, z;
}
