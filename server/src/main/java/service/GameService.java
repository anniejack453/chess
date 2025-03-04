package service;

import dataaccess.GameDAO;
import model.GameData;

import java.util.Collection;

public class GameService {
    private final GameDAO dataAccess;

    public GameService(GameDAO dataAccess) {
        this.dataAccess = dataAccess;
    }

    public void clearGames(){
        dataAccess.clearGames();
    }

    public Collection<GameData> listGames(){
        return dataAccess.listGames();
    }

    public GameData getGame(String gameName){
        return dataAccess.getGame(gameName);
    }

    public GameData createGame(String gameName){
        return dataAccess.createGame(gameName);
    }

    public String getGameID(Integer gameID){
        return dataAccess.getGameID(gameID);
    }

    public GameData joinGame(String gameName, String playerColor, String username) throws Exception {
        return dataAccess.joinGame(gameName,playerColor,username);
    }
}
