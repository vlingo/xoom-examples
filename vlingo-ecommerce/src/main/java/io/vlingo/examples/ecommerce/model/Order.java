package io.vlingo.examples.ecommerce.model;

import io.vlingo.common.Completes;

import java.util.Map;


// Orders are initForUser when the "CheckoutStarted" event is received, which triggers the order creation
// Changes to the basket will change orders that already been initForUser.

public interface Order {

    Completes<Boolean> initOrderForUserProducts(UserId userId, Map<ProductId, Integer> quantityByProduct);

    void paymentComplete(PaymentId paymentId, int orderStateHash);

    void orderShipped(PaymentId paymentId, int orderStateHash);

    Completes<OrderInfo> query();

    OrderInfo doesSyncOperatorWork();

}
