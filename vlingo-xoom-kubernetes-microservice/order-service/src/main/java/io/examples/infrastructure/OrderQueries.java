package io.examples.infrastructure;

import io.examples.order.domain.OrderState;
import io.vlingo.common.Completes;

import java.util.Set;

public interface OrderQueries {

    Completes<Set<OrderState>> allOrders();
    
}
