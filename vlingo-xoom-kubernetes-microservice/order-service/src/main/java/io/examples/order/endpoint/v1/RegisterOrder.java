package io.examples.order.endpoint.v1;

public class RegisterOrder {

    private final Long productId;
    private final Integer quantity;
    private final String siteName;

    public RegisterOrder(final Long productId, final Integer quantity, final String siteName) {
        this.productId = productId;
        this.quantity = quantity;
        this.siteName = siteName;
    }

    public Long productId() {
        return productId;
    }

    public Integer quantity() {
        return quantity;
    }

    public String siteName() { return siteName; }
}
