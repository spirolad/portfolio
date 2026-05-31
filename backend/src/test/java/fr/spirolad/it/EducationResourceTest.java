package fr.spirolad.it;

import fr.spirolad.dto.EducationRequest;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import io.restassured.http.ContentType;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class EducationResourceTest {

    @Test
    public void createEducation_withDto_returnsCreated() {
        EducationRequest dto = new EducationRequest()
                .institution("MIT")
                .degree("BS CS")
                .startDate(LocalDate.parse("2020-09-01"))
                .endDate(LocalDate.parse("2024-06-30"));

        given()
                .contentType(ContentType.JSON)
                .body(dto)
        .when()
                .post("/api/educations")
        .then()
                .statusCode(201)
                .body("institution", is("MIT"))
                .body("degree", is("BS CS"))
                .body("id", notNullValue());
    }

    @Test
    public void getEducation_notFound_returns404() {
        given()
        .when()
                .get("/api/educations/{id}", 999999)
        .then()
                .statusCode(404)
                .body("error", notNullValue());
    }

    @Test
    public void updateEducation_notFound_returns404() {
        EducationRequest dto = new EducationRequest()
                .institution("X")
                .degree("Y")
                .startDate(LocalDate.parse("2021-01-01"));

        given()
                .contentType(ContentType.JSON)
                .body(dto)
        .when()
                .put("/api/educations/{id}", 999997)
        .then()
                .statusCode(404)
                .body("error", notNullValue());
    }

    @Test
    public void getAllEducations_returnsList() {
        EducationRequest dto = new EducationRequest()
                .institution("LocalTest")
                .degree("Deg")
                .startDate(LocalDate.parse("2020-01-01"));

        Integer id = given()
                .contentType(ContentType.JSON)
                .body(dto)
        .when()
                .post("/api/educations")
        .then()
                .statusCode(201)
                .extract().path("id");

        given()
        .when()
                .get("/api/educations")
        .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1));

        given()
        .when()
                .delete("/api/educations/{id}", id)
        .then()
                .statusCode(204);
    }

    @Test
    public void createWithEmptyDto_returnsBadRequest() {
        String empty = "{}";

        given()
                .contentType(ContentType.JSON)
                .body(empty)
        .when()
                .post("/api/educations")
        .then()
                .statusCode(400)
                .body("error", notNullValue());
    }

    @Test
    public void createWithEndDateBeforeStart_returnsBadRequest() {
        EducationRequest dto = new EducationRequest()
                .institution("MIT")
                .degree("BS CS")
                .startDate(LocalDate.parse("2022-01-01"))
                .endDate(LocalDate.parse("2020-01-01"));

        given()
                .contentType(ContentType.JSON)
                .body(dto)
        .when()
                .post("/api/educations")
        .then()
                .statusCode(400)
                .body("error", notNullValue());
    }

}
