package fr.spirolad.it;

import fr.spirolad.dto.CategoryRequest;
import fr.spirolad.dto.CategoryResponse;
import fr.spirolad.dto.SkillRequest;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
public class SkillResourceTest {

    private CategoryResponse createCategory(String name) {
        CategoryRequest dto = new CategoryRequest()
                .name(name);

        Integer id = given()
                .contentType(ContentType.JSON)
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
                .category(category);
    }

    @Test
    public void createSkillCategory_withDto_returnsCreated() {
        CategoryRequest dto = new CategoryRequest()
                .name("Languages");

        given()
                .contentType(ContentType.JSON)
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

        Integer id = given()
                .contentType(ContentType.JSON)
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

        given()
        .when()
                .delete("/api/skills/{id}", id)
        .then()
                .statusCode(204);

        given()
        .when()
                .delete("/api/skills/categories/{id}", category.getId())
        .then()
                .statusCode(204);
    }

    @Test
    public void getSkills_afterCreate_returnsList() {
        CategoryResponse category = createCategory("Frontend");
        Integer id = given()
                .contentType(ContentType.JSON)
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

        given()
        .when()
                .delete("/api/skills/{id}", id)
        .then()
                .statusCode(204);

        given()
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

        given()
                .contentType(ContentType.JSON)
                .body(dto)
        .when()
                .put("/api/skills/{id}", 999997)
        .then()
                .statusCode(404)
                .body("error", notNullValue());

        given()
        .when()
                .delete("/api/skills/categories/{id}", category.getId())
        .then()
                .statusCode(204);
    }

    @Test
    public void createWithEmptyDto_returnsBadRequest() {
        given()
                .contentType(ContentType.JSON)
                .body("{}")
        .when()
                .post("/api/skills")
        .then()
                .statusCode(400)
                .body("error", notNullValue());
    }

    @Test
    public void createCategoryWithEmptyDto_returnsBadRequest() {
        given()
                .contentType(ContentType.JSON)
                .body("{}")
        .when()
                .post("/api/skills/categories")
        .then()
                .statusCode(400)
                .body("error", notNullValue());
    }
}