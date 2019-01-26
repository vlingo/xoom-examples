package io.vlingo.examples.ecommerce;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.text.MatchesPattern.matchesPattern;

public class CartResourceShould {

    @BeforeClass
    public static void setUp() throws InterruptedException {
        //todo:missing response Content-Type
        RestAssured.defaultParser = Parser.JSON;
        Boolean startUpSuccess = Bootstrap.instance().serverStartup().await(100);
        assertThat(startUpSuccess, is(equalTo(true)));
    }

    @AfterClass
    public static void cleanUp() throws InterruptedException {
        //todo: this call fails after timeout / does not throw exception
        //Bootstrap.instance().server.shutDown().await(1);
        Bootstrap.instance().server.stop();
    }


    RequestSpecification baseGiven() {
        return given().port(8081).accept(ContentType.JSON);
    }

    String createCart() {
        //todo:exceptions cause hangs, not 500  errors!!
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