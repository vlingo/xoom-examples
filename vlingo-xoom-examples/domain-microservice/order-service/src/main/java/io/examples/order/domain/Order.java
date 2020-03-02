package io.examples.order.domain;

import io.examples.order.domain.state.OrderStatus;
import io.examples.infra.Identity;
import io.vlingo.xoom.stepflow.StepFlow;
import io.vlingo.xoom.stepflow.StateTransition;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import java.util.UUID;

@Entity(name = "orders")
public class Order extends Identity {

    private Long accountId;
    private Long paymentId;
    private OrderStatus status = OrderStatus.ORDER_CREATED;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private OrderShippingAddress shippingAddress;

    public Order() {
    }

    public Order(OrderShippingAddress shippingAddress) {
        super();
        this.shippingAddress = shippingAddress;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public OrderShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(OrderShippingAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    @SuppressWarnings("unchecked")
    public Order sendEvent(StepFlow processor, OrderStatus targetState) {
        OrderEvent event = new OrderEvent(status.name(), targetState.name());
        StateTransition stateTransition = processor.applyEvent(event).await();
        stateTransition.apply(this);
        this.status = OrderStatus.valueOf(stateTransition.getTargetName());
        this.version = UUID.randomUUID().toString();
        return this;
    }
}