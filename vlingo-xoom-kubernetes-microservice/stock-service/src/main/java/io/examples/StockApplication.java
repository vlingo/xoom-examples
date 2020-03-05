package io.examples;

import io.examples.stock.domain.Stock;
import io.micronaut.runtime.Micronaut;

/**
 * The {@code StockApplication} is a microservice that implements features in the {@link Stock} context.
 */
public class StockApplication {

    private static final String DATABASE_URL = "STOCK_SERVICE_DATABASE_URL";
    private static final String URI_ENVIRONMENT_VARIABLE = "MESSAGING_URI";

    public static void main(String[] args) {
        Micronaut.build(args)
                .environmentVariableIncludes(DATABASE_URL)
                .environmentVariableIncludes(URI_ENVIRONMENT_VARIABLE)
                .run(StockApplication.class);
    }

}
