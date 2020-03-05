package io.examples.account.application;

import io.examples.account.domain.model.AccountQuery;
import io.vlingo.actors.Stoppable;
import io.vlingo.common.Completes;

import java.util.concurrent.CompletableFuture;

public interface Account extends Stoppable {
    Completes<CompletableFuture<AccountQuery>> query(Long id);
}
