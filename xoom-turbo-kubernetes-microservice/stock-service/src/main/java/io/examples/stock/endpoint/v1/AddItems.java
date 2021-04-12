package io.examples.stock.endpoint.v1;

public class AddItems {

    private final String locationName;
    private final Long itemId;
    private final Integer quantity;

    public AddItems(final String locationName, final Long itemId, final Integer quantity) {
        this.locationName = locationName;
        this.itemId = itemId;
        this.quantity = quantity;
    }

    public String locationName() {
        return locationName;
    }

    public Long itemId() {
        return itemId;
    }

    public Integer quantity() {
        return quantity;
    }

}
