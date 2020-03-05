package io.examples.order.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class ProductId {

    @Column(name = "PRODUCT_ID")
    private final long id;

    @SuppressWarnings("unused")
    public ProductId() {
        this(0l);
    }

    public static ProductId of(final long id) {
        return new ProductId(id);
    }

    private ProductId(final long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductId productId = (ProductId) o;
        return id == productId.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
