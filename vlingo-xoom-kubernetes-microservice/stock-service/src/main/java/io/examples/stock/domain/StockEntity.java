package io.examples.stock.domain;

import io.vlingo.common.Completes;
import io.vlingo.lattice.model.object.ObjectEntity;

/**
 * {@code Stock} ...
 *
 * @author Danilo Ambrosio
 */
public class StockEntity extends ObjectEntity<StockState> implements Stock  {

    private StockState state;

    public StockEntity(final StockId stockId) {
        super(stockId.value);
        state = StockState.from(stockId);
    }

    @Override
    public Completes<StockState> openIn(final Location location) {
        return apply(state.openIn(location), () -> state);
    }

    @Override
    public Completes<StockState> unload(final ItemId itemId, final Integer quantity) {
        return apply(state.unload(itemId, quantity), () -> state);
    }

    @Override
    public Completes<StockState> increaseAvailabilityFor(final ItemId itemId, final Integer quantity) {
        return apply(state.increaseAvailabilityFor(itemId, quantity), () -> state);
    }

    @Override
    protected StockState stateObject() {
        return state;
    }

    @Override
    protected void stateObject(final StockState stockState) {
        this.state = stockState;
    }

    @Override
    protected Class<StockState> stateObjectType() {
        return StockState.class;
    }
}
