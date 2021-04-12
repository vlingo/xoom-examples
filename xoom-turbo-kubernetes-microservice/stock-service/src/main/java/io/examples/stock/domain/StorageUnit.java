package io.examples.stock.domain;

import java.util.Objects;

public class StorageUnit {

    private final long itemId;

    private final Integer availableQuantity;

    public StorageUnit() {
        this(ItemId.of(0l), null);
    }

    public StorageUnit(final ItemId itemId, final Integer availableQuantity) {
        this.itemId = itemId.id();
        this.availableQuantity = availableQuantity;
    }

    public ItemId itemId() {
        return ItemId.of(itemId);
    }

    public boolean hasItem(final ItemId itemId) {
        return this.itemId().equals(itemId);
    }

    public StorageUnit decreaseAvailability(final Integer quantity) {
        return new StorageUnit(itemId(), availableQuantity - quantity);
    }

    public StorageUnit increaseAvailability(final Integer quantity) {
        return new StorageUnit(itemId(), availableQuantity + quantity);
    }

    public Integer availableQuantity() {
        return availableQuantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final StorageUnit that = (StorageUnit) o;
        return this.itemId().equals(that.itemId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId());
    }

    @Override
    public String toString() {
        return "StorageUnit{" +
                "itemId=" + itemId +
                ", availableQuantity=" + availableQuantity +
                '}';
    }
}
