package com.javablog;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
class TokenResourceTest {

    @Test
    void generateToken_returnsTokenResponse() {
        given()
                .when()
                .post("/v1/token")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .body("expiresIn", notNullValue());
    }
}
