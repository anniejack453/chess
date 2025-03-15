package dataaccess;

import java.util.Collection;
import model.GameData;
public interface GameDAO {
    public Collection<GameData> listGames() throws DataAccessException;
    public void clearGames() throws DataAccessException;
    public GameData createGame(String gameName) throws DataAccessException;
    public GameData getGameData(String gameName) throws DataAccessException;
    public String getGameID(Integer gameID) throws DataAccessException;
    public GameData joinGame(String gameName, String playerColor, String username) throws Exception;

}
