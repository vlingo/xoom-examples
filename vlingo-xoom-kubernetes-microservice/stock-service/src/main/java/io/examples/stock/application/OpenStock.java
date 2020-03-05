package io.examples.stock.application;

public class OpenStock {

    private final String locationName;

    public OpenStock(final String locationName) {
        this.locationName = locationName;
    }

    public String locationName() {
        return locationName;
    }
}
