package dataaccess;

import java.util.Collection;
import model.GameData;
public interface GameDAO {
    public Collection<GameData> listGames();
    public void clearGames();
    public GameData createGame(String gameName);
    public GameData getGame(String gameName);

}
