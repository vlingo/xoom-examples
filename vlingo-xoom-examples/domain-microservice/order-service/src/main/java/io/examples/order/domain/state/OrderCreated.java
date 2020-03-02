package io.examples.order.domain.state;

import io.examples.account.application.AccountContext;
import io.examples.account.domain.model.AccountQuery;
import io.examples.order.domain.OrderShippingAddress;
import io.examples.order.domain.Order;
import io.vlingo.xoom.stepflow.Transition;
import io.vlingo.xoom.stepflow.TransitionHandler;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static io.vlingo.xoom.stepflow.TransitionBuilder.from;
import static io.vlingo.xoom.stepflow.TransitionHandler.handle;

/**
 * When an {@link Order} is created, it must be connected to an {@link AccountQuery}. The {@link AccountQuery}
 * is a query model retrieved from the {@link AccountContext}. An accountId is provided to the {@link Order}
 * when the definition is first created by a consumer.
 * <p>
 * This class is responsible for transitioning the state of the {@link Order} from {@link OrderCreated} to
 * {@link AccountConnected}. The connectOrder method is a command handler that is provided with the account
 * context to check if the {@link AccountQuery} can be fetched. Once the {@link AccountQuery} is fetched
 * from the account microservice, the active shipping address on the account will be copied into the {@link Order}.
 * <p>
 * The shipping address is cloned to the order at the time the order is created, which means that the address
 * can only be changed in relation to the order itself.
 *
 * @author Kenny Bastani
 */
@Singleton
public class OrderCreated extends OrderState<OrderCreated> {

    private final AccountContext accountContext;
    private final AccountConnected accountConnected;

    public OrderCreated(Provider<AccountContext> accountContext, AccountConnected accountConnected) {
        this.accountContext = accountContext.get();
        this.accountConnected = accountConnected;
    }

    @Override
    public TransitionHandler[] getTransitionHandlers() {
        return new TransitionHandler[]{
                // Here we define the transition handler from OrderCreated to AccountConnected
                handle(from(this).to(accountConnected).on(Order.class)
                        .then(this::connectAccount)
                        .then(Transition::logResult))
        };
    }

    /**
     * This is the function command for handling the {@link OrderCreated} event. This function will call the
     * account service to retrieve a shipping address and add it to the order.
     *
     * @param order is the definition of the {@link Order} containing the context for this event handler
     * @return the updated {@link Order} definition to be persisted
     */
    private Order connectAccount(Order order) {
        // Retrieve the reactive client request to query the account service via its context
        CompletableFuture<AccountQuery> accountFuture = accountContext.getAccount()
                .query(order.getAccountId())
                .await();

        AccountQuery accountQuery;

        try {
            // This will execute the reactive HTTP request to query the account
            accountQuery = accountFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error getting account query: " + e.getCause().getMessage(), e);
        }

        // The shipping address is then copied from the account query to the order
        order.setShippingAddress(OrderShippingAddress.translateFrom(accountQuery.getAddresses()
                .stream()
                .filter(address -> address.getType().name().equals(OrderShippingAddress.AddressType.SHIPPING.name()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("The account does not have a shipping address"))));

        return order;
    }

    @Override
    public String getName() {
        return OrderStatus.ORDER_CREATED.name();
    }
}
