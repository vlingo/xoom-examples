package io.examples.stock.domain;

import io.vlingo.symbio.store.object.StateObject;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class StockState extends StateObject {

    private final StockId stockId;
    private Location location;
    private Set<StorageUnit> units = new HashSet<>();

    public StockState(final StockId stockId) {
        this.stockId = stockId;
    }

    public static StockState from(final StockId stockId) {
        return new StockState(stockId);
    }

    public StockState openIn(final Location location) {
        this.location = location;
        return this;
    }

    public StockState unload(final ItemId itemId, final Integer quantity) {
        final Optional<StorageUnit> storageUnit = unitHaving(itemId);
        refreshUnitsWith(storageUnit.get().decreaseAvailability(quantity));
        return this;
    }

    public StockState increaseAvailabilityFor(final ItemId itemId, final Integer quantity) {
        final Optional<StorageUnit> storageUnit = unitHaving(itemId);
        if(storageUnit.isPresent()) {
            refreshUnitsWith(storageUnit.get().increaseAvailability(quantity));
        } else {
            units.add(new StorageUnit(itemId, quantity));
        }
        return this;
    }

    private void refreshUnitsWith(final StorageUnit updatedUnit) {
        this.units.remove(updatedUnit);
        this.units.add(updatedUnit);
    }

    private Optional<StorageUnit> unitHaving(final ItemId itemId) {
        return units.stream().filter(unit -> unit.hasItem(itemId)).findFirst();
    }

    public Integer quantityFor(final ItemId itemId) {
        return unitHaving(itemId).get().availableQuantity();
    }

    public boolean locatedIn(final Location location) {
        return this.location.equals(location);
    }

    public StockId stockId() {
        return stockId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        StockState that = (StockState) o;
        return Objects.equals(stockId, that.stockId) &&
                location == that.location;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), stockId, location);
    }
}
