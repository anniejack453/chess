package service;

import dataaccess.GameDAO;
import dataaccess.UserDAO;
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
}
