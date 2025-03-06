package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {
    MemoryAuthDAO authData;
    private AuthData auth1;
    private AuthData auth2;

    @BeforeEach
    void setup() throws Exception {
        authData = new MemoryAuthDAO();
        auth1 = authData.createAuth("user");
        auth2 = authData.createAuth("Ann");
    }

    @Test
    void createAuthPos() throws Exception {
        AuthData newAuth = authData.createAuth("user");
        assertTrue(authData.listAuths().contains(newAuth));
    }

    @Test
    void createAuthNeg() throws Exception {
        assertThrows(Exception.class, () -> {
            authData.createAuth("");
        });
    }

    @Test
    void getAuthPos() throws Exception {
        AuthData userAuth = authData.createAuth("hi");
        String authToken = userAuth.authToken();
        assertEquals(userAuth,authData.getUserFromAuth(authToken));
    }

    @Test
    void getAuthNeg() throws Exception {
        assertThrows(Exception.class, () -> {
            authData.getUserFromAuth("");
        });
    }

    @Test
    void deleteAuthPos() throws Exception {
        AuthData userAuth = authData.createAuth("hi");
        String authToken = userAuth.authToken();
        authData.deleteAuth(authToken);
        Assertions.assertFalse(authData.listAuths().contains(userAuth));
    }

    @Test
    void deleteAuthNeg() throws Exception {
        var sizeBefore = authData.listAuths().size();
        authData.deleteAuth("101");
        Assertions.assertEquals(sizeBefore, authData.listAuths().size());
    }

    @Test
    void listAuthsPos() {
        Assertions.assertTrue(authData.listAuths().contains(auth1) && authData.listAuths().contains(auth2));
    }

    @Test
    void listAuthsNeg() throws Exception {
        authData.deleteAuth(auth1.authToken());
        Assertions.assertFalse(authData.listAuths().contains(auth1) && authData.listAuths().contains(auth2));
    }

    @Test
    void clearAuths() {
        authData.clearAuths();
        assertTrue(authData.listAuths().isEmpty());
    }
}