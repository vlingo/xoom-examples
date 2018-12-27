package io.vlingo.examples.processmanager.choreography;


import com.google.common.base.Objects;

public class ProductId {

    public final int id;

    public ProductId(int id) {
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
        return Objects.hashCode(id);
    }
}
