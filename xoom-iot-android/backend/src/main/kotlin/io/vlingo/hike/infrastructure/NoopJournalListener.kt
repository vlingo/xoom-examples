package io.vlingo.hike.infrastructure

import io.vlingo.symbio.Entry
import io.vlingo.symbio.State
import io.vlingo.symbio.store.journal.JournalListener

object NoopJournalListener : JournalListener<String> {
    override fun appendedAll(entries: MutableList<Entry<String>>?) {
    }

    override fun appendedAllWith(entries: MutableList<Entry<String>>?, snapshot: State<String>?) {
    }

    override fun appended(entry: Entry<String>?) {
    }

    override fun appendedWith(entry: Entry<String>?, snapshot: State<String>?) {
    }
}