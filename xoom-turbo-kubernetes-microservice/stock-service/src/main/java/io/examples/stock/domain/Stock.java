package io.examples.stock.domain;

import io.examples.infrastructure.StockQueryProvider;
import io.vlingo.actors.Address;
import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;

import java.util.UUID;

public interface Stock {

    static String generateName() {
        return "O:" + UUID.randomUUID().toString();
    }

    Completes<StockState> openIn(final Location location);

    Completes<StockState> increaseAvailabilityFor(final ItemId itemId, final Integer quantity);

    Completes<StockState> unload(final ItemId itemId, final Integer quantity);

    static Completes<StockState> openIn(final Stage stage, final Location location) {
        final Address address =
                stage.addressFactory().uniqueWith(generateName());

        final StockId stockId = StockId.from(address.idString());

        final Stock stock =
                stage.actorFor(Stock.class,
                        Definition.has(StockEntity.class, Definition.parameters(stockId)), address);

        return stock.openIn(location);
    }

    static Completes<StockState> increaseAvailabilityFor(final Stage stage, final Location location, final ItemId itemId, final Integer quantity) {
        return StockQueryProvider.instance()
                .queries().queryByLocation(location)
                .andThenTo(state -> {
                    final Address address = stage.addressFactory().from(state.stockId().value);
                    return stage.actorOf(Stock.class, address);
                })
                .andThenTo(stock -> stock.increaseAvailabilityFor(itemId, quantity));
    }

    static Completes<StockState> unload(final Stage stage, final Location location, final ItemId itemId, final Integer quantity) {
        return StockQueryProvider.instance()
                .queries().queryByLocation(location)
                .andThenTo(state -> {
                    final Address address = stage.addressFactory().from(state.stockId().value);
                    return stage.actorOf(Stock.class, address);
                })
                .andThenTo(stock -> stock.unload(itemId, quantity));
    }

}
