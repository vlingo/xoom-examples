package io.vlingo.developers.petclinic;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.specification.RequestSpecification;
import io.vlingo.developers.petclinic.infrastructure.Bootstrap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public abstract class AbstractRestTest {

    protected int port;

    @BeforeAll
    public static void init(){
        //port = (int)(Math.random() * 55535) + 10000;
        RestAssured.defaultParser = Parser.JSON;
    }

    @BeforeEach
    public void setUp() throws Exception {
        port = (int)(Math.random() * 55535) + 10000;
        Bootstrap.main(new String[]{String.valueOf(port)});
        System.out.println("==== Test Server running on "+port);
    }

    @AfterEach
    public void cleanUp() throws Exception {
        System.out.println("==== Test Server shutting down ");
        Bootstrap.stopServer();
    }

    public RequestSpecification given(){
        return io.restassured.RestAssured.given()
                .filter(new RequestLoggingFilter())
                .filter(new ResponseLoggingFilter())
                .port(port)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON);
    }
}
