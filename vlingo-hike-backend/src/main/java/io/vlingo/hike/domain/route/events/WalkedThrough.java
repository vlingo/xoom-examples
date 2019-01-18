package io.vlingo.hike.domain.route.events;

import io.vlingo.lattice.model.DomainEvent;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Value(staticConstructor = "through")
public class WalkedThrough extends DomainEvent  {
    public final LocalDateTime happened;
    public final double x, y, z;
}
