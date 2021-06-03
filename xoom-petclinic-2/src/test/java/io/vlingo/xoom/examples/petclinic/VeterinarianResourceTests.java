package io.vlingo.xoom.examples.petclinic;

import io.restassured.common.mapper.TypeRef;
import io.vlingo.xoom.examples.petclinic.infrastructure.VeterinarianData;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class VeterinarianResourceTests extends AbstractRestTest {

  private final String vet1 = "{\n" + "  \"name\": {\n" + "    \"first\": \"Severus\",\n" + "    \"last\": \"Snape\"\n"
      + "  },\n" + "  \"contactInformation\": {\n" + "    \"postalAddress\": {\n" + "      \"streetAddress\": \"Hogwarts st.\",\n"
      + "      \"city\": \"London\",\n" + "      \"stateProvince\": \"UK\",\n" + "      \"postalCode\": \"111\"\n"
      + "    },\n" + "    \"telephone\": {\n" + "      \"number\": \"01020304\"\n" + "    }\n" + "  },\n"
      + "  \"specialty\": {\n" + "    \"specialtyTypeId\": \"Surgery\"\n" + "  }\n" + "}";

  @Test
  public void testEmptyResponse() {
    given().when().get("/veterinarians").then().statusCode(200).body(is(equalTo("[]")));
  }

  @Test
  public void save() {
    VeterinarianData data = given().when().body(vet1).post("/veterinarians/").then().statusCode(201).extract().body()
        .as(VeterinarianData.class);

    assertThat(data, is(notNullValue()));
    assertThat(data.id, is(notNullValue()));
    assertThat(data.name, is(notNullValue()));
    assertThat(data.contactInformation, is(notNullValue()));
    assertThat(data.contactInformation.postalAddress, is(notNullValue()));
    assertThat(data.contactInformation.telephone, is(notNullValue()));
    assertThat(data.specialty, is(notNullValue()));
    assertThat(data.name.first, is(equalTo("Severus")));
    assertThat(data.name.last, is(equalTo("Snape")));
    assertThat(data.contactInformation.postalAddress.streetAddress, is(equalTo("Hogwarts st.")));
    assertThat(data.contactInformation.postalAddress.city, is(equalTo("London")));
    assertThat(data.contactInformation.postalAddress.stateProvince, is(equalTo("UK")));
    assertThat(data.contactInformation.postalAddress.postalCode, is(equalTo("111")));
    assertThat(data.contactInformation.telephone.number, is(equalTo("01020304")));
    assertThat(data.specialty.specialtyTypeId, is(equalTo("Surgery")));
  }

  private VeterinarianData saveExampleData() {
    return given().when().body(vet1).post("/veterinarians").then().statusCode(201).extract().body()
        .as(VeterinarianData.class);
  }

  @Test
  public void changeName() {
    VeterinarianData data = saveExampleData();
    data = given().when().body("{\"name\": {\"first\": \"Albus\",\"last\": \"Dumbledore\"}}")
        .patch("/veterinarians/{id}/name", data.id).then().statusCode(200).extract().body().as(VeterinarianData.class);

    assertThat(data, is(notNullValue()));
    assertThat(data.id, is(notNullValue()));
    assertThat(data.name.first, is(equalTo("Albus")));
    assertThat(data.name.last, is(equalTo("Dumbledore")));
  }

  @Test
  public void changeContact() {
    VeterinarianData data = saveExampleData();
    data = given().when()
        .body("{\"contactInformation\": {\n" + "    \"postalAddress\": {\n" + "      \"streetAddress\": \"Ave,\",\n"
            + "      \"city\": \"New-York\",\n" + "      \"stateProvince\": \"US\",\n"
            + "      \"postalCode\": \"123\"\n" + "    },\n" + "    \"telephone\": {\n"
            + "      \"number\": \"99119911\"\n" + "    }\n" + "  }}")
        .patch("/veterinarians/{id}/contact", data.id).then().statusCode(200).extract().body()
        .as(VeterinarianData.class);

    assertThat(data, is(notNullValue()));
    assertThat(data.id, is(notNullValue()));
    assertThat(data.contactInformation.postalAddress.streetAddress, is(equalTo("Ave,")));
    assertThat(data.contactInformation.postalAddress.city, is(equalTo("New-York")));
    assertThat(data.contactInformation.postalAddress.stateProvince, is(equalTo("US")));
    assertThat(data.contactInformation.postalAddress.postalCode, is(equalTo("123")));
    assertThat(data.contactInformation.telephone.number, is(equalTo("99119911")));
  }

  @Test
  public void specializesIn() {
    VeterinarianData data = saveExampleData();
    data = given().when().body("{\"specialty\": {\"specialtyTypeId\": \"Behaviour\"}}")
        .patch("/veterinarians/{id}/specialty", data.id).then().statusCode(200).extract().body()
        .as(VeterinarianData.class);

    assertThat(data, is(notNullValue()));
    assertThat(data.id, is(notNullValue()));
    assertThat(data.specialty.specialtyTypeId, is(equalTo("Behaviour")));
  }

  @Test
  @Disabled("not implemented")
  public void saveAndFetchById() {
    VeterinarianData data = saveExampleData();
    final String id = data.id;

    data = given().when().get("/veterinarians/{id}", id).then().statusCode(200).extract().body()
        .as(VeterinarianData.class);

    assertThat(data, is(notNullValue()));
    assertThat(data.id, is(equalTo(id)));
    assertThat(data.name.first, is(equalTo("Severus")));
    assertThat(data.name.last, is(equalTo("Snape")));
  }

  @Test
  public void saveAndFetchAll() {
    final VeterinarianData animalTypeData = saveExampleData();
    final String id = animalTypeData.id;

    List<VeterinarianData> veterinarians = given().when().get("/veterinarians/").then().statusCode(200).extract().body()
        .as(new TypeRef<List<VeterinarianData>>() {
        });

    assertThat(veterinarians, is(notNullValue()));
    assertThat(veterinarians.size(), is(1));
    VeterinarianData data = veterinarians.get(0);
    assertThat(data.id, is(id));
    assertThat(data.name.first, is(equalTo("Severus")));
    assertThat(data.name.last, is(equalTo("Snape")));
  }

}
