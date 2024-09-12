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
import static org.hamcrest.Matchers.*;

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

        //Create new user to use its fields to replace current ones
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

    @Test
    public void deleteUser() {
        //Adding user
        User user = UserCreator.saveUser();

        //Deleting user
        given()
                .auth().oauth2(ConfigLoader.getProperty("token"))
                .delete("/" + user.id())
                .then()
                .statusCode(204)
                .header("Server", equalTo("cloudflare"));
    }

    @Test
    public void getAllUsers() {
        //Get Users and check if all fields are not null, along with status code, and headers
        given()
                .auth().oauth2(ConfigLoader.getProperty("token"))
                .get()
                .then()
                .statusCode(200)
                .body("id", everyItem(notNullValue()))
                .body("name", everyItem(notNullValue())).body("email", everyItem(notNullValue()))
                .body("gender", everyItem(notNullValue())).body("status", everyItem(notNullValue()))
                .header("Content-Type", equalTo ("application/json; charset=utf-8"))
                .header("Server", equalTo("cloudflare"));
    }

    @Test
    public void createUserWithoutEmail() {
        //Creating random user
        CreateUser createUser = UserCreator.createUser();
        //Set an email to null
        createUser.setEmail(null);

        //Try to add the user with an empty email, and assert there will be an error
        given()
                .auth().oauth2(ConfigLoader.getProperty("token"))
                .contentType(ContentType.JSON)
                .body(createUser)
                .post()
                .then()
                .statusCode(422)
                .body("[0].field", equalTo("email"))
                .body("[0].message", equalTo("can't be blank"))
                .header("Content-Type", equalTo ("application/json; charset=utf-8"))
                .header("Server", equalTo("cloudflare"));
    }

    @Test
    public void createUserWithExistingEmail() {
        //Creating and adding first user
        User user = UserCreator.saveUser();

        //Create new user
        CreateUser createUser = UserCreator.createUser();
        //Set an existing email
        createUser.setEmail(user.email());

        //Assert that user will not be added
        given()
                .auth().oauth2(ConfigLoader.getProperty("token"))
                .contentType(ContentType.JSON)
                .body(createUser)
                .post()
                .then()
                .statusCode(422)
                .body("[0].field", equalTo("email"))
                .body("[0].message", equalTo("has already been taken"))
                .header("Content-Type", equalTo ("application/json; charset=utf-8"))
                .header("Server", equalTo("cloudflare"));
    }

    @Test
    public void deleteUserWithNonExistingID() {
        given()
                .auth().oauth2(ConfigLoader.getProperty("token"))
                .delete("/99999")
                .then()
                .statusCode(404)
                .body("message", equalTo("Resource not found"))
                .header("Content-Type", equalTo ("application/json; charset=utf-8"))
                .header("Server", equalTo("cloudflare"));
    }

    @Test
    public void tryGetDeletedUser() {
        //Adding user
        User user = UserCreator.saveUser();

        //Deleting User
        given()
                .auth().oauth2(ConfigLoader.getProperty("token"))
                .delete("/" + user.id())
                .then()
                .statusCode(204)
                .header("Server", equalTo("cloudflare"));

        given()
                .auth().oauth2(ConfigLoader.getProperty("token"))
                .get("/" + user.id())
                .then()
                .statusCode(404)
                .body("message", equalTo("Resource not found"))
                .header("Content-Type", equalTo ("application/json; charset=utf-8"))
                .header("Server", equalTo("cloudflare"));
    }
}
