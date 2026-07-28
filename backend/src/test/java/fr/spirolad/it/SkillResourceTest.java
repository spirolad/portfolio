package fr.spirolad.it;

import fr.spirolad.dto.CategoryRequest;
import fr.spirolad.dto.CategoryResponse;
import fr.spirolad.dto.SkillRequest;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
public class SkillResourceTest extends AuthenticatedIntegrationTest {

    private CategoryResponse createCategory(String name) {
        CategoryRequest dto = new CategoryRequest()
                .name(name);

        Integer id = authenticatedJsonRequest()
                .body(dto)
        .when()
                .post("/api/skills/categories")
        .then()
                .extract().path("id");

        return new CategoryResponse()
                .id(id.longValue())
                .name(name);
    }

    private SkillRequest createSkillRequest(CategoryResponse category) {
        return new SkillRequest()
                .name("Java")
                .categoryId(category.getId());
    }

    @Test
    public void createSkillCategory_withDto_returnsCreated() {
        CategoryRequest dto = new CategoryRequest()
                .name("Languages");

        authenticatedJsonRequest()
                .body(dto)
        .when()
                .post("/api/skills/categories")
        .then()
                .body("name", is("Languages"))
                .body("id", notNullValue());
    }

    @Test
    public void getSkillCategory_notFound_returns404() {
        given()
        .when()
                .get("/api/skills/categories/{id}", 999999)
        .then()
                .statusCode(404)
                .body("error", notNullValue());
    }

    @Test
    public void createSkill_withCategoryReference_returnsCreated() {
        CategoryResponse category = createCategory("Backend");
        SkillRequest dto = createSkillRequest(category);

        Integer id = authenticatedJsonRequest()
                .body(dto)
        .when()
                .post("/api/skills")
        .then()
                .statusCode(201)
                .body("name", is("Java"))
                .body("category.id", is(category.getId().intValue()))
                .body("category.name", is("Backend"))
                .extract().path("id");

        given()
        .when()
                .get("/api/skills/{id}", id)
        .then()
                .statusCode(200)
                .body("name", is("Java"))
                .body("category.id", is(category.getId().intValue()));

        authenticatedRequest()
        .when()
                .delete("/api/skills/{id}", id)
        .then()
                .statusCode(204);

        authenticatedRequest()
        .when()
                .delete("/api/skills/categories/{id}", category.getId())
        .then()
                .statusCode(204);
    }

    @Test
    public void getSkills_afterCreate_returnsList() {
        CategoryResponse category = createCategory("Frontend");
        Integer id = authenticatedJsonRequest()
                .body(createSkillRequest(category))
        .when()
                .post("/api/skills")
        .then()
                .statusCode(201)
                .extract().path("id");

        given()
        .when()
                .get("/api/skills")
        .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1))
                .body("find { it.id == " + id + " }.name", is("Java"));

        authenticatedRequest()
        .when()
                .delete("/api/skills/{id}", id)
        .then()
                .statusCode(204);

        authenticatedRequest()
        .when()
                .delete("/api/skills/categories/{id}", category.getId())
        .then()
                .statusCode(204);
    }

    @Test
    public void getSkill_notFound_returns404() {
        given()
        .when()
                .get("/api/skills/{id}", 999998)
        .then()
                .statusCode(404)
                .body("error", notNullValue());
    }

    @Test
    public void updateSkill_notFound_returns404() {
        CategoryResponse category = createCategory("DevOps");
        SkillRequest dto = createSkillRequest(category);

        authenticatedJsonRequest()
                .body(dto)
        .when()
                .put("/api/skills/{id}", 999997)
        .then()
                .statusCode(404)
                .body("error", notNullValue());

        authenticatedRequest()
        .when()
                .delete("/api/skills/categories/{id}", category.getId())
        .then()
                .statusCode(204);
    }

    @Test
    public void createWithEmptyDto_returnsBadRequest() {
        authenticatedJsonRequest()
                .body("{}")
        .when()
                .post("/api/skills")
        .then()
                .statusCode(400)
                .body("error", notNullValue());
    }

    @Test
    public void createSkill_withNullCategoryId_returnsBadRequest() {
        String body = "{\"name\":\"Java\",\"categoryId\":null}";

        authenticatedJsonRequest()
                .body(body)
        .when()
                .post("/api/skills")
        .then()
                .statusCode(400)
                .body("error", notNullValue());
    }

    @Test
    public void updateSkill_withUnknownCategoryId_returnsNotFound() {
        String body = "{\"name\":\"Java\",\"categoryId\":999999}";

        authenticatedJsonRequest()
                .body(body)
        .when()
                .put("/api/skills/{id}", 1)
        .then()
                .statusCode(404)
                .body("error", notNullValue());
    }

    @Test
    public void createCategoryWithEmptyDto_returnsBadRequest() {
        authenticatedJsonRequest()
                .body("{}")
        .when()
                .post("/api/skills/categories")
        .then()
                .statusCode(400)
                .body("error", notNullValue());
    }
}
