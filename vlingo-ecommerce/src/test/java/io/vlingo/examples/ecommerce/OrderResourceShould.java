package io.vlingo.examples.ecommerce;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.vlingo.examples.ecommerce.model.OrderResource;
import io.vlingo.examples.ecommerce.model.ProductId;
import io.vlingo.examples.ecommerce.model.UserId;
import org.apache.http.HttpStatus;
import org.junit.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.MatchesPattern.matchesPattern;

public class OrderResourceShould {


    public static final int TASK_COUNT = 10;

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
        Bootstrap.instance().stop();
    }


    RequestSpecification baseGiven() {
        return given().port(8081).accept(ContentType.JSON);
    }

    String createOrder() {

        OrderResource.OrderCreateRequest request = OrderResource.OrderCreateRequest.Builder
                .create()
                .withProduct(new ProductId("pid1"), 100)
                .withUser(new UserId(1))
                .build();

        Response response =
                baseGiven()
                        .when()
                        .body(request, ObjectMapperType.GSON)
                        .post("/order")
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.SC_CREATED)
                        .header("Location", matchesPattern("/order/(\\d+)"))
                        .extract()
                        .response();
        return response.header("Location");
    }

    @Test
    public void orderContainsProducts_whenQueried() {
        String orderUrl = createOrder();
        String orderId  = getOrderId(orderUrl);

        final String expected = String.format(
                "{\"orderId\":\"%s\",\"orderItems\":[{\"productId\":{\"id\":\"pid1\"},\"quantity\":100}]," +
                        "\"orderState\":\"notPaid\"}", orderId);
        baseGiven()
                .when()
                .get(orderUrl)
                .then()
                .assertThat()
                .log().body()
                .body(is(equalTo(expected)));
    }

    private Boolean createdOrderContainsProducts()  {
        String orderUrl = createOrder();
        String orderId  = getOrderId(orderUrl);

        final String expected = String.format(
                "{\"orderId\":\"%s\",\"orderItems\":[{\"productId\":{\"id\":\"pid1\"},\"quantity\":100}]," +
                        "\"orderState\":\"notPaid\"}", orderId);

        String body  =
        baseGiven()
                .when()
                .get(orderUrl)
                .then()
                .assertThat()
                .extract()
                .body().asString();
        return body.equals(expected);
    }

    @Test
    public void createdOrderContainsProduct_inParallel() throws InterruptedException {
        List<Callable<Boolean>> callables       = new ArrayList<>();
        ExecutorService      executorService = Executors.newFixedThreadPool(4);
        for (int i = 0; i < TASK_COUNT; i++) {
            callables.add(this::createdOrderContainsProducts);
        }
        List<Future<Boolean>> fList = executorService.invokeAll(callables);
        do {
            long count = fList.stream().filter(Future::isDone).count();
            if (count == TASK_COUNT) {
                break;
            }
            Thread.sleep(100);
            System.out.println("Waiting for N tasks: " + (TASK_COUNT - count));
        } while (true);
        Assert.assertEquals(TASK_COUNT, fList.stream().filter(OrderResourceShould::getOrHandleWithFalse).count());
    }

    private static Boolean getOrHandleWithFalse(Future<Boolean> f) {
        try {
            return  f.get();
        } catch (Exception e) {
        }
        return false;
    }

    private String getOrderId(String orderUrl) {
        return orderUrl.split("/")[2];
    }

    //@Test --Not implemented in the actor yet, causes a failure
    //   
    public void orderIsPaid_whenPaymentReceived() throws IOException {
        String orderUrl = createOrder();
        String orderId  = getOrderId(orderUrl);

        final String expected = String.format(
                "{\"orderId\":\"%s\",\"orderItems\":[]," +
                        "\"orderState\":\"paid\"}", orderId);


        baseGiven()
                .log().all()
                .when()
                .body("{\"id\": 4564}")
                .post(orderUrl + "/payment")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body(is(equalTo(expected)));
    }
}