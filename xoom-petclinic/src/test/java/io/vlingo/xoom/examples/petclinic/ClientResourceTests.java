package io.vlingo.xoom.examples.petclinic;

import io.restassured.common.mapper.TypeRef;
import io.vlingo.xoom.examples.petclinic.infrastructure.ClientData;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ClientResourceTests extends AbstractRestTest {

  private final String client1 =
      "{\n" +
          "  \"name\": {\n" +
          "    \"first\": \"Harry\",\n" +
          "    \"last\": \"Potter\"\n" +
          "  },\n" +
          "  \"contactInformation\": {\n" +
          "    \"postalAddress\": {\n" +
          "      \"streetAddress\": \"St,\",\n" +
          "      \"city\": \"London\",\n" +
          "      \"stateProvince\": \"UK\",\n" +
          "      \"postalCode\": \"000\"\n" +
          "    },\n" +
          "    \"telephone\": {\n" +
          "      \"number\": \"01020304\"\n" +
          "    }\n" +
          "  }\n" +
          "}";

  @Test
  public void testEmptyResponse() {
    given()
        .when()
        .get("/clients")
        .then()
        .statusCode(200)
        .body(is(equalTo("[]")));
  }

  @Test
  public void save() {
    ClientData data = given()
        .when()
        .body(client1)
        .post("/clients")
        .then()
        .statusCode(201)
        .extract()
        .body()
        .as(ClientData.class);

    assertThat(data, is(notNullValue()));
    assertThat(data.id, is(notNullValue()));
    assertThat(data.name, is(notNullValue()));
    assertThat(data.contactInformation, is(notNullValue()));
    assertThat(data.contactInformation.postalAddress, is(notNullValue()));
    assertThat(data.contactInformation.telephone, is(notNullValue()));
    assertThat(data.name.first, is(equalTo("Harry")));
    assertThat(data.name.last, is(equalTo("Potter")));
    assertThat(data.contactInformation.postalAddress.streetAddress, is(equalTo("St,")));
    assertThat(data.contactInformation.postalAddress.city, is(equalTo("London")));
    assertThat(data.contactInformation.postalAddress.stateProvince, is(equalTo("UK")));
    assertThat(data.contactInformation.postalAddress.postalCode, is(equalTo("000")));
    assertThat(data.contactInformation.telephone.number, is(equalTo("01020304")));
  }

  private ClientData saveExampleData() {
    return given()
        .when()
        .body(client1)
        .post("/clients")
        .then()
        .statusCode(201)
        .extract()
        .body()
        .as(ClientData.class);
  }

  @Test
  public void changeName() {
    ClientData data = saveExampleData();
    data = given()
        .when()
        .body("{\"name\": {\"first\": \"Albus\",\"last\": \"Dumbledore\"}, \"contactInformation\":{\"postalAddress\": {\"streetAddress\":\"\"}}}")
        .patch("/clients/{id}/name", data.id)
        .then()
        .statusCode(200)
        .extract()
        .body()
        .as(ClientData.class);

    assertThat(data, is(notNullValue()));
    assertThat(data.id, is(notNullValue()));
    assertThat(data.name.first, is(equalTo("Albus")));
    assertThat(data.name.last, is(equalTo("Dumbledore")));
  }

  @Test
  public void changeContact() {
    ClientData data = saveExampleData();
    data = given()
        .when()
        .body("{\"contactInformation\": {\n" +
            "    \"postalAddress\": {\n" +
            "      \"streetAddress\": \"Ave,\",\n" +
            "      \"city\": \"New-York\",\n" +
            "      \"stateProvince\": \"US\",\n" +
            "      \"postalCode\": \"123\"\n" +
            "    },\n" +
            "    \"telephone\": {\n" +
            "      \"number\": \"99119911\"\n" +
            "    }\n" +
            "  }}")
        .patch("/clients/{id}/contact", data.id)
        .then()
        .statusCode(200)
        .extract()
        .body()
        .as(ClientData.class);

    assertThat(data, is(notNullValue()));
    assertThat(data.id, is(notNullValue()));
    assertThat(data.contactInformation.postalAddress.streetAddress, is(equalTo("Ave,")));
    assertThat(data.contactInformation.postalAddress.city, is(equalTo("New-York")));
    assertThat(data.contactInformation.postalAddress.stateProvince, is(equalTo("US")));
    assertThat(data.contactInformation.postalAddress.postalCode, is(equalTo("123")));
    assertThat(data.contactInformation.telephone.number, is(equalTo("99119911")));
  }

  @Test
  @Disabled("not implemented")
  public void saveAndFetchById() {
    ClientData data = saveExampleData();
    final String id = data.id;

    data = given()
        .when()
        .get("/clients/{id}", id)
        .then()
        .statusCode(200)
        .extract()
        .body().as(ClientData.class);

    assertThat(data, is(notNullValue()));
    assertThat(data.id, is(equalTo(id)));
    assertThat(data.name.first, is(equalTo("Harry")));
    assertThat(data.name.last, is(equalTo("Potter")));
  }

  @Test
  public void saveAndFetchAll() {
    final ClientData animalTypeData = saveExampleData();
    final String id = animalTypeData.id;

    List<ClientData> clients = given()
        .when()
        .get("/clients")
        .then()
        .statusCode(200)
        .extract()
        .body().as(new TypeRef<List<ClientData>>() {
        });

    assertThat(clients, is(notNullValue()));
    assertThat(clients.size(), is(1));
    ClientData data = clients.get(0);
    assertThat(data.id, is(id));
    assertThat(data.name.first, is(equalTo("Harry")));
    assertThat(data.name.last, is(equalTo("Potter")));
  }

}
