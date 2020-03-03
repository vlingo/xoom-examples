package io.examples.order.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@code OrderTest} performs tests over {@link Order}
 * to ensure data consistency and state management.
 *
 * @author Danilo Ambrosio
 */
public class OrderTest {

    @Test
    public void testOrderInformationAccess() {
        final Order order = Order.from(ProductId.of(1l), 40, Site.LA);
        Assertions.assertEquals(ProductId.of(1l), order.productId());
        Assertions.assertEquals(40, order.quantity());
    }

}
