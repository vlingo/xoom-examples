package io.vlingo.xoom.examples.spaces.model;

import io.vlingo.xoom.lattice.grid.spaces.Key;

public final class Key1 implements Key {
    public final String id;

    public Key1(final String id) {
        this.id = id;
    }

    @Override
    public boolean matches(final Key other) {
        return equals(other);
    }

    @Override
    public int hashCode() {
        return 31 * id.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || other.getClass() != getClass()) {
            return false;
        }
        return id.equals(((Key1) other).id);
    }

    @Override
    public String toString() {
        return "Key1[id=" + id + "]";
    }

    @Override
    public int compare(Key key1, Key key2) {
        final Key1 k1 = (Key1) key1;
        final Key1 k2 = (Key1) key2;

        return k1.id.compareTo(k2.id);
    }
}
