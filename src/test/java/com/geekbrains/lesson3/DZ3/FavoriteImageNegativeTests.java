package com.geekbrains.lesson3.DZ3;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;


public class FavoriteImageNegativeTests extends BaseTest {
    String uploadedImageId;

    @BeforeEach
    void setUp() {
        uploadedImageId = given()
                .headers("Authorization", token)
                .multiPart("image", new File("src/test/resources/sad.jpg"))
                .multiPart("title", "Sad")
                .expect()
                .statusCode(200)
                .when()
                .post("https://api.imgur.com/3/image")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.id");
    }
    @Test //Некорректный id  -  No image data was sent to the upload api
    void FavoriteImageInvalidIdNegativeTest() {
        given()
                .headers("Authorization", token)
                .when()
                .post("https://api.imgur.com/3/image/{imageHash}/favorite", 0)
                .prettyPeek()
                .then()
                .statusCode(400);
    }
    @Test //Некорректный url  -  404 Not Found
    void FavoriteImageInvalidURLNegativeTest() {
        given()
                .headers("Authorization", token)
                .when()
                .post("https://api.imgur.com/3/image/favorite")
                .prettyPeek()
                .then()
                .statusCode(404);
    }
    @AfterEach
    void tearDown() {
        given()
                .headers("Authorization", token)
                .when()
                .delete("https://api.imgur.com/3/account/{username}/image/{deleteHash}", "nazilya", uploadedImageId)
                .prettyPeek()
                .then()
                .statusCode(200);
    }
}
