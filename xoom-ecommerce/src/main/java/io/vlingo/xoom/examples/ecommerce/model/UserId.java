package io.vlingo.xoom.examples.ecommerce.model;


import com.google.common.base.Objects;

public class UserId {

    public final int id;

    public UserId(int id) {
        this.id = id;
    }

    public static UserId Unspecified() {
        return new UserId(-1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserId userId = (UserId) o;
        return id == userId.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public String getId() {
        return Integer.toString(id);
    }
}
