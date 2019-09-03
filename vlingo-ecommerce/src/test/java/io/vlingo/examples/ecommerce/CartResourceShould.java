package io.vlingo.examples.ecommerce;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.text.MatchesPattern.matchesPattern;

public class CartResourceShould {

    private static final AtomicInteger portNumber = new AtomicInteger(9091);

    private int cartPortNumber = portNumber.getAndIncrement();

    @Before
    public void setUp() {
        Bootstrap.instance(cartPortNumber);

        // This should not be needed; see https://github.com/vlingo/vlingo-http/issues/26
        RestAssured.defaultParser = Parser.JSON;
        Boolean startUpSuccess = Bootstrap.instance().serverStartup().await(100);
        assertThat(startUpSuccess, is(equalTo(true)));
    }

    @After
    public void cleanUp() {
        // Shutdown is not reliable yet; see https://github.com/vlingo/vlingo-http/issues/25
        Bootstrap.instance().stop();
    }


    RequestSpecification baseGiven() {
        return given()
                .port(cartPortNumber)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON);
    }

    String createCart() {
        Response response =
                baseGiven()
                        .when()
                        .body("{id: 100}")
                        .post("/cart")
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.SC_CREATED)
                        .header("Location", matchesPattern("/cart/(\\d+)"))
                        .extract().response();
        return response.header("Location");
    }

    @Test
    public void cartIsEmpty_whenCreated() {
        String cartUrl = createCart();

        baseGiven()
                .when()
                .get(cartUrl)
                .then()
                .body("$", empty());

    }

    @Test
    public void cartAddsProduct_whenProductIsAdded() throws IOException {

        String cartUrl = createCart();

        baseGiven()
                .when()
                .body("{operation: \"add\"}")
                .patch(cartUrl + "/pid1")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body(is(equalTo("[{\"productId\":{\"id\":\"pid1\"},\"quantity\":1}]")));

        baseGiven()
                .when()
                .body("{operation: \"add\"}")
                .patch(cartUrl + "/pid1")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body(is(equalTo("[{\"productId\":{\"id\":\"pid1\"},\"quantity\":2}]")));
    }
}