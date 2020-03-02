package io.examples.stock.application;

public class UnloadItem {

    private final long itemId;
    private final String locationName;
    private final Integer quantity;

    public UnloadItem(final String locationName,
                      final long itemId,
                      final Integer quantity) {
        this.locationName = locationName;
        this.itemId = itemId;
        this.quantity = quantity;
    }

    public String locationName() {
        return locationName;
    }

    public long itemId() {
        return itemId;
    }

    public Integer quantity() {
        return quantity;
    }

}
