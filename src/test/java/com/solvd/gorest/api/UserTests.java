package com.solvd.gorest.api;

import com.solvd.dto.UserDTO;
import com.solvd.model.Status;
import com.solvd.model.User;
import com.solvd.service.UserCreator;
import com.solvd.utilis.ConfigLoader;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(com.solvd.gorest.graphql.UserTests.class);

    @BeforeTest
    public void setUrl() {
        RestAssured.baseURI = ConfigLoader.getProperty("url");
    }

    @Test
    public void createUser() {
        LOGGER.info("Creating random User");
        UserDTO userDTO = UserCreator.createUser();
        LOGGER.info("User created: {}", userDTO);

        LOGGER.info("Sending {} request", "Create User");
        given()
                .auth().oauth2(ConfigLoader.getProperty("token"))
                .contentType(ContentType.JSON)
                .body(userDTO)
                .when()
                .post()
                .then()
                .statusCode(201)
                .body("email", equalTo(userDTO.getEmail()))
                .body("name", equalTo(userDTO.getName()))
                .body("status", equalTo(userDTO.getStatus().getStatus()))
                .body("gender", equalTo(userDTO.getGender().getGender()))
                .header("Content-Type", equalTo ("application/json; charset=utf-8"))
                .header("Server", equalTo("cloudflare"));
        LOGGER.info("Test Passed");
    }

    @Test
    public void getUserById() {
        LOGGER.info("Creating random User");
        User user = UserCreator.saveUser();
        LOGGER.info("User created: {}", user);

        LOGGER.info("Sending {} request", "Get User by Id");
        given()
                .auth().oauth2(ConfigLoader.getProperty("token"))
                .contentType(ContentType.JSON)
                .when()
                .get("/" + user.id())
                .then()
                .statusCode(200)
                .body("id", equalTo(user.id().intValue()))
                .body("email", equalTo(user.email()))
                .body("name", equalTo(user.name()))
                .body("gender", equalTo(user.gender().getGender()))
                .body("status", equalTo(user.status().getStatus()))
                .header("Content-Type", equalTo ("application/json; charset=utf-8"))
                .header("Server", equalTo("cloudflare"));
        LOGGER.info("Test Passed");
    }

    @Test
    public void updatedUser() {
        LOGGER.info("Creating random User");
        User user = UserCreator.saveUser();
        LOGGER.info("User created: {}", user);

        LOGGER.info("Creating random User to use it's fields as an update request");
        UserDTO newUser = UserCreator.createUser();
        LOGGER.info("User created: {}", newUser);

        LOGGER.info("Sending {} request", "Update User");
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
        LOGGER.info("Test Passed");
    }

    @Test
    public void deleteUser() {
        LOGGER.info("Creating random User");
        User user = UserCreator.saveUser();
        LOGGER.info("User created: {}", user);

        LOGGER.info("Sending {} request", "Delete User");
        given()
                .auth().oauth2(ConfigLoader.getProperty("token"))
                .delete("/" + user.id())
                .then()
                .statusCode(204)
                .header("Server", equalTo("cloudflare"));
        LOGGER.info("Test Passed");
    }

    @Test
    public void getAllUsers() {
        LOGGER.info("Sending {} request", "Get All Users");
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
        LOGGER.info("Test Passed");
    }

    @Test
    public void createUserWithoutEmail() {
        LOGGER.info("Creating random User");
        UserDTO userDTO = UserCreator.createUser();
        LOGGER.info("User created: {}", userDTO);

        LOGGER.info("User email set to null");
        userDTO.setEmail(null);

        LOGGER.info("Sending {} request", "Create User without Enail");
        given()
                .auth().oauth2(ConfigLoader.getProperty("token"))
                .contentType(ContentType.JSON)
                .body(userDTO)
                .post()
                .then()
                .statusCode(422)
                .body("[0].field", equalTo("email"))
                .body("[0].message", equalTo("can't be blank"))
                .header("Content-Type", equalTo ("application/json; charset=utf-8"))
                .header("Server", equalTo("cloudflare"));
        LOGGER.info("Test Passed");
    }

    @Test
    public void createUserWithExistingEmail() {
        LOGGER.info("Creating random User");
        User user = UserCreator.saveUser();
        LOGGER.info("User created: {}", user);

        LOGGER.info("Creating new User to set an existing email");
        UserDTO userDTO = UserCreator.createUser();
        LOGGER.info("User created: {}", userDTO);

        LOGGER.info("Setting an existing email");
        userDTO.setEmail(user.email());
        LOGGER.info("Email in user set: {}", userDTO);

        LOGGER.info("Sending {} request", "Create User with Exisiting Email");
        given()
                .auth().oauth2(ConfigLoader.getProperty("token"))
                .contentType(ContentType.JSON)
                .body(userDTO)
                .post()
                .then()
                .statusCode(422)
                .body("[0].field", equalTo("email"))
                .body("[0].message", equalTo("has already been taken"))
                .header("Content-Type", equalTo ("application/json; charset=utf-8"))
                .header("Server", equalTo("cloudflare"));
        LOGGER.info("Test Passed");
    }

    @Test
    public void deleteUserWithNonExistingID() {
        LOGGER.info("Sending {} request", "Delete User with non exisiting id");
        given()
                .auth().oauth2(ConfigLoader.getProperty("token"))
                .delete("/99999")
                .then()
                .statusCode(404)
                .body("message", equalTo("Resource not found"))
                .header("Content-Type", equalTo ("application/json; charset=utf-8"))
                .header("Server", equalTo("cloudflare"));
        LOGGER.info("Test Passed");
    }

    @Test
    public void tryGetDeletedUser() {
        LOGGER.info("Creating random User");
        User user = UserCreator.saveUser();
        LOGGER.info("User created: {}", user);

        LOGGER.info("Sending {} request", "Delete User");
        given()
                .auth().oauth2(ConfigLoader.getProperty("token"))
                .delete("/" + user.id())
                .then()
                .statusCode(204)
                .header("Server", equalTo("cloudflare"));

        LOGGER.info("Sending {} request", "try to get Deleted User");
        given()
                .auth().oauth2(ConfigLoader.getProperty("token"))
                .get("/" + user.id())
                .then()
                .statusCode(404)
                .body("message", equalTo("Resource not found"))
                .header("Content-Type", equalTo ("application/json; charset=utf-8"))
                .header("Server", equalTo("cloudflare"));
        LOGGER.info("Test Passed");
    }

    @Test
    public void updateUserStatus() {
        LOGGER.info("Creating random User");
        User user = UserCreator.saveUser();
        LOGGER.info("User created: {}", user);

        LOGGER.info("Creating new Status");
        Status newStatus;
        if (user.status().getStatus().equals("active")) {
            newStatus = Status.INACTIVE;
        } else {
            newStatus = Status.ACTIVE;
        }
        LOGGER.info("New Status Created: {}", newStatus.getStatus());

        LOGGER.info("Creating new User with updated status");
        UserDTO userDTO = UserDTO.builder().name(user.name())
                .email(user.email())
                .gender(user.gender())
                .status(newStatus)
                .build();
        LOGGER.info("Created User with new Status: {}", userDTO);

        LOGGER.info("Sending {} request", "updated Status in User");
        given()
                .auth().oauth2(ConfigLoader.getProperty("token"))
                .contentType(ContentType.JSON)
                .body(userDTO)
                .put("/" + user.id())
                .then()
                .statusCode(200)
                .body("email", equalTo(user.email()))
                .body("name", equalTo(user.name()))
                .body("status", equalTo(newStatus.getStatus()))
                .body("gender", equalTo(user.gender().getGender()))
                .header("Content-Type", equalTo ("application/json; charset=utf-8"))
                .header("Server", equalTo("cloudflare"));
        LOGGER.info("Test Passed");
    }
}
