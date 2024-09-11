package gorest.api;

import com.solvd.dto.CreateUser;
import com.solvd.service.UserCreator;
import com.solvd.utilis.ConfigLoader;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UserTests {

    @BeforeTest
    public void setUrl() {
        RestAssured.baseURI = ConfigLoader.getProperty("url");
    }

    @Test
    public void createUser() {
        //Creating random user to add using HTTP POST request
        CreateUser createUser = UserCreator.createUser();

        //Sending request, and assert body, status, and header
        given()
                .auth().oauth2(ConfigLoader.getProperty("token"))
                .contentType(ContentType.JSON)
                .body(createUser)
                .when()
                .post()
                .then()
                .body("email", equalTo(createUser.getEmail()))
                .body("name", equalTo(createUser.getName()))
                .body("status", equalTo(createUser.getStatus().getStatus()))
                .body("gender", equalTo(createUser.getGender().getGender()))
                .header("Content-Type", equalTo ("application/json; charset=utf-8"))
                .header("Server", equalTo("cloudflare"))
                .statusCode(201);
    }

}
