package io.vlingo.xoom.examples.ecommerce;

import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.vlingo.xoom.actors.testkit.TestUntil;
import io.vlingo.xoom.examples.ecommerce.model.OrderResource;
import io.vlingo.xoom.examples.ecommerce.model.ProductId;
import io.vlingo.xoom.examples.ecommerce.model.UserId;
import org.apache.http.HttpStatus;
import org.junit.*;

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
    private static final AtomicInteger portNumber = new AtomicInteger(18080);
    
    private Bootstrap bootstrap;
    
    private int orderPortNumber;
    private final Object lock = new Object();

    @Before
    public void setUp() {
        orderPortNumber = portNumber.getAndIncrement();
        bootstrap = Bootstrap.forTest(orderPortNumber);
        Boolean startUpSuccess = bootstrap.serverStartup().await(100);
        assertThat(startUpSuccess, is(equalTo(true)));
    }

    @After
    public void cleanUp() {
        // Shutdown is not reliable yet; see https://github.com/vlingo/xoom-http/issues/25
    	bootstrap.stopAndCleanup();
    }

    private RequestSpecification baseGiven() {
        return given()
                .port(orderPortNumber)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON);
    }

    private String getOrderId(final String orderUrl) {
        return orderUrl.split("/")[2];
    }

    private String createOrderWithSingleItem() {

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
        String orderUrl = createOrderWithSingleItem();
        String orderId = getOrderId(orderUrl);

        final String expected = String.format(
                "{\"orderId\":\"%s\",\"orderItems\":[{\"productId\":{\"id\":\"pid1\"},\"quantity\":100}]," +
                        "\"orderState\":\"notPaid\"}", orderId);
        baseGiven()
                .when()
                .get(orderUrl)
                .then()
                .assertThat()
                .body(is(equalTo(expected)));
    }

    @Test
    public void createdOrderContainsProduct_inParallel() throws InterruptedException {
        final int TASK_COUNT = 10;
        final TestUntil until = TestUntil.happenings(TASK_COUNT);
        final AtomicLong testPassCount = new AtomicLong();
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
                        testPassCount.addAndGet((pass?1:0));
                        until.happened();
                    }
                }
                return null;
            });
        }
        executorService.invokeAll(callableTests);
        until.completes();

        synchronized (lock) {
            Assert.assertEquals(TASK_COUNT, testPassCount.get());
        }
    }


    @Test
    public void orderIsPaid_whenPaymentReceived() {
        String orderUrl = createOrderWithSingleItem();
        String orderId = getOrderId(orderUrl);
        String randomPaymentId = "4567";

        final String paidOrderBody = String.format(
                "{\"orderId\":\"%s\",\"orderItems\":[{\"productId\":{\"id\":\"pid1\"},\"quantity\":100}]," +
                        "\"orderState\":\"paid\"}", orderId);


        baseGiven()
                .when()
                .body("{\"id\": " + randomPaymentId + "}")
                .post(orderUrl + "/payment")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);

        baseGiven()
                .when()
                .get(orderUrl)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body(is(equalTo(paidOrderBody)));
    }
}