package dataaccess;

import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class SQLGameDAOTest {
    private SQLGameDAO gameData;
    private GameData game1;
    private GameData game2;

    @BeforeEach
    void setup() throws Exception {
        gameData = new SQLGameDAO();
        game1 = gameData.createGame("lonely");
        game2 = gameData.createGame("hello");
    }

    @AfterEach
    void clearData() throws DataAccessException {
        gameData.clearGames();
    }

    @Test
    void clearGames() throws DataAccessException {
        gameData.clearGames();
        assertTrue(gameData.listGames().isEmpty());
    }

    @Test
    void listGamesPos() throws DataAccessException {
        Collection<GameData> gameList = gameData.listGames();
        assertEquals(gameList,gameData.listGames());
    }

    @Test
    void listGamesNeg() throws DataAccessException {
        GameData game3 = new GameData(123,null,null,"hello",new ChessGame());
        assertFalse(gameData.listGames().contains(game3));
    }

    @Test
    void getGamePos() throws DataAccessException {
        assertEquals(game2.gameName(),gameData.getGameData("hello").gameName());
    }

    @Test
    void getGameNeg() throws DataAccessException {
        assertNull(gameData.getGameData("he"));
    }

    @Test
    void createGamePos() throws DataAccessException {
        var game3 = gameData.createGame("gah");
        assertEquals(3, gameData.listGames().size());
    }

    @Test
    void createGameNeg() throws DataAccessException {
        var game3 = gameData.createGame(null);
        assertFalse(gameData.listGames().contains(game3));
    }

    @Test
    void getGameIDPos() throws DataAccessException {
        assertEquals("lonely",gameData.getGameID(game1.gameID()));
    }

    @Test
    void getGameIDNeg() throws DataAccessException {
        assertNull(gameData.getGameID(null));
    }

    @Test
    void joinGamePos() throws Exception {
        GameData newGameData = new GameData(game1.gameID(),"a",null,"lonely", game1.game());
        assertEquals(newGameData,gameData.joinGame("lonely","WHITE","a"));
    }

    @Test
    void joinGameNeg() {
        assertThrows(Exception.class, () -> {
            gameData.joinGame("hen","WHITE","a");
        });
    }
}