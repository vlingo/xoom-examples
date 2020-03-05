package io.examples.account.application;

import io.examples.account.domain.model.AccountQuery;
import io.vlingo.actors.Actor;
import io.vlingo.common.Completes;

import java.util.concurrent.CompletableFuture;

public class AccountActor extends Actor implements Account {

    private final AccountClient accountClient;

    public AccountActor(AccountClient accountClient) {
        super();
        this.accountClient = accountClient;
    }

    @Override
    public Completes<CompletableFuture<AccountQuery>> query(Long id) {
        return completes().with(accountClient.queryAccount(id));
    }
}
