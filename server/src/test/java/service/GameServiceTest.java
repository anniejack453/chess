package service;

import chess.ChessGame;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {
    MemoryGameDAO gameData;
    private GameData game1;
    private GameData game2;

    @BeforeEach
    void setup() throws Exception {
        gameData = new MemoryGameDAO();
        game1 = gameData.createGame("help");
        game2 = gameData.createGame("Ann");
    }

    @Test
    void clearGames() {
        gameData.clearGames();
        assertTrue(gameData.listGames().isEmpty());
    }

    @Test
    void listGamesPos() {
        assertTrue(gameData.listGames().contains(game1) && gameData.listGames().contains(game2));
    }

    @Test
    void listGamesNeg() {
        GameData game3 = new GameData(123,null,null,"hello",new ChessGame());
        assertTrue(!gameData.listGames().contains(game3));
    }

    @Test
    void getGamePos() {
        assertEquals(game1,gameData.getGameData("help"));
    }

    @Test
    void getGameNeg() {
        assertNull(gameData.getGameData("he"));
    }

    @Test
    void createGamePos() {
        var game3 = gameData.createGame("3");
        assertTrue(gameData.listGames().contains(game3));
    }

    @Test
    void createGameNeg() {
        var game3 = gameData.createGame("");
        assertFalse(gameData.listGames().contains(game3));
    }

    @Test
    void getGameIDPos() {
        assertEquals("help",gameData.getGameID(game1.gameID()));
    }

    @Test
    void getGameIDNeg() {
        assertNull(gameData.getGameID(null));
    }

    @Test
    void joinGamePos() throws Exception {
        GameData newGameData = new GameData(game1.gameID(),"a",null,"help", game1.game());
        assertEquals(newGameData,gameData.joinGame("help","WHITE","a"));
    }

    @Test
    void joinGameNeg() throws Exception {
        assertThrows(Exception.class, () -> {
            gameData.joinGame("he","WHITE","a");
        });
    }
}