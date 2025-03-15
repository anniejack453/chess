package service;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;

import java.util.Collection;

public class GameService {
    private final GameDAO dataAccess;

    public GameService(GameDAO dataAccess) {
        this.dataAccess = dataAccess;
    }

    public void clearGames() throws DataAccessException {
        dataAccess.clearGames();
    }

    public Collection<GameData> listGames() throws DataAccessException {
        return dataAccess.listGames();
    }

    public GameData getGame(String gameName) throws DataAccessException {
        return dataAccess.getGameData(gameName);
    }

    public GameData createGame(String gameName) throws DataAccessException {
        return dataAccess.createGame(gameName);
    }

    public String getGameID(Integer gameID){
        return dataAccess.getGameID(gameID);
    }

    public GameData joinGame(String gameName, String playerColor, String username) throws Exception {
        return dataAccess.joinGame(gameName,playerColor,username);
    }
}
