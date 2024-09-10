package gorest.api;

import com.solvd.dto.CreateUser;
import com.solvd.service.UserCreator;
import org.testng.annotations.Test;

public class UserTests {

    @Test
    public void createUser() {
        CreateUser createUser = UserCreator.createUser();


    }
}
