package io.examples.account.application;

import io.examples.account.domain.model.AccountQuery;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.retry.annotation.Retryable;

import java.util.concurrent.CompletableFuture;

@Retryable(attempts = "${retry.attempts:3}", delay = "${retry.delay:50ms}")
@Client("account-service")
public interface AccountClient {

    @Get("/v1/accounts/{id}")
    CompletableFuture<AccountQuery> queryAccount(Long id);
}
