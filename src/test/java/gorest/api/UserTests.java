package gorest.api;

import com.solvd.dto.CreateUser;
import com.solvd.model.User;
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
                .statusCode(201)
                .body("email", equalTo(createUser.getEmail()))
                .body("name", equalTo(createUser.getName()))
                .body("status", equalTo(createUser.getStatus().getStatus()))
                .body("gender", equalTo(createUser.getGender().getGender()))
                .header("Content-Type", equalTo ("application/json; charset=utf-8"))
                .header("Server", equalTo("cloudflare"));
    }

    @Test
    public void getUserById() {
        //Adding createUser
        User user = UserCreator.saveUser();

        //Getting added user along with assertion
        given()
                .auth().oauth2(ConfigLoader.getProperty("token"))
                .contentType(ContentType.JSON)
                .when()
                .get("/" + user.id())
                .then()
                .statusCode(200)
                .body("id", equalTo(user.id().intValue())).body("email", equalTo(user.email()))
                .body("name", equalTo(user.name())).body("gender", equalTo(user.gender().getGender()))
                .body("status", equalTo(user.status().getStatus()))
                .header("Content-Type", equalTo ("application/json; charset=utf-8"))
                .header("Server", equalTo("cloudflare"));
    }

    @Test
    public void updatedUser() {
        //Adding user
        User user = UserCreator.saveUser();

        CreateUser newUser = UserCreator.createUser();

        given()
                .auth().oauth2(ConfigLoader.getProperty("token"))
                .contentType(ContentType.JSON)
                .body(newUser)
                .when()
                .put("/" + user.id())
                .then()
                .statusCode(200)
                .body("email", equalTo(newUser.getEmail()))
                .body("name", equalTo(newUser.getName()))
                .body("status", equalTo(newUser.getStatus().getStatus()))
                .body("gender", equalTo(newUser.getGender().getGender()))
                .header("Content-Type", equalTo ("application/json; charset=utf-8"))
                .header("Server", equalTo("cloudflare"));

    }

}
