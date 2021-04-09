package io.vlingo.xoom.examples.ecommerce.model;

import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.lattice.model.sourcing.EventSourced;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static io.vlingo.xoom.examples.ecommerce.model.OrderInfo.*;


public class OrderActor extends EventSourced implements Order {

    static {
        BiConsumer<OrderActor, OrderEvents.Created> created = OrderActor::applyCreated;
        EventSourced.registerConsumer(OrderActor.class, OrderEvents.Created.class, created);

        BiConsumer<OrderActor, OrderEvents.PaymentReceived> paymentReceived = OrderActor::applyPaymentReceived;
        EventSourced.registerConsumer(OrderActor.class, OrderEvents.PaymentReceived.class, paymentReceived);

        BiConsumer<OrderActor, OrderEvents.OrderShipped> orderShipped = OrderActor::applyShipment;
        EventSourced.registerConsumer(OrderActor.class, OrderEvents.OrderShipped.class, orderShipped);

    }

    private State state;

    public OrderActor(
            String orderId) {
        super(orderId);
        this.state =  State.init(orderId);
    }

    private void applyShipment(OrderEvents.OrderShipped orderShipped) {
        this.state = state.orderStatusUpdate(OrderStatusEnum.shipped);
    }

    private void applyPaymentReceived(OrderEvents.PaymentReceived paymentReceived) {
        this.state = state.orderStatusUpdate(OrderStatusEnum.paid);
    }

    private void applyCreated(OrderEvents.Created created) {
        state = state.createForUserOrderItems(created.userId, created.quantityByProductId);
    }

    @Override
    public void initOrderForUserProducts(UserId userId,
                                                       Map<ProductId, Integer> quantityByProduct) {
        apply(OrderEvents.Created.with(state.orderId, userId,  quantityByProduct));
    }

    @Override
    public void paymentComplete(PaymentId paymentId) {
        if (state.status != OrderStatusEnum.notPaid) {
            throw new IllegalStateException("Payment unexpected, already paid for.");
        }
        apply(OrderEvents.PaymentReceived.with(state.orderId, paymentId, OrderStatusEnum.paid));
    }

    @Override
    public void orderShipped(PaymentId paymentId, int orderStateHash) {
        if (state.status != OrderStatusEnum.notPaid) {
            throw new IllegalStateException("Payment unexpected, already paid for.");
        }
        apply(OrderEvents.OrderShipped.with(state.orderId, OrderStatusEnum.paid));
    }

    @Override
    public Completes<OrderInfo> query() {
        return completes().with(state.createOrderInfo());
    }

    static class State {
        final String                    orderId;
        final UserId                    userId;
        final Map<ProductId, OrderItem> itemsByProductId;
        final OrderStatusEnum status;

        private State(
                String orderId,
                UserId userId,
                Map<ProductId, OrderItem> itemsByProductId,
                OrderStatusEnum status
        ) {
            this.orderId = orderId;
            this.userId = userId;
            this.itemsByProductId = Collections.unmodifiableMap(new HashMap<>(itemsByProductId));
            this.status = status;
        }

        static State init(String orderId) {
            return new State(orderId, UserId.Unspecified(), new HashMap<>(), OrderStatusEnum.notPaid);
        }

        State createForUserOrderItems(
                UserId userId,
                Map<ProductId, Integer> itemsByProductId
        ) {
            Map<ProductId, OrderItem> orderItemsByProductId = itemsByProductId.entrySet().stream()
                    .map((entry) -> new OrderItem(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toMap(OrderItem::getProductId, orderItem -> orderItem));

            return new State(orderId, userId, orderItemsByProductId, OrderStatusEnum.notPaid);
        }

        State orderStatusUpdate(OrderStatusEnum newState) {
            return new State(orderId, userId, itemsByProductId, newState);
        }

        OrderInfo createOrderInfo() {
            return new OrderInfo(
                    orderId,
                    itemsByProductId.entrySet().stream()
                            .map(Map.Entry::getValue).collect(Collectors.toList()),
                    status);
        }
    }

}
