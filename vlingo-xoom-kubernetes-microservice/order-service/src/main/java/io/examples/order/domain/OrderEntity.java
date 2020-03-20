package io.examples.order.domain;

import io.vlingo.common.Completes;
import io.vlingo.lattice.model.object.ObjectEntity;

/**
 * {@code Order} ...
 *
 * @author Danilo Ambrosio
 */
public class OrderEntity extends ObjectEntity<OrderState> implements Order {

    private OrderState state;

    public OrderEntity(final OrderId orderId) {
        super(orderId.value);
        state = OrderState.from(orderId);
    }

    @Override
    public Completes<OrderState> register(final ProductId productId,
                                          final Integer quantity,
                                          final Site site) {
        return apply(state.register(productId, quantity, site), () -> state);
    }

    @Override
    protected OrderState stateObject() {
        return state;
    }

    @Override
    protected void stateObject(final OrderState orderState) {
        this.state = orderState;
    }

    @Override
    protected Class<OrderState> stateObjectType() {
        return OrderState.class;
    }
}
