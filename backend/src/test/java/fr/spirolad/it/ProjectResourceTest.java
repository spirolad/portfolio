package fr.spirolad.it;

import fr.spirolad.dto.ProjectUploadRequest;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import io.restassured.http.ContentType;

import java.net.URI;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class ProjectResourceTest {

    @Test
    public void createProject_withValidDto_returnsCreated() {
        ProjectUploadRequest dto = new ProjectUploadRequest()
                .name("Portfolio Site")
                .description("A simple site")
                .link(URI.create("https://example.com"))
                .addTechnologiesItem("Java");

        given()
                .contentType(ContentType.JSON)
                .body(dto)
        .when()
                .post("/api/projects")
        .then()
                .statusCode(201)
                .body("name", is("Portfolio Site"))
                .body("id", notNullValue());
    }

    @Test
    public void createProject_withEmptyDto_returnsBadRequest() {
        String empty = "{}";

        given()
                .contentType(ContentType.JSON)
                .body(empty)
        .when()
                .post("/api/projects")
        .then()
                .statusCode(400)
                .body("error", notNullValue());
    }

    @Test
    public void createProject_withInvalidLink_returnsBadRequest() {
        String body = "{\"name\":\"X\",\"link\":\"not-a-url\"}";

        given()
                .contentType(ContentType.JSON)
                .body(body)
        .when()
                .post("/api/projects")
        .then()
                .statusCode(400)
                .body("error", notNullValue());
    }

    @Test
    public void getProject_notFound_returns404() {
        given()
        .when()
                .get("/api/projects/{id}", 999999)
        .then()
                .statusCode(404)
                .body("error", notNullValue());
    }

}
