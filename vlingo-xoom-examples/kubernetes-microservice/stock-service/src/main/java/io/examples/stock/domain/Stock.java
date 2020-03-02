package io.examples.stock.domain;

import io.examples.stock.data.Identity;

import javax.persistence.*;
import java.util.*;

/**
 * {@code Stock} is modeled as an Aggregate, responsible for
 * manage its own state, ensuring consistency and retaining all
 * information for upcoming queries.
 *
 * @author Danilo Ambrosio
 */
@Entity
@Table(name="VLG_STOCK")
public class Stock extends Identity {

    @Enumerated
    @Column(name = "LOCATION")
    private final Location location;

    @ElementCollection(fetch = FetchType.EAGER)
    private final Set<StorageUnit> units = new HashSet<>();

    public Stock() {
        this(null, Collections.emptyList());
    }

    private Stock(final Location location, final List<StorageUnit> units) {
        this.location = location;
        this.units.addAll(units);
    }

    public static Stock openIn(final Location location) {
        return new Stock(location, Collections.emptyList());
    }

    public void unload(final ItemId itemId, final Integer quantity) {
        final Optional<StorageUnit> storageUnit = unitHaving(itemId);
        refreshUnitsWith(storageUnit.get().decreaseAvailability(quantity));
    }

    public void increaseAvailabilityFor(final ItemId itemId, final Integer quantity) {
        final Optional<StorageUnit> storageUnit = unitHaving(itemId);
        if(storageUnit.isPresent()) {
            refreshUnitsWith(storageUnit.get().increaseAvailability(quantity));
        } else {
            units.add(new StorageUnit(itemId, quantity));
        }
    }

    private void refreshUnitsWith(final StorageUnit updatedUnit) {
        this.units.remove(updatedUnit);
        this.units.add(updatedUnit);
    }

    private Optional<StorageUnit> unitHaving(final ItemId itemId) {
        return units.stream().filter(unit -> unit.hasItem(itemId)).findFirst();
    }

    public int quantityFor(final ItemId itemId) {
        return unitHaving(itemId).get().availableQuantity();
    }

    public Location getLocation() {
        return location;
    }

}
