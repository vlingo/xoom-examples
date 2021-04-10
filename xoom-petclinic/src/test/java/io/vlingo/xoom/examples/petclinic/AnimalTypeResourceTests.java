package io.vlingo.xoom.examples.petclinic;

import io.restassured.common.mapper.TypeRef;
import io.vlingo.xoom.examples.petclinic.infrastructure.AnimalTypeData;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AnimalTypeResourceTests extends AbstractRestTest{

    @Test @Disabled("due to unknown bug")
    public void testEmptyResponse(){
        given()
                .when()
                .get("/animalTypes")
                .then()
                .statusCode(200)
                .body(is(equalTo("")));
    }

    @Test
    public void save(){
        AnimalTypeData res = given()
                .when()
                .body("{\"name\": \"Owl\"}")
                .post("/animalTypes")
                .then()
                .statusCode(201)
                .extract()
                .body()
                .as(AnimalTypeData.class);

        assertThat(res, is(notNullValue()));
        assertThat(res.id, is(notNullValue()));
        assertThat(res.name, is(equalTo("Owl")));
    }

    private AnimalTypeData saveExampleData(){
        return given()
                .when()
                .body("{\"name\": \"Owl\"}")
                .post("/animalTypes")
                .then()
                .statusCode(201)
                .extract()
                .body()
                .as(AnimalTypeData.class);
    }

    @Test
    public void saveAndUpdate(){
        AnimalTypeData animalTypeData = saveExampleData();
        animalTypeData = given()
                .when()
                .body("{\"name\": \"Dog\"}")
                .put("/animalTypes/{id}", animalTypeData.id)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(AnimalTypeData.class);

        assertThat(animalTypeData, is(notNullValue()));
        assertThat(animalTypeData.id, is(notNullValue()));
        assertThat(animalTypeData.name, is(equalTo("Dog")));
    }

    @Test
    public void saveAndFetchById(){
        AnimalTypeData res = saveExampleData();
        final String id = res.id;

        res = given()
                .when()
                .get("/animalTypes/{id}", id)
                .then()
                .statusCode(200)
                .extract()
                .body().as(AnimalTypeData.class);

        assertThat(res, is(notNullValue()));
        assertThat(res.id, is(equalTo(id)));
        assertThat(res.name, is(equalTo("Owl")));
    }

    @Test
    public void saveAndFetchAll(){
        final AnimalTypeData animalTypeData = saveExampleData();
        final String id = animalTypeData.id;

        List<AnimalTypeData> animalTypes = given()
                .when()
                .get("/animalTypes")
                .then()
                .statusCode(200)
                .extract()
                .body().as(new TypeRef<List<AnimalTypeData>>() {});

        assertThat(animalTypes, is(notNullValue()));
        assertThat(animalTypes.size(), is(1));
        assertThat(animalTypes.get(0).id, is(id));
        assertThat(animalTypes.get(0).name, is("Owl"));
    }

}
