package fr.spirolad.it;

import fr.spirolad.dto.PortfolioRequest;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class PortfolioResourceTest {

    @Test
    public void updateAndGetPortfolio_returnsUpdatedProfile() {
        PortfolioRequest req = new PortfolioRequest()
                .name("Alice")
                .email("alice@example.com")
                .currentPosition("Developer");

        given()
                .contentType(ContentType.JSON)
                .body(req)
        .when()
                .put("/api/portfolio")
        .then()
                .statusCode(200)
                .body("generalInfo.name", is("Alice"))
                .body("generalInfo.email", is("alice@example.com"));

        given()
        .when()
                .get("/api/portfolio")
        .then()
                .statusCode(200)
                .body("generalInfo.name", is("Alice"));
    }

    @Test
    public void getPortfolio_returns200_andBody() {
        given()
        .when()
                .get("/api/portfolio")
        .then()
                .statusCode(200)
                .body(notNullValue());
    }

}
