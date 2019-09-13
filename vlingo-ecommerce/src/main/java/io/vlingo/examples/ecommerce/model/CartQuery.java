package io.vlingo.examples.ecommerce.model;

import io.vlingo.common.Completes;

public interface CartQuery {

    Completes<CartUserSummaryData> getCartSummaryForUser(int userId);
}
