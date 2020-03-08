package io.vlingo.examples.ecommerce.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import io.vlingo.lattice.model.IdentifiedDomainEvent;

public interface OrderEvents {

    class Created extends IdentifiedDomainEvent  {

        public final String orderId;
        public final UserId userId;
        public final Map<ProductId, Integer> quantityByProductId;

        public Created(String orderId, UserId userId, Map<ProductId, Integer> quantityByProductId ) {
            this.orderId = orderId;
            this.userId = userId;
            this.quantityByProductId = Collections.unmodifiableMap(new HashMap<>(quantityByProductId));;
        }

        static Created with(String orderId, UserId userId, Map<ProductId, Integer> productQuanityById) {
            return new Created(orderId, userId, productQuanityById);
        }

		@Override
		public String identity() {
			return orderId;
		}
    }

    class PaymentReceived extends IdentifiedDomainEvent  {

        public final String orderId;
        public final PaymentId paymentId;
        public final OrderInfo.OrderStatusEnum newState;

        public PaymentReceived(String orderId, PaymentId paymentId, OrderInfo.OrderStatusEnum newState) {
            this.orderId = orderId;
            this.paymentId = paymentId;
            this.newState = newState;
        }

        static PaymentReceived with(String orderId, PaymentId paymentId, OrderInfo.OrderStatusEnum newState) {
            return new PaymentReceived(orderId, paymentId, newState);
        }


		@Override
		public String identity() {
			return orderId;
		}
    }

    class OrderShipped extends IdentifiedDomainEvent {
        public final String orderId;
        public final OrderInfo.OrderStatusEnum newState;

        public OrderShipped(String orderId, OrderInfo.OrderStatusEnum newState) {
            this.orderId = orderId;
            this.newState = newState;
        }

        static OrderShipped with(String orderId, OrderInfo.OrderStatusEnum newState) {
            return new OrderShipped(orderId, newState);
        }

		@Override
		public String identity() {
			return orderId;
		}
    }
}
