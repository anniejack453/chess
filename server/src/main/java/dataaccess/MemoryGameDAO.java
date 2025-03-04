package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MemoryGameDAO implements GameDAO{
    final private HashMap<Integer, GameData> games = new HashMap<>();

    @Override
    public Collection<GameData> listGames() {
        return games.values();
    }

    @Override
    public void clearGames() {
        games.clear();
    }

    @Override
    public GameData createGame(String gameName) {
        Random rand = new Random();
        Integer gameID = rand.nextInt(10000);
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(gameID,null,null,gameName,game);
        games.put(gameID, gameData);
        return gameData;
    }

    @Override
    public GameData getGame(String gameName) {
        return games.get(gameName);
    }

}
