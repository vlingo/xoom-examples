package io.vlingo.examples.ecommerce.model;


import com.google.common.base.Objects;

public class ProductId {

    public final String id;

    public ProductId(String id) {
        this.id = id;
    }

    public static ProductId fromId(String id) {
        return new ProductId(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductId productId = (ProductId) o;
        return id.equals(productId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return id;
    }
}
