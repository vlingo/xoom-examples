package io.examples.account.domain;

import io.examples.account.endpoint.AccountEndpoint;
import io.examples.account.endpoint.v1.AccountResource;
import io.examples.account.flow.FlowContext;
import io.examples.account.repository.AccountRepository;
import io.reactivex.Observable;
import io.vlingo.common.Completes;

import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.function.Consumer;

/**
 * The {@link AccountService} exposes operations and business logic that pertains to the {@link Account} entity and
 * aggregate root. This service forms an anti-corruption layer that is exposed to consumers using the
 * {@link AccountResource}.
 *
 * @author Kenny Bastani
 * @see AccountEndpoint
 */
@Singleton
public class AccountService {

    private final AccountRepository accountRepository;
    private final FlowContext context;

    public AccountService(AccountRepository accountRepository, FlowContext flowContext) {
        this.accountRepository = accountRepository;
        this.context = flowContext;
    }

    /**
     * Get all {@link Account}s from the {@link AccountRepository}.
     *
     * @return a list of {@link Account} entities.
     */
    public Completes<List<Account>> getAccounts() {
        return Completes.withSuccess(accountRepository.findAll())
                .andThen(accounts -> Observable.fromIterable(accounts).toList().blockingGet());
    }

    /**
     * Lookup an {@link Account} by its unique identifier.
     *
     * @param id is the unique identifier of the {@link Account}.
     * @return the {@link Account} or throw a {@link RuntimeException} if one does not exist.
     */
    public Completes<Account> getAccount(Long id) {
        return accountRepository.findById(id).map(Completes::withSuccess).orElseThrow(() ->
                new RuntimeException("Account with id[" + id + "] does not exist"));
    }

    /**
     * Register a new {@link Account}.
     *
     * @param account is the {@link Account} to create.
     * @return the created {@link Account}.
     */
    public Completes<Account> create(Account account) {
        account = accountRepository.save(account);
        execute(account.getId(), result -> result.create(context.getProcessor()));
        return getAccount(account.getId());
    }

    /**
     * Updates the {@link Account} with the corresponding unique identifier.
     *
     * @param id      is the unique identifier for the {@link Account}.
     * @param model is the {@link Account} entity containing the fields to update.
     * @return the updated {@link Account}.
     */
    public Completes<Account> update(@NotNull Long id, @NotNull Account model) {
        return execute(id, account -> account.update(context.getProcessor(), model))
                .andThenConsume(account ->
                        accountRepository.update(id, account.getAccountNumber()));
    }

    /**
     * Delete the {@link Account} with the corresponding unique identifier.
     *
     * @param id is the unique identifier of the {@link Account} that is to be deleted.
     */
    public void delete(Long id) {
        accountRepository.deleteById(id);
    }

    public Completes<Account> confirm(@NotNull Long id) {
        return execute(id, account -> account.confirm(context.getProcessor()));
    }

    public Completes<Account> activate(@NotNull Long id) {
        return execute(id, account -> account.activate(context.getProcessor()));
    }

    public Completes<Account> suspend(@NotNull Long id) {
        return execute(id, account -> account.suspend(context.getProcessor()));
    }

    public Completes<Account> archive(@NotNull Long id) {
        return execute(id, account -> account.archive(context.getProcessor()));
    }

    private Completes<Account> execute(@NotNull Long id, Consumer<Account> commandHandler) {
        return getAccount(id)
                .andThenConsume(commandHandler)
                .andThenConsume(account -> accountRepository
                        .update(id, account.getAccountStatus(), account.getVersion()));
    }
}
