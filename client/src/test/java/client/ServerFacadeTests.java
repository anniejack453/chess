package client;

import exception.ResponseException;
import model.AuthData;
import model.CreateResult;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;

import java.util.Map;

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

    @Test
    void logoutPos() throws Exception {
        AuthData regData = (AuthData) facade.register("player1", "password", "p1@email.com");
        assertDoesNotThrow(() -> {
            facade.logout(regData.authToken());
        });
    }

    @Test
    void logoutNeg() throws Exception {
        assertThrows(Exception.class, () -> {
            facade.logout("doesNotExist");
        });
    }

    @Test
    void deletePos() throws Exception {
        AuthData regData = (AuthData) facade.register("player1", "password", "p1@email.com");
        AuthData moreData = (AuthData) facade.register("player2", "password2", "p2@email.com");
        AuthData moreData2 = (AuthData) facade.register("player3", "password3", "p3@email.com");
        assertDoesNotThrow(() -> {
            facade.delete();
        });
    }

    @Test
    void createPos() throws Exception {
        AuthData regData = (AuthData) facade.register("player1", "password", "p1@email.com");
        CreateResult id = (CreateResult) facade.createGame(regData.authToken(),"hello");
        assertEquals(1, id.gameID());
    }

    @Test
    void createNeg() throws Exception {
        assertThrows(Exception.class, () -> {
            facade.createGame("doesNotExist", "hi");
        });
    }

    @Test
    void joinPos() throws Exception {
        AuthData regData = (AuthData) facade.register("player1", "password", "p1@email.com");
        CreateResult id = (CreateResult) facade.createGame(regData.authToken(),"hello");
        assertDoesNotThrow(() -> {
            facade.joinGame(regData.authToken(), "WHITE", id.gameID());
        });
    }

    @Test
    void joinNeg() throws Exception {
        AuthData regData = (AuthData) facade.register("player1", "password", "p1@email.com");
        assertThrows(Exception.class, () -> {
            facade.joinGame(regData.authToken(), "BLACK", 1);
        });
    }

    @Test
    void listPos() throws Exception {
        AuthData regData = (AuthData) facade.register("player1", "password", "p1@email.com");
        CreateResult id = (CreateResult) facade.createGame(regData.authToken(),"hello");
        facade.joinGame(regData.authToken(), "WHITE", id.gameID());
        Map gameList = (Map) facade.listGames(regData.authToken());
        assertNotNull(gameList);
    }

    @Test
    void listNeg() throws Exception {
        assertThrows(Exception.class, () -> {
            facade.listGames("doesNotExist");
        });
    }
}
