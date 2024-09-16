package com.solvd.gorest.graphql;

import com.solvd.dto.UserDTO;
import com.solvd.model.Gender;
import com.solvd.model.Status;
import com.solvd.model.User;
import com.solvd.service.UserCreator;
import com.solvd.utilis.ConfigLoader;
import com.solvd.utilis.QueryProvider;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UserTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserTests.class);

    @BeforeTest
    public void setUrl() {
        RestAssured.baseURI = ConfigLoader.getProperty("graphqlurl");
    }

    @Test
    public void createUser() {
        //noinspection LoggingSimilarMessage
        LOGGER.info("Creating random User");
        UserDTO userToAdd = UserCreator.createUser();
        LOGGER.info(String.format("User created: %s", userToAdd));

        LOGGER.info("Creating a query");
        String query = String.format(QueryProvider.CREATE_USER,
                userToAdd.getName(), userToAdd.getEmail(), userToAdd.getGender().getGender(), userToAdd.getStatus().getStatus());
        LOGGER.info("Query created: {}", query);

        LOGGER.info("Sending {} request, \nwith created query: {}", "Create User", query);
        given()
                .auth()
                .oauth2(ConfigLoader.getProperty("token"))
                .contentType(ContentType.JSON)
                .body(query)
                .when()
                .post()
                .then()
                .statusCode(200)
                .body("data.createUser.user.email", equalTo(userToAdd.getEmail()))
                .body("data.createUser.user.name", equalTo(userToAdd.getName()))
                .body("data.createUser.user.status", equalTo(userToAdd.getStatus().getStatus()))
                .body("data.createUser.user.gender", equalTo(userToAdd.getGender().getGender()))
                .header("Content-Type", equalTo ("application/json; charset=utf-8"))
                .header("Server", equalTo("cloudflare"));
        LOGGER.info("Test Passed");
    }

    @Test
    public void getUserById() {
        LOGGER.info("Creating random User");
        User user = UserCreator.saveGraphQLUser();
        LOGGER.info("User created: {}", user);

        LOGGER.info("Creating a query");
        String query = String.format(QueryProvider.GET_USER_BY_ID,
                user.id());
        LOGGER.info("Query created: {}", query);

        LOGGER.info("Sending {} request, \nwith created query: {}", "Get User by Id", query);
        given()
                .auth().oauth2(ConfigLoader.getProperty("token"))
                .contentType(ContentType.JSON)
                .body(query)
                .post()
                .then()
                .statusCode(200)
                .body("data.user.id", equalTo(user.id().intValue()))
                .body("data.user.email", equalTo(user.email()))
                .body("data.user.name", equalTo(user.name()))
                .body("data.user.status", equalTo(user.status().getStatus()))
                .body("data.user.gender", equalTo(user.gender().getGender()))
                .header("Content-Type", equalTo ("application/json; charset=utf-8"))
                .header("Server", equalTo("cloudflare"));
        LOGGER.info("Test Passed");
    }

    @Test
    public void updateStatusById() {
        LOGGER.info("Creating random User");
        User user = UserCreator.saveGraphQLUser();
        LOGGER.info("User created: {}", user);

        LOGGER.info("Change status {}", user.status().getStatus());
        Status newStatus;
        if (user.status().getStatus().equals("active")) {
            newStatus = Status.INACTIVE;
        } else {
            newStatus = Status.ACTIVE;
        }
        LOGGER.info("Status right now is: {}", newStatus.getStatus());

        LOGGER.info("Creating a query");
        String query = String.format(QueryProvider.UPDATE_STATUS,
                user.id(), newStatus.getStatus());
        LOGGER.info("Query created: {}", query);

        LOGGER.info("Sending {} request, \nwith created query: {}", "Update Status by Id", query);
        given()
                .auth().oauth2(ConfigLoader.getProperty("token"))
                .contentType(ContentType.JSON)
                .body(query)
                .post()
                .then()
                .statusCode(200)
                .body("data.updateUser.user.id", equalTo(user.id().intValue()))
                .body("data.updateUser.user.name", equalTo(user.name()))
                .body("data.updateUser.user.email", equalTo(user.email()))
                .body("data.updateUser.user.gender", equalTo(user.gender().getGender()))
                .body("data.updateUser.user.status", equalTo(newStatus.getStatus()))
                .header("Content-Type", equalTo ("application/json; charset=utf-8"))
                .header("Server", equalTo("cloudflare"));
        LOGGER.info("Test Passed");
    }

    @Test
    public void updateGenderById() {
        LOGGER.info("Creating random User");
        User user = UserCreator.saveGraphQLUser();
        LOGGER.info("User created: {}", user);

        LOGGER.info("Change gender: {}", user.gender().getGender());
        Gender newGender;
        if (user.gender().getGender().equals("female")) {
            newGender = Gender.MALE;
        } else {
            newGender = Gender.FEMALE;
        }
        LOGGER.info("Gender right now is: {}", newGender.getGender());

        LOGGER.info("Creating a query");
        String query = String.format(QueryProvider.UPDATE_GENDER,
                user.id(), newGender.getGender());
        LOGGER.info("Query created: {}", query);

        LOGGER.info("Sending {} request, \nwith created query: {}", "Update Gender by Id", query);
        given()
                .auth().oauth2(ConfigLoader.getProperty("token"))
                .contentType(ContentType.JSON)
                .body(query)
                .post()
                .then()
                .statusCode(200)
                .body("data.updateUser.user.id", equalTo(user.id().intValue()))
                .body("data.updateUser.user.name", equalTo(user.name()))
                .body("data.updateUser.user.email", equalTo(user.email()))
                .body("data.updateUser.user.gender", equalTo(newGender.getGender()))
                .body("data.updateUser.user.status", equalTo(user.status().getStatus()))
                .header("Content-Type", equalTo ("application/json; charset=utf-8"))
                .header("Server", equalTo("cloudflare"));
        LOGGER.info("Test Passed");
    }

    @Test
    public void deleteById() {
        LOGGER.info("Creating random User");
        User user = UserCreator.saveGraphQLUser();
        LOGGER.info("User created: {}", user);

        LOGGER.info("Creating a query");
        String query = String.format(QueryProvider.DELETE_USER,
                user.id());
        LOGGER.info("Query created: {}", query);

        LOGGER.info("Sending {} request, \nwith created query: {}", "Delete By Id", query);
        given()
                .auth().oauth2(ConfigLoader.getProperty("token"))
                .contentType(ContentType.JSON)
                .body(query)
                .post()
                .then()
                .statusCode(200)
                .body("data.deleteUser.user.id", equalTo(user.id().intValue()))
                .body("data.deleteUser.user.name", equalTo(user.name()))
                .body("data.deleteUser.user.email", equalTo(user.email()))
                .body("data.deleteUser.user.gender", equalTo(user.gender().getGender()))
                .body("data.deleteUser.user.status", equalTo(user.status().getStatus()))
                .header("Server", equalTo("cloudflare"));
        LOGGER.info("Test Passed");
    }

}
