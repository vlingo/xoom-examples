package io.vlingo.hike.infrastructure

import io.vlingo.actors.Actor
import io.vlingo.actors.Address
import io.vlingo.actors.Definition
import io.vlingo.actors.World
import io.vlingo.common.Completes

fun World.addressOfString(string: String): Address {
    return addressFactory().from(string.hashCode().toString())
}

inline fun <reified T, reified K : Actor> World.actor(args: Array<Any>, address: Address): Completes<T> {
    return stage().actorOf(T::class.java, address)
        .andThen {
            it ?: stage().actorFor<T>(
                T::class.java,
                Definition.has(K::class.java, Definition.parameters(*args)),
                address
            )
        }.otherwise {
            it ?: stage().actorFor<T>(
                T::class.java,
                Definition.has(K::class.java, Definition.parameters(*args)),
                address
            )
        }.recoverFrom {
            defaultLogger().log("Exception when recovering actor at $address, with $args", it)

            stage().actorFor<T>(
                T::class.java,
                Definition.has(K::class.java, Definition.parameters(*args)),
                address
            )
        }
}