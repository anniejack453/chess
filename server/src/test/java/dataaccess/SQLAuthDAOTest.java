package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SQLAuthDAOTest {
    SQLAuthDAO authData;
    private AuthData auth1;
    private AuthData auth2;

    @BeforeEach
    void setup() throws Exception {
        authData = new SQLAuthDAO();
        auth1 = authData.createAuth("newUser");
        auth2 = authData.createAuth("AnnaE");
    }

    @AfterEach
    void clearData() throws DataAccessException {
        authData.clearAuths();
    }

    @Test
    void createAuthPos() throws Exception {
        AuthData newAuth = authData.createAuth("newUser");
        assertTrue(authData.listAuths().contains(newAuth));
    }

    @Test
    void createAuthNeg() throws Exception {
        assertThrows(Exception.class, () -> {
            authData.createAuth(null);
        });
    }

    @Test
    void getAuthPos() throws Exception {
        AuthData userAuth = authData.createAuth("hippy");
        String authToken = userAuth.authToken();
        assertEquals(userAuth,authData.getUserFromAuth(authToken));
    }

    @Test
    void getAuthNeg() throws Exception {
        assertThrows(Exception.class, () -> {
            authData.getUserFromAuth(null);
        });
    }

    @Test
    void deleteAuthPos() throws Exception {
        AuthData userAuth = authData.createAuth("hip");
        String authToken = userAuth.authToken();
        authData.deleteAuth(authToken);
        Assertions.assertFalse(authData.listAuths().contains(userAuth));
    }

    @Test
    void deleteAuthNeg() throws Exception {
        var sizeBefore = authData.listAuths().size();
        authData.deleteAuth("10123");
        Assertions.assertEquals(sizeBefore, authData.listAuths().size());
    }

    @Test
    void listAuthsPos() throws DataAccessException {
        Assertions.assertTrue(authData.listAuths().contains(auth1) && authData.listAuths().contains(auth2));
    }

    @Test
    void listAuthsNeg() throws Exception {
        authData.deleteAuth(auth1.authToken());
        Assertions.assertFalse(authData.listAuths().contains(auth1) && authData.listAuths().contains(auth2));
    }

    @Test
    void clearAuths() throws DataAccessException {
        authData.clearAuths();
        assertTrue(authData.listAuths().isEmpty());
    }

}