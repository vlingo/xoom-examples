package io.vlingo.examples.ecommerce.model;

import io.vlingo.common.Completes;
import io.vlingo.lattice.model.sourcing.EventSourced;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;


public class OrderEntity extends EventSourced<OrderEntity.State> implements Order {
    @Override
    public void orderItemChange(ProductId productId, int newQuantity) {
        throw new NotImplementedException();
    }

    @Override
    public void changeShipmentAddress(MailingAddress shipmentAddress) {
        throw new NotImplementedException();
    }

    @Override
    public void paymentComplete(PaymentId paymentId, int orderStateHash) {
        throw new NotImplementedException();
    }

    @Override
    public Completes<OrderInfo> query() {
        return null;
    }

    @Override
    protected String streamName() {
        return "orderEvents";
    }

    static class State {
        List<OrderItem> items;
        OrderInfo.OrderStatusEnum status;

        public State(List<OrderItem> items, OrderInfo.OrderStatusEnum status) {
            this.items = items;
            this.status = status;
        }
    }

}
