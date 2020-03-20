package io.examples.order.domain;

import io.vlingo.symbio.store.object.StateObject;

import java.util.Objects;

public class OrderState extends StateObject {

    private final OrderId id;
    private ProductId productId;
    private Integer quantity;
    private Site site;

    private OrderState(final OrderId id) {
        this.id = id;
    }

    private OrderState(final OrderId id,
                       final ProductId productId,
                       final Integer quantity,
                       final Site site) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.site = site;
    }

    public static OrderState from(final OrderId id) {
        return new OrderState(id);
    }

    public OrderState register(final ProductId productId, final Integer quantity, final Site site) {
        return new OrderState(id, productId, quantity, site);
    }

    public ProductId productId() {
        return productId;
    }

    public Integer quantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        OrderState that = (OrderState) o;
        return id.equals(that.id) &&
                productId.equals(that.productId) &&
                quantity.equals(that.quantity) &&
                site == that.site;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, productId, quantity, site);
    }
}
