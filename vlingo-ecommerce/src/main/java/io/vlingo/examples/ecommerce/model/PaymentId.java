package io.vlingo.examples.ecommerce.model;

public class PaymentId {

    public final String id;

    public PaymentId(String id) {
        this.id = id;
    }

    public static PaymentId from(String id) {
        return new PaymentId(id);
    }
}
