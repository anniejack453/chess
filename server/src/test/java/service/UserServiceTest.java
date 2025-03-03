package service;

import dataaccess.MemoryUserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserServiceTest {

    @BeforeEach
    void setup(){
        var userData = new MemoryUserDAO();
    }

    @Test
    void getUserPos() {

    }

    @Test
    void createUser() {
    }

    @Test
    void listUsers() {
    }

    @Test
    void clearUsers() {
    }
}
