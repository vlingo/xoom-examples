package io.vlingo.xoom.examples.petclinic;

import io.restassured.common.mapper.TypeRef;
import io.vlingo.xoom.examples.petclinic.infrastructure.SpecialtyTypeData;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SpecialtyTypeResourceTests extends AbstractRestTest {

    @Test
    public void testEmptyResponse() {
        given().when().get("/specialties").then().statusCode(200).body(is(equalTo("[]")));
    }

    @Test
    public void save() {
        SpecialtyTypeData res = given().when().body("{\"name\": \"Behaviour\"}").post("/specialties").then()
                .statusCode(201).extract().body().as(SpecialtyTypeData.class);

        assertThat(res, is(notNullValue()));
        assertThat(res.id, is(notNullValue()));
        assertThat(res.name, is(equalTo("Behaviour")));
    }

    private SpecialtyTypeData saveExampleData() {
        return given().when().body("{\"name\": \"Behaviour\"}").post("/specialties").then().statusCode(201).extract()
                .body().as(SpecialtyTypeData.class);
    }

    @Test
    public void saveAndUpdate() {
        SpecialtyTypeData data = saveExampleData();
        data = given().when().body("{\"name\": \"Surgery\"}").patch("/specialties/{id}/name", data.id).then().statusCode(200)
                .extract().body().as(SpecialtyTypeData.class);

        assertThat(data, is(notNullValue()));
        assertThat(data.id, is(notNullValue()));
        assertThat(data.name, is(equalTo("Surgery")));
    }

    @Test
    @Disabled("not implemented")
    public void saveAndFetchById() {
        SpecialtyTypeData res = saveExampleData();
        final String id = res.id;

        res = given().when().get("/specialties/{id}", id).then().statusCode(200).extract().body()
                .as(SpecialtyTypeData.class);

        assertThat(res, is(notNullValue()));
        assertThat(res.id, is(equalTo(id)));
        assertThat(res.name, is(equalTo("Behaviour")));
    }

    @Test
    public void saveAndFetchAll() {
        final SpecialtyTypeData animalTypeData = saveExampleData();
        final String id = animalTypeData.id;

        List<SpecialtyTypeData> specialties = given().when().get("/specialties").then().statusCode(200).extract().body()
                .as(new TypeRef<List<SpecialtyTypeData>>() {
                });

        assertThat(specialties, is(notNullValue()));
        assertThat(specialties.size(), is(1));
        assertThat(specialties.get(0).id, is(id));
        assertThat(specialties.get(0).name, is("Behaviour"));
    }

}
