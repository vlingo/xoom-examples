package io.vlingo.hike.domain.hiker.events

import io.vlingo.lattice.model.DomainEvent
import java.time.LocalDateTime

data class RouteStarted(val date: LocalDateTime): DomainEvent()