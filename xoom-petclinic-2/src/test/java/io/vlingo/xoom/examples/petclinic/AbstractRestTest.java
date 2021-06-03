package io.vlingo.xoom.examples.petclinic;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.specification.RequestSpecification;
import io.vlingo.xoom.examples.petclinic.infrastructure.XoomInitializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.CoreMatchers.equalTo;


public abstract class AbstractRestTest {

    protected int port = 18080;
    private XoomInitializer xoom;

    @BeforeAll
    public static void init() {
        //port = (int)(Math.random() * 55535) + 10000;
        RestAssured.defaultParser = Parser.JSON;
    }

    @BeforeEach
    public void setUp() throws Exception {
        port = (int) (Math.random() * 55535) + 10000;

        XoomInitializer.main(new String[]{"-Dport=" + port});
        xoom = XoomInitializer.instance();
        Boolean startUpSuccess = xoom.server().startUp().await(100);
        System.out.println("==== Test Server running on " + port);
        assertThat(startUpSuccess, is(equalTo(true)));
    }

    @AfterEach
    public void cleanUp() throws Exception {
        System.out.println("==== Test Server shutting down ");
        xoom.server().stop();
        //xoom.stopServer();
    }

    public RequestSpecification given() {
        return io.restassured.RestAssured.given()
                .filter(new RequestLoggingFilter())
                .filter(new ResponseLoggingFilter())
                .port(port)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON);
    }
}
