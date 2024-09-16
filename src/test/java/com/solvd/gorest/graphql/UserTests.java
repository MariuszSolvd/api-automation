package com.solvd.gorest.graphql;

import com.solvd.dto.UserDTO;
import com.solvd.model.User;
import com.solvd.service.UserCreator;
import com.solvd.utilis.ConfigLoader;
import com.solvd.utilis.QueryProvider;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UserTests {

    @BeforeTest
    public void setUrl() {
        RestAssured.baseURI = ConfigLoader.getProperty("graphqlurl");
    }

    @Test
    public void createUser() {
        UserDTO userToAdd = UserCreator.createUser();
        String query = String.format(QueryProvider.CREATE_USER,
                userToAdd.getName(), userToAdd.getEmail(), userToAdd.getGender().getGender(), userToAdd.getStatus().getStatus());

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
    }

    @Test
    public void getUserById() {
        User user = UserCreator.saveGraphQLUser();
        String query = String.format(QueryProvider.GET_USER_BY_ID,
                user.id());

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
    }


}
