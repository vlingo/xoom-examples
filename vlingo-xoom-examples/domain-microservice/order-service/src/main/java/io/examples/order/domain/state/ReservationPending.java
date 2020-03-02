package io.examples.order.domain.state;

import io.examples.order.domain.Order;
import io.vlingo.xoom.stepflow.Transition;
import io.vlingo.xoom.stepflow.TransitionHandler;

import javax.inject.Singleton;

import static io.vlingo.xoom.stepflow.TransitionBuilder.from;
import static io.vlingo.xoom.stepflow.TransitionHandler.handle;

/**
 * The {@link ReservationPending} state transitions from the {@link AccountConnected} state. This state
 * is responsible for reserving inventory for the order in a warehouse. If the inventory is not available
 * for the {@link Order} then the state will transition to {@link ReservationFailed}, and any inventory
 * that was reserved for this {@link Order} will be unreserved.
 * <p>
 * If all the inventory can be reserved for the {@link Order}, then the state will transition to
 * the {@link ReservationSucceeded} state.
 * <p>
 * The progress of the reservations are bound to the INVENTORY_RESERVED sub-state transition. Each event
 * will fork and join together to determine the success or failure of the reservation.
 * <p>
 * The progress of the reservations can be done incrementally through HTTP request/response or can be handled
 * asynchronously with durable message queues. For this example, the inventory reservations will be
 * the responsibility of an external HTTP consumer.
 *
 * @author Kenny Bastani
 */
@Singleton
public class ReservationPending extends OrderState<ReservationPending> {

    private final ReservationFailed reservationFailed;
    private final ReservationSucceeded reservationSucceeded;

    public ReservationPending(ReservationFailed reservationFailed,
                              ReservationSucceeded reservationSucceeded) {
        this.reservationFailed = reservationFailed;
        this.reservationSucceeded = reservationSucceeded;
    }

    @Override
    public TransitionHandler[] getTransitionHandlers() {
        return new TransitionHandler[]{
                handle(from(this).to(this)
                        .then(Transition::logResult))
                        .withAddress(OrderStatus.INVENTORY_RESERVED.name()),
                handle(from(this).to(reservationSucceeded)
                        .then(Transition::logResult)),
                handle(from(this).to(reservationFailed)
                        .then(Transition::logResult))
        };
    }

    /**
     * This is a reactive streams processor that will consume line items from the {@link Order} and reserve inventory
     * in a warehouse. The inventory will be reserved by associating an inventory item with this order's identity.
     * When an inventory item is successfully reserved from the inventory service, a durable confirmation of the
     * reservation will be posted back to the order service.
     * <p>
     * If the reservation post-back fails because the {@link Order} state has transitioned away from
     * {@link ReservationPending} state, the reservation will automatically be unreserved in the warehouse.
     *
     * @param order
     * @return
     */
    public Order reserveNextInventory(Order order) {
        return order;
    }

    @Override
    public String getName() {
        return OrderStatus.RESERVATION_PENDING.name();
    }
}
