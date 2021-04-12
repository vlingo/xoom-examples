package io.examples.stock.endpoint.v1;

public class OpenStock {

    private final String locationName;

    public OpenStock(final String locationName) {
        this.locationName = locationName;
    }

    public String locationName() {
        return locationName;
    }
}
