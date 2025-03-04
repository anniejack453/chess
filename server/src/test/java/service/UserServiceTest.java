package service;

import dataaccess.MemoryUserDAO;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserServiceTest {
    MemoryUserDAO userData;

    @BeforeEach
    void setup(){
        userData = new MemoryUserDAO();
        userData.createUser(new UserData("user","123","email@email.com"));
        userData.createUser(new UserData("Ann","345","ann@email.com"));
    }

    @Test
    void getUserPos() {
        UserData user = new UserData("Ann","345","ann@email.com");
        Assertions.assertEquals(user, userData.getUser("Ann"));
    }

    @Test
    void getUserNeg() {
        Assertions.assertNull(userData.getUser("An"));
    }

    @Test
    void createUserPos() {

    }

    @Test
    void listUsers() {
    }

    @Test
    void clearUsers() {
    }

    @Test
    void getUser() {
    }

    @Test
    void createUser() {
    }

    @Test
    void testListUsers() {
    }

    @Test
    void testClearUsers() {
    }
}
