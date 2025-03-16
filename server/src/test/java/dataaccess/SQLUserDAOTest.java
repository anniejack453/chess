package dataaccess;

import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SQLUserDAOTest {
    SQLUserDAO userData;

    @BeforeEach
    void setup() throws Exception {
        userData = new SQLUserDAO();
        userData.createUser(new UserData("user1","123","email@email.com"));
        userData.createUser(new UserData("Anna","345","anna@email.com"));
    }

    @AfterEach
    void clearData() throws DataAccessException {
        userData.clearUsers();
    }

    @Test
    void getUserPos() throws DataAccessException {
        UserData user = userData.getUser("Anna");
        assertTrue(verifyUser(user.username(), "345"));
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
        UserData user = new UserData("Anna","345","anna@email.com");
        Assertions.assertThrows(Exception.class, () -> {
            userData.createUser(user);
        });
    }

    @Test
    void listUsersPos() throws Exception {

        List<UserData> expectedUsers = List.of(
                new UserData("Anna", "345", "anna@email.com"),
                new UserData("user1", "123", "email@email.com")
        );

        List<UserData> actualUsers = (List<UserData>) userData.listUsers();

        for (int i = 0; i < actualUsers.size(); i++){
            UserData actualUser = actualUsers.get(i);
            UserData expectUser = expectedUsers.get(i);
            assertTrue(verifyUser(actualUser.username(),expectUser.password()));
        }
    }

    @Test
    void listUsersNeg() throws Exception {

        List<UserData> expectedUsers = List.of(
                new UserData("Anna", "345", "anna@email.com"),
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

    private String readHashedPasswordFromDatabase(String username) throws DataAccessException {
        var userInfo = userData.getUser(username);
        return userInfo.password();
    }

    private boolean verifyUser(String username, String providedClearTextPassword) throws DataAccessException {
        var hashedPassword = readHashedPasswordFromDatabase(username);

        return BCrypt.checkpw(providedClearTextPassword, hashedPassword);
    }
}