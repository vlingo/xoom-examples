package io.vlingo.examples.processmanager.choreography;


import com.google.common.base.Objects;

public class UserId {

    public final int id;

    public UserId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserId productId = (UserId) o;
        return id == productId.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
