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

public abstract class AbstractRestTest {

  protected int port;
  private XoomInitializer xoom;

  @BeforeAll
  public static void init() {
    //port = (int)(Math.random() * 55535) + 10000;
    RestAssured.defaultParser = Parser.JSON;
  }

  @BeforeEach
  public void setUp() throws Exception {
    port = (int) (Math.random() * 55535) + 10000;

    XoomInitializer.main(new String[]{String.valueOf(port)});
    xoom = XoomInitializer.instance();
    Boolean startUpSuccess = xoom.server().startUp().await(100);
    System.out.println("==== Test Server running on " + port);
  }

  @AfterEach
  public void cleanUp() throws Exception {
    System.out.println("==== Test Server shutting down ");
    xoom.server().stop();
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
