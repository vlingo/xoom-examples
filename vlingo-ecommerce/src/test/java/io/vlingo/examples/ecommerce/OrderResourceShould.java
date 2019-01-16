package io.vlingo.examples.ecommerce;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.vlingo.examples.ecommerce.model.OrderResource;
import io.vlingo.examples.ecommerce.model.ProductId;
import io.vlingo.examples.ecommerce.model.UserId;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static io.vlingo.common.serialization.JsonSerialization.serialized;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.text.MatchesPattern.matchesPattern;

public class OrderResourceShould {

    @BeforeClass
    public static void setUp() throws InterruptedException {
        //todo:missing response Content-Type
        RestAssured.defaultParser = Parser.JSON;
        Boolean startUpSuccess = Bootstrap.instance().serverStartup().await(100);
        assertThat(startUpSuccess, is(equalTo(true)));
    }

    @AfterClass
    public static void cleanUp() throws InterruptedException {
        Bootstrap.instance().stop();
    }


    RequestSpecification baseGiven() {
        return given().port(8081).accept(ContentType.JSON);
    }

    String createOrder() {

        OrderResource.OrderCreateRequest request = OrderResource.OrderCreateRequest.Builder
                                         .create()
                .withProduct(new ProductId("pid1"), 100)
                .withProduct(new ProductId("pid2"), 200)
                .withUser(new UserId(1))
                .build();

        Response response =
                baseGiven()
                        .when()
                        .body(serialized(request))
                        .post("/cart")
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.SC_CREATED)
                        .header("Location", matchesPattern("/order/(\\d+)"))
                        .extract().response();
        return response.header("Location");
    }

    @Test
    public void cartIsEmpty_whenCreated() {
        String cartUrl = createOrder();

        baseGiven()
                .when()
                .get(cartUrl)
                .then()
                .body("$", empty());

    }

    @Test
    public void cartAddsProduct_whenProductIsAdded() throws IOException {

        String cartUrl = createOrder();

        baseGiven()
                .log().all()
                .when()
                .body("{operation: \"add\"}")
                .patch(cartUrl + "/pid1")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body(is(equalTo("[{\"productId\":{\"id\":\"pid1\"},\"quantity\":1}]")));
    }
}