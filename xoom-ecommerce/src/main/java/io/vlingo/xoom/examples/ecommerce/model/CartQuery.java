package io.vlingo.xoom.examples.ecommerce.model;

import io.vlingo.xoom.common.Completes;

public interface CartQuery {

    Completes<CartUserSummaryData> getCartSummaryForUser(UserId userId);
}
