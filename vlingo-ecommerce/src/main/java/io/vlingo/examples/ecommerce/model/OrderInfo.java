package io.vlingo.examples.ecommerce.model;

import java.util.ArrayList;
import java.util.List;

public class OrderInfo {

    public final String orderId;
    public final List<OrderItem> orderItems;

    public OrderInfo(String orderId, List<OrderItem> orderItems, OrderStatusEnum orderState) {
        this.orderId = orderId;
        this.orderState = orderState;
        this.orderItems = new ArrayList<>(orderItems);
    }

    public final OrderStatusEnum orderState;

    public static OrderInfo empty(String orderId) {
        return new OrderInfo(orderId, new ArrayList<>(), OrderStatusEnum.notPaid);
    }

    public enum OrderStatusEnum {
        notPaid, paid, shipped
    }

}
