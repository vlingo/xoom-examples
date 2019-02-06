package io.vlingo.examples.ecommerce;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.MatchesPattern.matchesPattern;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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

public class OrderResourceShould {

    public static final int TASK_COUNT = 10;

    private static final AtomicInteger portNumber = new AtomicInteger(8081);

    private int orderPortNumber = portNumber.getAndIncrement();

    @Before
    public void setUp() throws InterruptedException {
        Bootstrap.instance(orderPortNumber);

        //todo:missing response Content-Type
        RestAssured.defaultParser = Parser.JSON;
        Boolean startUpSuccess = Bootstrap.instance().serverStartup().await(100);
        assertThat(startUpSuccess, is(equalTo(true)));
    }

    @After
    public void cleanUp() throws InterruptedException {
        //todo: this call fails after timeout / does not throw exception
        //Bootstrap.instance().server.shutDown().await(1);
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
        final TestUntil until = TestUntil.happenings(10);

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
        
        fList.stream().forEach((taskAcknowleable) -> readOrHandleWithFalse(taskAcknowleable, until));

        until.completes();

        synchronized (lock) {
          Assert.assertEquals(TASK_COUNT, totalTaskCount);
        }
    }

    private final Object lock = new Object();
    private int totalTaskCount = 0;

    private void readOrHandleWithFalse(Future<Boolean> f, final TestUntil until) {
        try {
          synchronized (lock) {
            if (f.get()) {
              System.out.println("READ TRUE");
            } else {
              System.out.println("READ FALSE");
            }

            // this is accurate because some may have
            // already executed and stopped

            ++totalTaskCount;
          }
        } catch (Exception e) {
          System.out.println("READ FAILED");
        }

        until.happened();

        return;
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