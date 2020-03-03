package io.examples.order.domain;

import io.examples.order.data.Identity;

import javax.persistence.*;

/**
 * {@code Order} is modeled as an Aggregate, responsible for
 * manage its own state, ensuring consistency and retaining all
 * information for upcoming queries.
 *
 * @author Danilo Ambrosio
 */
@Entity
@Table(name = "VLG_ORDER")
public class Order extends Identity {

    @Embedded
    private final ProductId productId;

    @Column(name = "QUANTITY")
    private final Integer quantity;

    @Enumerated
    @Column(name = "SITE")
    private final Site site;

    public static Order from(final ProductId productId,
                             final Integer quantity,
                             final Site site) {
        return new Order(productId, quantity, site);
    }

    private Order(final ProductId productId,
                  final Integer quantity,
                  final Site site) {
        this.productId = productId;
        this.quantity = quantity;
        this.site = site;
    }

    public Order() {
        this(null, null, null);
    }

    public ProductId productId() {
        return productId;
    }

    public Integer quantity() {
        return quantity;
    }

}
