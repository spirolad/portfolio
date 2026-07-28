package fr.spirolad.it;

import fr.spirolad.dto.ProjectUploadRequest;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class ProjectResourceTest extends AuthenticatedIntegrationTest {

    private ProjectUploadRequest buildValidProjectRequest() {
        return new ProjectUploadRequest()
                .name("Portfolio Site")
                .description("A simple site")
                .link(URI.create("https://example.com"))
                .addTechnologiesItem("Java");
    }

    @Test
    public void createProject_withValidDto_returnsCreated() {
        ProjectUploadRequest dto = buildValidProjectRequest();

        authenticatedJsonRequest()
                .body(dto)
        .when()
                .post("/api/projects")
        .then()
                .statusCode(201)
                .body("name", is("Portfolio Site"))
                .body("id", notNullValue());
    }

    @Test
    public void getProjects_afterCreate_returnsList() {
        Integer id = authenticatedJsonRequest()
                .body(buildValidProjectRequest())
        .when()
                .post("/api/projects")
        .then()
                .statusCode(201)
                .extract().path("id");

        given()
        .when()
                .get("/api/projects")
        .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1))
                .body("find { it.id == " + id + " }.name", is("Portfolio Site"));

        authenticatedRequest()
        .when()
                .delete("/api/projects/{id}", id)
        .then()
                .statusCode(204);
    }

    @Test
    public void createProject_withEmptyDto_returnsBadRequest() {
        String empty = "{}";

        authenticatedJsonRequest()
                .body(empty)
        .when()
                .post("/api/projects")
        .then()
                .statusCode(400)
                .body("error", notNullValue());
    }

    @Test
    public void updateProject_notFound_returns404() {
        authenticatedJsonRequest()
                .body(buildValidProjectRequest())
        .when()
                .put("/api/projects/{id}", 999998)
        .then()
                .statusCode(404)
                .body("error", notNullValue());
    }

    @Test
    public void deleteProject_notFound_returns404() {
        authenticatedRequest()
        .when()
                .delete("/api/projects/{id}", 999997)
        .then()
                .statusCode(404)
                .body("error", notNullValue());
    }

    @Test
    public void createProject_withInvalidLink_returnsBadRequest() {
        String body = "{\"name\":\"X\",\"link\":\"not-a-url\"}";

        authenticatedJsonRequest()
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
