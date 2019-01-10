package io.vlingo.examples.ecommerce.model;

import java.util.ArrayList;
import java.util.List;

public class OrderInfo {

    public final String id;
    public final List<OrderItem> orderItems;

    public OrderInfo(String id, List<OrderItem> orderItems, OrderStatusEnum orderState) {
        this.id = id;
        this.orderState = orderState;
        this.orderItems = new ArrayList<>(orderItems);
    }

    public final OrderStatusEnum orderState;

    public enum OrderStatusEnum {
        notPaid, paid, shipped
    }

}
