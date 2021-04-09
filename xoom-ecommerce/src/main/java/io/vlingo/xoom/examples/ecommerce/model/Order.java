package io.vlingo.xoom.examples.ecommerce.model;

import io.vlingo.xoom.common.Completes;

import java.util.Map;


public interface Order {

    void initOrderForUserProducts(UserId userId, Map<ProductId, Integer> quantityByProduct);

    void paymentComplete(PaymentId paymentId);

    void orderShipped(PaymentId paymentId, int orderStateHash);

    Completes<OrderInfo> query();
}
