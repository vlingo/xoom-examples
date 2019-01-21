package io.vlingo.hike.infrastructure.actors

import io.vlingo.lattice.model.DomainEvent
import io.vlingo.lattice.model.sourcing.EventSourced
import java.util.function.BiConsumer

inline fun <reified T: EventSourced<String>, reified E: DomainEvent> eventConsumer(
        noinline consumer: (actor: T, event: E) -> Unit
) {
    EventSourced.registerConsumer(T::class.java, E::class.java, BiConsumer<T, E> { t, u -> consumer(t, u) })
}