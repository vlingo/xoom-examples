package io.vlingo.hike.infrastructure

import io.vlingo.common.serialization.JsonSerialization
import io.vlingo.hike.domain.hiker.HikerActor
import io.vlingo.hike.domain.hiker.events.RouteStarted
import io.vlingo.lattice.model.DomainEvent
import io.vlingo.lattice.model.sourcing.EventSourced
import io.vlingo.lattice.model.sourcing.Sourced
import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry
import io.vlingo.symbio.Entry
import io.vlingo.symbio.EntryAdapter
import io.vlingo.symbio.Metadata
import io.vlingo.symbio.Source
import io.vlingo.symbio.store.journal.Journal

inline fun <reified T : Source<*>> eventAdapter(): EntryAdapter<T, Entry.TextEntry> {
    return object : EntryAdapter<T, Entry.TextEntry> {
        override fun fromEntry(entry: Entry.TextEntry?): T = JsonSerialization.deserialized(entry!!.entryData, T::class.java)

        override fun toEntry(source: T): Entry.TextEntry {
            val serialization = JsonSerialization.serialized(source)
            return Entry.TextEntry(T::class.java, 1, serialization, Metadata.nullMetadata())
        }
    }
}

inline fun <reified A : EventSourced<String>> SourcedTypeRegistry.register(journal: Journal<String>): SourcedTypeRegistry.Info<String> {
    this.register(
            SourcedTypeRegistry.Info<String>(
                    journal, A::class.java as Class<Sourced<String, *>>, A::class.java.simpleName
            )
    )

    return this.info(A::class.java) as SourcedTypeRegistry.Info<String>
}

inline fun <reified E : DomainEvent> SourcedTypeRegistry.Info<String>.register() {
    this.register(E::class.java, eventAdapter(), journal::registerAdapter)
}

fun registerSources(registry: SourcedTypeRegistry, journal: Journal<String>) {
    registry.register<HikerActor>(journal)
            .register<RouteStarted>()
}