package dataaccess;

import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SQLUserDAOTest {
    SQLUserDAO userData;

    @BeforeEach
    void setup() throws Exception {
        userData = new SQLUserDAO();
        userData.createUser(new UserData("user","123","email@email.com"));
        userData.createUser(new UserData("Ann","345","ann@email.com"));
    }

    @Test
    void getUserPos() throws DataAccessException {
        UserData user = new UserData("Ann","345","ann@email.com");
        assertEquals(user, userData.getUser("Ann"));
    }

    @Test
    void getUserNeg() throws DataAccessException {
        Assertions.assertNull(userData.getUser("An"));
    }

    @Test
    void createUserPos() throws Exception {
        UserData user = new UserData("Use","222","email");
        assertEquals(user,userData.createUser(user));
    }

    @Test
    void createUserNeg() throws Exception {
        UserData user = new UserData("Ann","345","ann@email.com");
        Assertions.assertThrows(Exception.class, () -> {
            userData.createUser(user);
        });
    }

    @Test
    void listUsersPos() throws Exception {

        List<UserData> expectedUsers = List.of(
                new UserData("Ann", "345", "ann@email.com"),
                new UserData("user", "123", "email@email.com")
        );

        Assertions.assertTrue(userData.listUsers().containsAll(expectedUsers));
    }

    @Test
    void listUsersNeg() throws Exception {

        List<UserData> expectedUsers = List.of(
                new UserData("Ann", "345", "ann@email.com"),
                new UserData("user", "123", "email@email.com"),
                new UserData("Use","321","user@email.com")
        );

        Assertions.assertFalse(userData.listUsers().containsAll(expectedUsers));
    }

    @Test
    void clearUsers() throws DataAccessException {
        userData.clearUsers();
        Assertions.assertTrue(userData.listUsers().isEmpty());
    }
}