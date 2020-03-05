package io.examples.account.endpoint;

import io.examples.account.domain.Account;
import io.vlingo.xoom.resource.annotations.Resource;
import io.vlingo.common.Completes;
import io.vlingo.http.Response;
import io.vlingo.http.resource.RequestHandler;
import io.vlingo.xoom.resource.Endpoint;

/**
 * The {@link AccountEndpoint} describes a base REST API contract that is used to evolve versions of your API without
 * breaking consumers.
 * <p>
 * By implementing this interface and marking it with the {@link Resource} annotation, you can
 * override this base endpoint definition with your versioned changes. By overriding the {@link RequestHandler[]} in
 * Endpoint.getRequestHandlers(), you can serve different versions of your REST API.
 *
 * @author Kenny Bastani
 * @see io.examples.account.endpoint.v1.AccountResource
 */
public interface AccountEndpoint extends Endpoint {
    String ENDPOINT_NAME = "Account";

    /**
     * Get the full name and version of this {@link Endpoint}.
     *
     * @return a {@link String} representing the full name and semantic version of this {@link Endpoint} definition.
     */
    @Override
    default String getName() {
        return ENDPOINT_NAME;
    }

    /**
     * Find all {@link Account} entities.
     *
     * @return a {@link Completes<Response>} with the JSON result.
     */
    Completes<Response> findAllAccounts();

    /**
     * Find an {@link Account} entity by its unique identifier.
     *
     * @param id is the identifier of the {@link Account}
     * @return a {@link Completes<Response>} with the JSON result.
     */
    Completes<Response> findAccountById(Long id);

    /**
     * Create a new {@link Account} entity.
     *
     * @param account is the {@link Account} to create.
     * @return a {@link Completes<Response>} with the JSON result.
     */
    Completes<Response> createAccount(Account account);

    /**
     * Update the {@link Account} with the unique identifier.
     *
     * @param id      is the unique identifier of the {@link Account}.
     * @param account is the {@link Account} entity model with the fields that will be updated.
     * @return a {@link Completes<Response>} with the JSON result.
     */
    Completes<Response> updateAccount(Long id, Account account);

    /**
     * Delete an {@link Account} with its unique identifier.
     *
     * @param id is the unique identifier of the {@link Account}.
     * @return a {@link Completes<Response>} with the JSON result.
     */
    Completes<Response> deleteAccount(Long id);

    Completes<Response> confirmAccount(Long id);

    Completes<Response> activateAccount(Long id);

    Completes<Response> archiveAccount(Long id);

    Completes<Response> suspendAccount(Long id);
}
