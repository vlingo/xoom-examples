package io.vlingo.developers.petclinic;

import io.restassured.common.mapper.TypeRef;
import io.restassured.mapper.ObjectMapper;
import io.restassured.path.json.mapper.factory.DefaultJackson2ObjectMapperFactory;
import io.vlingo.developers.petclinic.infrastructure.KindData;
import io.vlingo.developers.petclinic.infrastructure.NameData;
import io.vlingo.developers.petclinic.infrastructure.OwnerData;
import io.vlingo.developers.petclinic.infrastructure.PetData;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PetResourceTests extends AbstractRestTest{

    private final String HEDWIG =
        "{\n" +
            "  \"name\": {\n" +
            "    \"value\": \"Hedwig\"\n" +
            "  },\n" +
            "  \"birth\": 100,\n" +
            "  \"kind\": {\n" +
            "    \"animalTypeId\": \"Owl\"\n" +
            "  },\n" +
            "  \"owner\": {\n" +
            "    \"clientId\": \"Potter\"\n" +
            "  }\n" +
        "}";

    @Test
    public void testEmptyResponse(){
        given()
                .when()
                .get("/pets")
                .then()
                .statusCode(200)
                .body(is(equalTo("[]")));
    }

    @Test
    public void save(){
        PetData res = given()
                /*.when()*/
                .body(HEDWIG)
                .post("/pets")
                .then()
                .statusCode(201)
                .extract()
                .body()
                .as(PetData.class);

        assertThat(res, is(notNullValue()));
        assertThat(res.id, is(notNullValue()));
        assertThat(res.name, is(notNullValue()));
        assertThat(res.name.value, is(equalTo("Hedwig")));
        assertThat(res.birth, is(equalTo(100L)));
        assertThat(res.death, is(equalTo(0L)));
        assertThat(res.kind, is(notNullValue()));
        assertThat(res.kind.animalTypeId, is(equalTo("Owl")));
        assertThat(res.owner, is(notNullValue()));
        assertThat(res.owner.clientId, is(equalTo("Potter")));
        assertThat(res.visit, is(nullValue()));
    }

    private PetData saveExampleData(){
        return given()
                .when()
                .body(HEDWIG)
                .post("/pets")
                .then()
                .statusCode(201)
                .extract()
                .body()
                .as(PetData.class);
    }

    @Test
    public void recordBirth(){
        PetData data = saveExampleData();
        data = given()
                .when()
                .body("{\"birth\": 101}")
                .patch("/pets/{id}/birth", data.id)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PetData.class);

        assertThat(data, is(notNullValue()));
        assertThat(data.id, is(notNullValue()));
        assertThat(data.birth, is(equalTo(101L)));
    }

    @Test
    public void recordDeath(){
        PetData data = saveExampleData();
        data = given()
                .when()
                .body("{\"death\": 201}")
                .patch("/pets/{id}/death", data.id)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PetData.class);

        assertThat(data, is(notNullValue()));
        assertThat(data.id, is(notNullValue()));
        assertThat(data.death, is(equalTo(201L)));
    }

    @Test
    public void correctKind(){
        PetData data = saveExampleData();
        data = given()
                .when()
                .body("{\"kind\":{\"animalTypeId\":\"Dog\"}}")
                .patch("/pets/{id}/kind", data.id)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PetData.class);

        assertThat(data, is(notNullValue()));
        assertThat(data.id, is(notNullValue()));
        assertThat(data.kind.animalTypeId, is(equalTo("Dog")));
    }

    @Test
    public void changeOwner(){
        PetData data = saveExampleData();
        data = given()
                .when()
                .body("{\"owner\":{\"clientId\":\"Granger\"}}")
                .patch("/pets/{id}/owner", data.id)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PetData.class);

        assertThat(data, is(notNullValue()));
        assertThat(data.id, is(notNullValue()));
        assertThat(data.owner.clientId, is(equalTo("Granger")));
    }

    @Test
    public void saveAndFetchById(){
        PetData res = saveExampleData();
        final String id = res.id;
        res = given()
                .when()
                .get("/pets/{id}", id)
                .then()
                .statusCode(200)
                .extract()
                .body().as(PetData.class);

        assertThat(res, is(notNullValue()));
        assertThat(res.id, is(equalTo(id)));
        assertThat(res.name, is(notNullValue()));
        assertThat(res.name.value, is(equalTo("Hedwig")));
        assertThat(res.birth, is(equalTo(100L)));
        assertThat(res.death, is(equalTo(0L)));
        assertThat(res.kind, is(notNullValue()));
        assertThat(res.kind.animalTypeId, is(equalTo("Owl")));
        assertThat(res.owner, is(notNullValue()));
        assertThat(res.owner.clientId, is(equalTo("Potter")));
        assertThat(res.visit, is(nullValue()));
    }

    @Test
    public void saveAndFetchAll(){
        final PetData animalTypeData = saveExampleData();
        final String id = animalTypeData.id;

        List<PetData> pets = given()
                .when()
                .get("/pets")
                .then()
                .statusCode(200)
                .extract()
                .body().as(new TypeRef<List<PetData>>() {});

        assertThat(pets, is(notNullValue()));
        assertThat(pets.size(), is(1));
        assertThat(pets.get(0).id, is(id));
        assertThat(pets.get(0).name, is(notNullValue()));
        assertThat(pets.get(0).name.value, is(equalTo("Hedwig")));
        assertThat(pets.get(0).birth, is(equalTo(100L)));
        assertThat(pets.get(0).death, is(equalTo(0L)));
        assertThat(pets.get(0).kind, is(notNullValue()));
        assertThat(pets.get(0).kind.animalTypeId, is(equalTo("Owl")));
        assertThat(pets.get(0).owner, is(notNullValue()));
        assertThat(pets.get(0).owner.clientId, is(equalTo("Potter")));
        assertThat(pets.get(0).visit, is(nullValue()));
    }

    @Test @Disabled("not implemented")
    public void saveAndFetchByOwner(){
        final PetData animalTypeData = saveExampleData();
        final String id = animalTypeData.id;
        final String ownerId = animalTypeData.owner.clientId;

        List<PetData> pets = given()
                .when()
                .get("/pets/owners/{id}", ownerId)
                .then()
                .statusCode(200)
                .extract()
                .body().as(new TypeRef<List<PetData>>() {});

        assertThat(pets, is(notNullValue()));
        assertThat(pets.size(), is(1));
        assertThat(pets.get(0).id, is(id));
        assertThat(pets.get(0).name, is(notNullValue()));
        assertThat(pets.get(0).name.value, is(equalTo("Hedwig")));
    }

}
