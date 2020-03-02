package io.examples.order.infra.journal;

import io.vlingo.symbio.store.journal.Journal;
import io.vlingo.xoom.data.JdbcJournalProvider;

import javax.inject.Singleton;

@Singleton
public class OrganizationJournalService {

    private final JdbcJournalProvider journalProvider;
    private final Journal<String> organizationJournal;

    public OrganizationJournalService(JdbcJournalProvider journalProvider) {
        this.journalProvider = journalProvider;
        this.organizationJournal = journalProvider.getJournal();
    }

    public JdbcJournalProvider getJournalProvider() {
        return journalProvider;
    }

    public Journal<String> getOrganizationJournal() {
        return organizationJournal;
    }
}
