package com.javablog;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
class NewsletterResourceTest {

    @Test
    void registerNewsletter_returnsConfirmation() {
        given()
                .contentType("application/json")
                .body("""
                        {
                            "email": "test@example.com",
                            "token": "test-token"
                        }
                        """)
                .when()
                .post("/v1/newsletter")
                .then()
                .statusCode(200)
                .body("message", notNullValue());
    }
}
