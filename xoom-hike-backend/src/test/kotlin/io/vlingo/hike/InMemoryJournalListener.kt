package io.vlingo.hike

import io.vlingo.actors.testkit.TestUntil
import io.vlingo.symbio.Entry
import io.vlingo.symbio.State
import io.vlingo.symbio.store.journal.JournalListener

import java.util.ArrayList
import java.util.concurrent.CopyOnWriteArrayList

class InMemoryJournalListener : JournalListener<String> {
    val allEntries: MutableList<Entry<String>?> = CopyOnWriteArrayList()
    val allSnapshots: MutableList<State<String>?> = CopyOnWriteArrayList()
    var until = TestUntil.happenings(1)

    override fun appended(entry: Entry<String>?) {
        allEntries.add(entry)
        allSnapshots.add(null)
        until.happened()
    }

    override fun appendedWith(entry: Entry<String>?, snapshot: State<String>?) {
        allEntries.add(entry)
        allSnapshots.add(snapshot)
        until.happened()
    }

    override fun appendedAll(entries: List<Entry<String>>?) {
        allEntries.addAll(entries ?: emptyList())
        val snapshots = ArrayList<State<String>?>(entries?.size ?: 0)
        for (idx in entries?.indices ?: emptyList<Int>()) {
            snapshots.add(null)
        }
        allSnapshots.addAll(snapshots)
        until.happened()
    }

    override fun appendedAllWith(entries: List<Entry<String>>?, snapshot: State<String>?) {
        allEntries.addAll(entries ?: emptyList())
        val snapshots = ArrayList<State<String>?>(entries?.size ?: 0)
        for (idx in 0 until ((entries?.size ?: 0) - 1)) {
            snapshots.add(null)
        }
        snapshots.add(snapshot)
        allSnapshots.addAll(snapshots)
        until.happened()
    }
}