package io.vlingo.xoom.examples.ecommerce.model;

public class OrderItem {
    public final ProductId productId;
    public final int quantity;

    public ProductId getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public OrderItem(ProductId productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}
