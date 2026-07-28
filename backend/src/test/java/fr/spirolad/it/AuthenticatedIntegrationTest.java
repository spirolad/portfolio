package fr.spirolad.it;

import fr.spirolad.dto.LoginRequest;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

abstract class AuthenticatedIntegrationTest {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    private String adminToken;

    protected RequestSpecification authenticatedRequest() {
        return given().header("Authorization", "Bearer " + token());
    }

    protected RequestSpecification authenticatedJsonRequest() {
        return authenticatedRequest().contentType(ContentType.JSON);
    }

    private String token() {
        if (adminToken == null) {
            LoginRequest request = new LoginRequest()
                    .username(ADMIN_USERNAME)
                    .password(ADMIN_PASSWORD);

            adminToken = given()
                    .contentType(ContentType.JSON)
                    .body(request)
            .when()
                    .post("/api/auth/login")
            .then()
                    .statusCode(200)
                    .extract()
                    .path("token");
        }

        return adminToken;
    }
}
