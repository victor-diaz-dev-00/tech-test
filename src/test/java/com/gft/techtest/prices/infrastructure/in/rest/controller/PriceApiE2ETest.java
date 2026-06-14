package com.gft.techtest.prices.infrastructure.in.rest.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PriceApiE2ETest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Test
    void shouldReturnPriceForJune14AtTen() {
        given()
                .queryParam("date", "2020-06-14T10:00:00Z")
                .queryParam("productId", 35455)
                .queryParam("brandId", 1)
        .when()
                .get("/prices")
        .then()
                .statusCode(200)
                .body("productId", equalTo(35455))
                .body("brandId", equalTo(1))
                .body("priceList", equalTo(1))
                .body("price", equalTo(35.50f))
                .body("currency", equalTo("EUR"));
    }

    @Test
    void shouldReturnPriceForJune14AtSixteen() {
        given()
                .queryParam("date", "2020-06-14T16:00:00Z")
                .queryParam("productId", 35455)
                .queryParam("brandId", 1)
        .when()
                .get("/prices")
        .then()
                .statusCode(200)
                .body("productId", equalTo(35455))
                .body("brandId", equalTo(1))
                .body("priceList", equalTo(2))
                .body("price", equalTo(25.45f))
                .body("currency", equalTo("EUR"));
    }

    @Test
    void shouldReturnPriceForJune14AtTwentyOne() {
        given()
                .queryParam("date", "2020-06-14T21:00:00Z")
                .queryParam("productId", 35455)
                .queryParam("brandId", 1)
        .when()
                .get("/prices")
        .then()
                .statusCode(200)
                .body("productId", equalTo(35455))
                .body("brandId", equalTo(1))
                .body("priceList", equalTo(1))
                .body("price", equalTo(35.50f))
                .body("currency", equalTo("EUR"));
    }

    @Test
    void shouldReturnPriceForJune15AtTen() {
        given()
                .queryParam("date", "2020-06-15T10:00:00Z")
                .queryParam("productId", 35455)
                .queryParam("brandId", 1)
        .when()
                .get("/prices")
        .then()
                .statusCode(200)
                .body("productId", equalTo(35455))
                .body("brandId", equalTo(1))
                .body("priceList", equalTo(3))
                .body("price", equalTo(30.50f))
                .body("currency", equalTo("EUR"));
    }

    @ParameterizedTest(name = "Should return Price List {1} for date {0}")
    @CsvSource({
            "2020-06-14T17:00:00Z,      2, 25.45",
            "2020-06-14T17:00:00+05:00, 1, 35.50"
    })
    void shouldReturnCorrectPriceBasedOnTimezoneNormalization(String inputDate, int expectedPriceList, float expectedPrice) {
        given()
                .queryParam("date", inputDate)
                .queryParam("productId", 35455)
                .queryParam("brandId", 1)
                .when()
                .get("/prices")
                .then()
                .statusCode(200)
                .body("priceList", equalTo(expectedPriceList))
                .body("price", equalTo(expectedPrice));
    }

    @Test
    void shouldReturnConflictForDuplicatePriority() {
        given()
                .queryParam("date", "2020-12-15T10:00:00Z")
                .queryParam("productId", 354)
                .queryParam("brandId", 2)
        .when()
                .get("/prices")
        .then()
                .statusCode(409)
                .body("status", equalTo(409))
                .body("path", equalTo("/prices"));
    }

    @Test
    void shouldReturnNotFoundWhenProductDoesNotExist() {
        given()
                .queryParam("date", "2020-06-14T10:00:00Z")
                .queryParam("productId", 99999)
                .queryParam("brandId", 1)
                .when()
                .get("/prices")
                .then()
                .statusCode(404)
                .body("status", equalTo(404))
                .body("message", equalTo("Product not found: 99999"));
    }

    @Test
    void shouldReturnNotFoundWhenBrandDoesNotExist() {
        given()
                .queryParam("date", "2020-06-14T10:00:00Z")
                .queryParam("productId", 35455)
                .queryParam("brandId", 99999)
                .when()
                .get("/prices")
                .then()
                .statusCode(404)
                .body("status", equalTo(404))
                .body("message", equalTo("Brand not found: 99999"));
    }

    @Test
    void shouldReturnNotFoundWhenNoPriceIsConfiguredForGivenDate() {
        given()
                .queryParam("date", "2040-01-01T10:00:00Z")
                .queryParam("productId", 35455)
                .queryParam("brandId", 1)
                .when()
                .get("/prices")
                .then()
                .statusCode(404)
                .body("status", equalTo(404))
                .body("message",
                        equalTo("Applicable price not found for brand: 1 and product: 35455 for date: 2040-01-01T10:00Z"));
    }
}
