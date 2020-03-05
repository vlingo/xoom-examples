package io.examples;

import io.examples.account.domain.Account;
import io.examples.account.domain.Address;
import io.examples.account.domain.CreditCard;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;


@MicronautTest(application=AccountApplication.class)
public class AccountServiceTests {
    @Inject
    EmbeddedServer server;

    @Inject
    @Client("/v1")
    HttpClient client;

    @SuppressWarnings("unchecked")
    @Test
    public void testAccountCrudOperations() {

        List<Long> accountIds = new ArrayList<>();

        // Create a new account
        Account account = new Account("12345");

        // Create a new credit card for the account
        CreditCard creditCard = new CreditCard("1234567801234567", CreditCard.CreditCardType.VISA);

        // Add the credit card to the customer's account
        account.getCreditCards().add(creditCard);

        // Create a new shipping address for the customer
        Address address = new Address("1600 Pennsylvania Ave NW", null,
                "DC", "Washington", "United States", Address.AddressType.SHIPPING, 20500);

        // Add address to the customer's account
        account.getAddresses().add(address);

//        HttpRequest request = HttpRequest.POST("/accounts", account);
//        HttpResponse<Account> response = client.toBlocking().exchange(request, Account.class);
//        Account accountReply = response.getBody().get();
//        accountIds.add(accountReply.getId());
//
//        assertEquals(HttpStatus.CREATED, response.getStatus());
//
//        request = HttpRequest.POST("/accounts", account);
//        response = client.toBlocking().exchange(request, Account.class);
//
//        assertEquals(HttpStatus.CREATED, response.getStatus());
//
//        Long id = response.getBody().get().getId();
//        accountIds.add(id);
//        request = HttpRequest.GET("/accounts/" + id);
//
//        Account accountResponse = client.toBlocking().retrieve(request, Account.class);
//
//        assertEquals("12345", account.getAccountNumber());
//
//        request = HttpRequest.PUT("/accounts/" + id, new Account("54321", account.getAddresses()));
//        response = client.toBlocking().exchange(request, Account.class);
//
//        assertEquals(HttpStatus.OK, response.getStatus());
//
//        request = HttpRequest.GET("/accounts/" + id);
//        accountResponse = client.toBlocking().retrieve(request, Account.class);
//        assertEquals("54321", accountResponse.getAccountNumber());
//
//        request = HttpRequest.GET("/accounts");
//        List<Account> accounts = client.toBlocking().retrieve(request, Argument.of(List.class, Account.class));
//
//        assertEquals(2, accounts.size());
//
//        // cleanup:
//        for (Long genreId : accountIds) {
//            request = HttpRequest.DELETE("/accounts/" + genreId);
//            response = client.toBlocking().exchange(request);
//            assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
//        }
    }
}
