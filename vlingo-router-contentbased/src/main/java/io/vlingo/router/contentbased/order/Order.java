package io.vlingo.router.contentbased.order;

import java.util.Map;

/**
 * @author Chandrabhan Kumhar
 * Stores details of an order
 */
public class Order {

    private String id;
    private String type;
    private Double grandTotal;
    private Map<String, OrderItem> orderItems;

    public Order(String id, String type, Map<String, OrderItem> orderItemList) {
        this.id = id;
        this.type = type;
        this.orderItems = orderItemList;
        this.grandTotal = orderItemList.entrySet().stream().mapToDouble(value -> value.getValue().getPrice()).sum();
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Double getGrandTotal() {
        return grandTotal;
    }

    public Map<String, OrderItem> getOrderItems() {
        return orderItems;
    }

    @Override
    public String toString() {
        return "{" + String.join(", ", this.id, this.type, this.grandTotal.toString(), orderItems.values().toString()) + "}";
    }
}
