package client;

import exception.ResponseException;
import model.AuthData;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @BeforeEach
    public void setup() throws ResponseException {
        facade.delete();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        assertTrue(true);
    }

    @Test
    void registerPos() throws Exception {
        AuthData authData = (AuthData) facade.register("player1", "password", "p1@email.com");
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void nullRegister() throws Exception {
        assertThrows(Exception.class, () -> {
            facade.register(null, "password", "p1@email.com");
        });
    }

    @Test
    void loginPos() throws Exception {
        AuthData regData = (AuthData) facade.register("player1", "password", "p1@email.com");
        AuthData authData = (AuthData) facade.login("player1", "password");
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void loginNeg() throws Exception {
        assertThrows(Exception.class, () -> {
            facade.login(null, "password");
        });
    }

}
