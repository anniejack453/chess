package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.*;

public class MemoryGameDAO implements GameDAO{
    final private HashMap<String, GameData> games = new HashMap<>();

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
        if (gameName != null && !gameName.isEmpty()){
            Random rand = new Random();
            Integer gameID = rand.nextInt(10000);
            ChessGame game = new ChessGame();
            GameData gameData = new GameData(gameID,null,null,gameName,game);
            games.put(gameName, gameData);
            return gameData;
        }
        return null;
    }

    @Override
    public GameData getGameData(String gameName) {
        return games.get(gameName);
    }

    @Override
    public String getGameID(Integer gameID) {
        if (gameID == null){
            return null;
        }
        String gameName = null;
        for(Map.Entry<String,GameData> entry : games.entrySet()){
            GameData game = entry.getValue();
            if (game.gameID() == gameID){
                gameName = entry.getKey();
                break;
            }
        }
        return gameName;
    }

    @Override
    public GameData joinGame(String gameName, String playerColor, String username) throws Exception {
        GameData gameData = games.get(gameName);
        if (gameData == null){
            throw new Exception("Game name not found");
        }
        if (Objects.equals(playerColor, "WHITE")){
            if (gameData.whiteUsername() == null){
                games.replace(gameName, new GameData(gameData.gameID(),username,gameData.blackUsername(),gameName,gameData.game()));
                return games.get(gameName);
            } else {
                return null;
            }
        } else if (Objects.equals(playerColor, "BLACK")){
            if (gameData.blackUsername() == null){
                games.replace(gameName, new GameData(gameData.gameID(),gameData.whiteUsername(),username,gameName,gameData.game()));
                return games.get(gameName);
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

    @Override
    public GameData setUsernameNull(String gameName, String playerColor, String username) {
        return null;
    }

    @Override
    public GameData updateGame(String gameName, ChessGame chess) {
        return null;
    }

}
