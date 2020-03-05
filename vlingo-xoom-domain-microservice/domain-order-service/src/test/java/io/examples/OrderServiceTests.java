package io.examples;

import io.examples.account.application.AccountClient;
import io.examples.account.domain.model.AccountAddress;
import io.examples.account.domain.model.AccountQuery;
import io.examples.order.domain.Order;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import io.reactivex.Single;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@MicronautTest(application = OrderApplication.class, environments = "test")
public class OrderServiceTests {

    @Inject
    EmbeddedServer embeddedServer;

    @Inject
    AccountClient accountClient;

    @Inject
    @Client("order")
    RxHttpClient client;

    @Test
    public void testCreateOrganization() {
        Order order = new Order();
        order.setAccountId(1L);

        AccountQuery accountQuery = new AccountQuery("12345");

        accountQuery.getAddresses()
                .add(new AccountAddress("101 4th Ave", "", "CA",
                        "Palo Alto", "US", AccountAddress.AddressType.SHIPPING, 94403));

        when(accountClient.queryAccount(1L)).then(invocation -> Single.create(onSubscribe ->
                onSubscribe.onSuccess(accountQuery)));

        HttpRequest<Order> request = HttpRequest.POST("/v1/orders", order);
        //Order response = client.toBlocking().retrieve(request, Order.class);
        //assertEquals(OrderStatus.ACCOUNT_CONNECTED, Objects.requireNonNull(response).getStatus());
    }

    @MockBean(AccountClient.class)
    AccountClient accountClient() {
        return mock(AccountClient.class);
    }
}
