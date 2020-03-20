package io.examples.order.domain;

import java.util.Objects;

public class ProductId {

    private final long id;

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
