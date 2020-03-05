package io.examples;

import io.examples.order.domain.Order;
import io.micronaut.runtime.Micronaut;

/**
 * The {@code OrderApplication} is a microservice that implements features in the {@link Order} context.
 */
public class OrderApplication {

    private static final String MESSAGING_URI = "URI_ENVIRONMENT_VARIABLE";
    private static final String DATABASE_URL = "ORDER_SERVICE_DATABASE_URL";

    public static void main(String[] args) {
        Micronaut.build(args)
                .environmentVariableIncludes(DATABASE_URL)
                .environmentVariableIncludes(MESSAGING_URI)
                .run(OrderApplication.class);
    }

}
