package io.vlingo.examples.ecommerce.model;

import io.vlingo.common.Completes;


// Orders are created when the "CheckoutStarted" event is received, which triggers the order creation
// Changes to the basket will change orders that already been created.

public interface Order {

    void paymentComplete(PaymentId paymentId, int orderStateHash);

    void orderShipped(PaymentId paymentId, int orderStateHash);

    Completes<OrderInfo> query();

    OrderInfo doesSyncOperatorWork();

}
