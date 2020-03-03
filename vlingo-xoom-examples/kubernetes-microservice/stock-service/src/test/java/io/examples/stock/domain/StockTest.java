package io.examples.stock.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@code StockTest} performs tests over {@link Stock}
 * to ensure data consistency and state management.
 *
 * @author Danilo Ambrosio
 */
public class StockTest {

    @Test
    public void testAvailableQuantityManipulation() {
        final Stock stock = Stock.openIn(Location.LA);
        stock.increaseAvailabilityFor(ItemId.of(1l), 500);
        stock.increaseAvailabilityFor(ItemId.of(1l), 100);
        stock.unload(ItemId.of(1l), 200);
        Assertions.assertEquals(400, stock.quantityFor(ItemId.of(1l)));
    }

}
