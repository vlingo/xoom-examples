package io.vlingo.examples.ecommerce;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.vlingo.actors.testkit.TestUntil;
import io.vlingo.examples.ecommerce.model.OrderResource;
import io.vlingo.examples.ecommerce.model.ProductId;
import io.vlingo.examples.ecommerce.model.UserId;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.MatchesPattern.matchesPattern;

public class OrderResourceShould {

    public static final int TASK_COUNT = 10;

    private static final AtomicInteger portNumber = new AtomicInteger(8081);

    private int orderPortNumber = portNumber.getAndIncrement();
    private final Object lock = new Object();

    @Before
    public void setUp() throws InterruptedException {
        Bootstrap.instance(orderPortNumber);

        // This should not be needed; see https://github.com/vlingo/vlingo-http/issues/26
        RestAssured.defaultParser = Parser.JSON;
        Boolean startUpSuccess = Bootstrap.instance().serverStartup().await(100);
        assertThat(startUpSuccess, is(equalTo(true)));
    }

    @After
    public void cleanUp() throws InterruptedException {
        // Shutdown is not reliable yet; see https://github.com/vlingo/vlingo-http/issues/25
        Bootstrap.instance().stop();
    }

    RequestSpecification baseGiven() {
        return given().port(orderPortNumber).accept(ContentType.JSON);
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
        String orderId = getOrderId(orderUrl);

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

    @Test
    public void createdOrderContainsProduct_inParallel() throws InterruptedException {
        final TestUntil until = TestUntil.happenings(TASK_COUNT);
        final AtomicLong passCount = new AtomicLong();
        final List<Callable<Void>> callableTests = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for (int i = 0; i < TASK_COUNT; i++) {
            callableTests.add(() -> {
                boolean pass = false;
                try {
                    orderContainsProducts_whenQueried();
                    pass = true;
                } catch (Exception ignored) {}
                finally {
                    synchronized (lock) {
                        passCount.addAndGet((pass?1:0));
                        until.happened();
                    }
                }
                return null;
            });
        }
        executorService.invokeAll(callableTests);
        until.completes();

        synchronized (lock) {
            Assert.assertEquals(TASK_COUNT, passCount.get());
        }
    }

    private String getOrderId(String orderUrl) {
        return orderUrl.split("/")[2];
    }

    @Test
    public void orderIsPaid_whenPaymentReceived() throws IOException {
        String orderUrl = createOrder();
        String orderId = getOrderId(orderUrl);

        final String expected = String.format(
                "{\"orderId\":\"%s\",\"orderItems\":[{\"productId\":{\"id\":\"pid1\"},\"quantity\":100}]," +
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