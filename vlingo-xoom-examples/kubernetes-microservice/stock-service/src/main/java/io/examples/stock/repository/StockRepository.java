package io.examples.stock.repository;

import io.examples.stock.domain.Location;
import io.examples.stock.domain.Stock;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

/**
 * {@code StockRepository} is capable to persist and retrieve
 * {@link Stock} data.
 *
 * @author Danilo Ambrosio
 */
@Repository
public interface StockRepository extends CrudRepository<Stock, Long> {

    Optional<Stock> findByLocation(final Location location);
}
