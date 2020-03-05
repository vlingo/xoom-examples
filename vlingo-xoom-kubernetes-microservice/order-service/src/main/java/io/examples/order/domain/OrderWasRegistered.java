package io.examples.order.domain;

/**
 * {@code OrderWasRegistered} is a {@link DomainEvent} that
 * happens every time a valid {@link Order} is registered.
 *
 * @author Danilo Ambrosio
 */
public class OrderWasRegistered implements DomainEvent {

    private final ProductId productId;
    private final Integer quantity;
    private final Site site;

    public OrderWasRegistered(final ProductId productId,
                              final Integer quantity,
                              final Site site) {
        this.productId = productId;
        this.quantity = quantity;
        this.site = site;
    }

    public ProductId productId() {
        return productId;
    }

    public Integer quantity() {
        return quantity;
    }

}
