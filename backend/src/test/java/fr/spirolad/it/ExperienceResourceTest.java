package fr.spirolad.it;

import fr.spirolad.dto.ExperienceRequest;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import io.restassured.http.ContentType;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class ExperienceResourceTest {

    private ExperienceRequest buildValidExperienceRequest() {
        return new ExperienceRequest()
                .company("Acme")
                .position("Engineer")
                .startDate(LocalDate.parse("2020-01-01"))
                .endDate(LocalDate.parse("2022-01-01"));
    }

    @Test
    public void createExperience_withDto_returnsCreated() {
        given()
                .contentType(ContentType.JSON)
                .body(buildValidExperienceRequest())
        .when()
                .post("/api/experiences")
        .then()
                .statusCode(201)
                .body("company", is("Acme"))
                .body("position", is("Engineer"))
                .body("id", notNullValue());
    }

    @Test
    public void getExperienceById_afterCreate_returnsCreatedExperience() {
        Integer id = given()
                .contentType(ContentType.JSON)
                .body(buildValidExperienceRequest())
        .when()
                .post("/api/experiences")
        .then()
                .statusCode(201)
                .extract().path("id");

        given()
        .when()
                .get("/api/experiences/{id}", id)
        .then()
                .statusCode(200)
                .body("id", is(id.intValue()))
                .body("company", is("Acme"))
                .body("position", is("Engineer"));
    }

    @Test
    public void getExperiences_afterCreate_returnsList() {
        Integer id = given()
                .contentType(ContentType.JSON)
                .body(buildValidExperienceRequest())
        .when()
                .post("/api/experiences")
        .then()
                .statusCode(201)
                .extract().path("id");

        given()
        .when()
                .get("/api/experiences")
        .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1))
                .body("find { it.id == " + id + " }.company", is("Acme"))
                .body("find { it.id == " + id + " }.position", is("Engineer"));
    }

    @Test
    public void getExperienceById_notFound_returns404() {
        given()
        .when()
                .get("/api/experiences/{id}", 999999)
        .then()
                .statusCode(404)
                .body("error", notNullValue());
    }

    @Test
    public void updateExperience_afterCreate_returnsUpdatedExperience() {
        Integer id = given()
                .contentType(ContentType.JSON)
                .body(buildValidExperienceRequest())
        .when()
                .post("/api/experiences")
        .then()
                .statusCode(201)
                .extract().path("id");

        ExperienceRequest updated = new ExperienceRequest()
                .company("Acme Updated")
                .position("Senior Engineer")
                .startDate(LocalDate.parse("2021-01-01"))
                .endDate(LocalDate.parse("2023-01-01"));

        given()
                .contentType(ContentType.JSON)
                .body(updated)
        .when()
                .put("/api/experiences/{id}", id)
        .then()
                .statusCode(200)
                .body("id", is(id.intValue()))
                .body("company", is("Acme Updated"))
                .body("position", is("Senior Engineer"));
    }

    @Test
    public void updateExperience_notFound_returns404() {
        ExperienceRequest updated = new ExperienceRequest()
                .company("Acme Updated")
                .position("Senior Engineer")
                .startDate(LocalDate.parse("2021-01-01"))
                .endDate(LocalDate.parse("2023-01-01"));

        given()
                .contentType(ContentType.JSON)
                .body(updated)
        .when()
                .put("/api/experiences/{id}", 999998)
        .then()
                .statusCode(404)
                .body("error", notNullValue());
    }

    @Test
    public void deleteExperience_afterCreate_returnsNoContent() {
        Integer id = given()
                .contentType(ContentType.JSON)
                .body(buildValidExperienceRequest())
        .when()
                .post("/api/experiences")
        .then()
                .statusCode(201)
                .extract().path("id");

        given()
        .when()
                .delete("/api/experiences/{id}", id)
        .then()
                .statusCode(204);

        given()
        .when()
                .get("/api/experiences/{id}", id)
        .then()
                .statusCode(404)
                .body("error", notNullValue());
    }

    @Test
    public void deleteExperience_notFound_returns404() {
        given()
        .when()
                .delete("/api/experiences/{id}", 999997)
        .then()
                .statusCode(404)
                .body("error", notNullValue());
    }

    @Test
    public void createWithEndDateBeforeStart_returnsBadRequest() {
        ExperienceRequest dto = new ExperienceRequest()
                .company("Acme")
                .position("Engineer")
                .startDate(LocalDate.parse("2022-01-01"))
                .endDate(LocalDate.parse("2020-01-01"));

        given()
                .contentType(ContentType.JSON)
                .body(dto)
        .when()
                .post("/api/experiences")
        .then()
                .statusCode(400)
                .body("error", notNullValue());
    }
}
