package com.solvd.service;

import com.solvd.dto.UserDTO;
import com.solvd.model.Gender;
import com.solvd.model.Status;
import com.solvd.model.User;
import com.solvd.utilis.ConfigLoader;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class UserCreator {
    private static final List<String> NAMES = List.of("James", "Michael", "Robert", "John", "David", "William",
            "Richard", "Joseph", "Thomas", "Daniel", "Mary", "Patricia", "Jennifer", "Linda", "Elizabeth", "Barbara",
            "Susan", "Jessica", "Karen", "Sarah");
    private static final List<String> LASTNAMES = List.of("Smith", "Johnson", "Williams", "Brown", "Jones", "Gracia", "Miller",
            "Davis", "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas", "Taylor",
            "Moore", "Jackson", "Martin");
    private static final List<String> DOMAIN = List.of("gmail.com", "outlook.com", "yahoo.com", "protonmail.com", "zoho.com",
            "hushmail.com", "mailfance.com", "tutanota.com", "thexyz.com", "runbox.com");


    public static UserDTO createUser() {
        Random random = new Random();
        //Creating name using random name and lastname from the list
        String name = NAMES.get(random.nextInt(NAMES.size())) + " " + LASTNAMES.get(random.nextInt(LASTNAMES.size()));

        //Creating an email using name some random number along with random domain from the list
        String emailName = name.replace(" ", "_");
        String email = emailName.toLowerCase() + random.nextInt(100) + "@" + DOMAIN.get(random.nextInt(DOMAIN.size()));

        //Get random Gender based on an Array of genders
        Gender gender = Gender.values()[random.nextInt(Gender.values().length)];

        //Get random Status based on an Array of Statuses
        Status status = Status.values()[random.nextInt(Status.values().length)];

        //Creating instance of createUser with random generated data
        return UserDTO.builder().name(name)
                .email(email)
                .gender(gender)
                .status(status).build();
    }

    public static User saveUser() {
        RestAssured.baseURI = ConfigLoader.getProperty("url");

        //Creating random user
        UserDTO userDTO = UserCreator.createUser();

        //Adding createUser

        return given()
                .auth().oauth2(ConfigLoader.getProperty("token"))
                .contentType(ContentType.JSON)
                .body(userDTO)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .as(User.class);
    }
}
