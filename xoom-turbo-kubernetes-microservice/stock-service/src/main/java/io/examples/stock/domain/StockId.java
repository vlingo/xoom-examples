package io.examples.stock.domain;

public class StockId {

    public final String value;

    private StockId(final String value) {
        this.value = value;
    }

    public static StockId from(final String value) {
        return new StockId(value);
    }
}
