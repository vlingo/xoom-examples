package io.examples.account.endpoint.v1;

import io.examples.account.domain.Account;
import io.examples.account.domain.AccountService;
import io.examples.account.endpoint.AccountEndpoint;
import io.vlingo.xoom.resource.annotations.Resource;
import io.vlingo.common.Completes;
import io.vlingo.http.Response;
import io.vlingo.http.resource.RequestHandler;
import io.vlingo.xoom.resource.Endpoint;

import static io.vlingo.common.Completes.withSuccess;
import static io.vlingo.http.Response.Status.*;
import static io.vlingo.http.resource.ResourceBuilder.*;

/**
 * This {@link AccountResource} exposes a REST API that maps resource HTTP request-response handlers to operations
 * contained in the {@link AccountService}. This {@link Endpoint} implementation forms an anti-corruption layer between
 * consuming services and this microservice's {@link AccountService} API.
 * <p>
 * This resource is a versioned API definition that implements the {@link AccountEndpoint}. To fork versions, create a
 * separate implementation of the {@link AccountEndpoint} in a separate package and change the getRequestHandlers
 * method by incrementing the versioned URI root.
 *
 * @author Kenny Bastani
 * @see AccountEndpoint
 */
@Resource
public class AccountResource implements AccountEndpoint {

    private static String ENDPOINT_VERSION = "1.1";
    private final AccountService accountService;

    public AccountResource(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Get the semantic version of this {@link AccountEndpoint}.
     *
     * @return a {@link String} representing the semantic version of this HTTP {@link Endpoint} definition.
     */
    @Override
    public String getVersion() {
        return AccountResource.ENDPOINT_VERSION;
    }

    /**
     * Get an array of {@link RequestHandler}s that expose HTTP mappings on commands in the {@link AccountService}.
     *
     * @return an array of {@link RequestHandler}s.
     */
    @Override
    public RequestHandler[] getHandlers() {
        return new RequestHandler[]{
                get("/v1/accounts")
                        .handle(this::findAllAccounts)
                        .onError(this::getErrorResponse),
                get("/v1/accounts/{id}")
                        .param(Long.class)
                        .handle(this::findAccountById)
                        .onError(this::getErrorResponse),
                post("/v1/accounts")
                        .body(Account.class)
                        .handle(this::createAccount)
                        .onError(this::getErrorResponse),
                put("/v1/accounts/{id}")
                        .param(Long.class)
                        .body(Account.class)
                        .handle(this::updateAccount)
                        .onError(this::getErrorResponse),
                delete("/v1/accounts/{id}")
                        .param(Long.class)
                        .handle(this::deleteAccount)
                        .onError(this::getErrorResponse)

        };
    }

    @Override
    public Completes<Response> findAllAccounts() {
        return response(Ok, accountService.getAccounts());
    }

    @Override
    public Completes<Response> findAccountById(Long id) {
        return response(Ok, accountService.getAccount(id));
    }

    @Override
    public Completes<Response> createAccount(Account account) {
        return response(Created, accountService.createAccount(account));
    }

    @Override
    public Completes<Response> updateAccount(Long id, Account account) {
        return response(Ok, accountService.updateAccount(id, account));
    }

    @Override
    public Completes<Response> deleteAccount(Long id) {
        return emptyResponse(NoContent, withSuccess(() -> accountService.deleteAccount(id)));
    }
}
