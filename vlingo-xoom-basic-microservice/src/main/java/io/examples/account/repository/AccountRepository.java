package io.examples.account.repository;

import io.examples.account.domain.Account;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

/**
 * The {@link AccountRepository} provides a persistence context for managing data with Hibernate/JPA and
 * Micronaut data repositories.
 *
 * @author Kenny Bastani
 */
@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

    /**
     * Updates the {@link Account}'s fields.
     *
     * @param id            is the unique identifier for the {@link Account}.
     * @param accountNumber is the account number.
     */
    void update(@Id Long id, String accountNumber);
}
