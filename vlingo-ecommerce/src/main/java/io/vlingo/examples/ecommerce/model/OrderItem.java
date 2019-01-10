package io.vlingo.examples.ecommerce.model;

public class OrderItem {
    public final ProductId productId;
    public final int quantity;

    public OrderItem(ProductId productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}
