package io.examples.stock.application;

import io.examples.stock.domain.ItemId;
import io.examples.stock.domain.Location;
import io.examples.stock.domain.Stock;
import io.examples.stock.repository.StockRepository;
import io.vlingo.common.Completes;

import javax.inject.Singleton;
import javax.transaction.Transactional;

/**
 * The {@code StockApplicationServices} exposes operations and business logic that
 * pertains to the {@link Stock} domain model. This service forms an anti-corruption
 * layer that is exposed to consumers using the {@link io.examples.stock.endpoint.v1.StockResource}.
 *
 * @author Danilo Ambrosio
 * @see io.examples.stock.endpoint.StockEndpoint
 */
@Singleton
public class StockApplicationServices {

    private final StockRepository stockRepository;

    public StockApplicationServices(final StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Transactional
    public Completes<Stock> openStock(final OpenStock openStock) {

        final Location location =
                Location.valueOf(openStock.locationName());

        return Completes.withSuccess(stockRepository)
                .andThen(repository -> repository.findByLocation(location))
                .andThen(existingStock -> {
                    if(existingStock.isPresent()) {
                        throw new IllegalStateException("There is already a Stock in " + openStock.locationName());
                    }
                    return stockRepository.save(Stock.openIn(location));
                });
    }

    @Transactional
    public Completes<Stock> loadStock(final AddItems addItems) {
        final ItemId itemId = ItemId.of(addItems.itemId());

        final Location location =
                Location.valueOf(addItems.locationName());

        return Completes.withSuccess(stockRepository)
                .andThen(repository -> repository.findByLocation(location))
                .andThen(optionalRetrievedStock -> {
                    if(!optionalRetrievedStock.isPresent()) {
                        throw new IllegalStateException("There is no Stock in " + location);
                    }

                    final Stock existingStock = optionalRetrievedStock.get();
                    existingStock.increaseAvailabilityFor(itemId, addItems.quantity());
                    return stockRepository.save(existingStock);
                });

    }

    @Transactional
    public void unloadItem(final UnloadItem unloadItem) {
        final ItemId itemId = ItemId.of(unloadItem.itemId());
        final Location location = Location.valueOf(unloadItem.locationName());

        Completes.withSuccess(stockRepository)
                .andThen(repository -> repository.findByLocation(location))
                .andThen(retrievedStock -> {
                    if(!retrievedStock.isPresent()) {
                        throw new IllegalStateException("There is no Stock in " + location);
                    }

                    final Stock existingStock = retrievedStock.get();
                    existingStock.unload(itemId, unloadItem.quantity());
                    return stockRepository.save(existingStock);
                });
    }

    public Completes<Stock> findByLocation(final String locationName) {
        final Location location = Location.valueOf(locationName);
        return Completes.withSuccess(stockRepository)
                .andThen(repository -> repository.findByLocation(location))
                .andThen(stock -> stock.isPresent() ? stock.get() : null);
    }

}
