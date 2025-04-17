package websocket;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.*;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.MoveCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;

@WebSocket
public class WebSocketHandler {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private WebSocketState state = WebSocketState.GAMEPLAY;


    private final ConnectionManager connections = new ConnectionManager();

    public WebSocketHandler(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        try {
            UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
            String username = getUsername(command.getAuthToken());
            if (username != null) {
                switch (command.getCommandType()) {
                    case CONNECT -> connect(username, command, session);
                    case RESIGN -> resign(username, command, session);
                    case LEAVE -> leave(username, command, session);
                    case MAKE_MOVE -> {
                        MoveCommand moveCommand = new Gson().fromJson(message, MoveCommand.class);
                        makeMove(username, moveCommand, session);
                    }
                }
            } else {
                throw new DataAccessException("Invalid authToken\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
            var error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, e.getMessage());
            session.getRemote().sendString(new Gson().toJson(error));
        }
    }

    private void leave(String username, UserGameCommand command, Session session) throws Exception {
        var gameName = gameDAO.getGameID(command.getGameID());
        var game = gameDAO.getGameData(gameName);
        var notif = String.format("%s has left\n", username);
        if (gameName != null) {
            var message = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, notif);
            connections.broadcastOthers(username, command.getGameID(), message);
            connections.leaveGame(username, command.getGameID());
            if (Objects.equals(username, game.blackUsername())) {
                gameDAO.setUsernameNull(gameName, "BLACK", null);
            } else if (Objects.equals(username, game.whiteUsername())) {
                gameDAO.setUsernameNull(gameName, "WHITE", null);
            }
        } else {
            throw new DataAccessException("Invalid game ID\n");
        }
    }

    private void resign(String username, UserGameCommand command, Session session) throws Exception {
        assertPlaying();
        var identity = command.getIdentity();
        var gameName = gameDAO.getGameID(command.getGameID());
        var game = gameDAO.getGameData(gameName);
        var usernames = new ArrayList<>();
        usernames.add(game.blackUsername());
        usernames.add(game.whiteUsername());
        if (!usernames.contains(username)) {
            throw new Exception("Observer cannot resign\n");
        }
        if (identity == UserGameCommand.IdentityType.OBSERVER) {
            throw new Exception("Observer cannot resign\n");
        }
        var notif = String.format("%s has resigned\n", username);
        if (gameName != null) {
            var message = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, notif);
            connections.broadcastResign(command.getGameID(), message);
            state = WebSocketState.RESIGNED;
        } else {
            throw new DataAccessException("Invalid game ID\n");
        }
    }

    private void connect(String username, UserGameCommand command, Session session) throws IOException, DataAccessException {
        state = WebSocketState.GAMEPLAY;
        var gameName = gameDAO.getGameID(command.getGameID());
        var gameData = gameDAO.getGameData(gameName);
        var notif = getString(username, command, gameData);
        if (gameName != null) {
            connections.add(username, command.getGameID(), session);
            var chess = gameDAO.getGameData(gameName).game();
            var message = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, notif);
            if (Objects.equals(username, gameData.blackUsername())) {
                var game = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, chess, ChessGame.TeamColor.BLACK);
                connections.broadcastLoadGame(username, command.getGameID(), game);
            } else if (Objects.equals(username, gameData.whiteUsername())) {
                var game = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, chess, ChessGame.TeamColor.WHITE);
                connections.broadcastLoadGame(username, command.getGameID(), game);
            } else {
                var game = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, chess, ChessGame.TeamColor.WHITE);
                connections.broadcastLoadGame(username, command.getGameID(), game);
            }
            connections.broadcastOthers(username, command.getGameID(), message);
        } else {
            throw new DataAccessException("Invalid game ID\n");
        }
    }

    private static String getString(String username, UserGameCommand command, GameData gameData) {
        var notif = "";
        if (command.getTeamColor() == ChessGame.TeamColor.BLACK) {
            notif = String.format("%s has joined the game as %s\n", username, command.getTeamColor());
        } else {
            if (Objects.equals(username, gameData.whiteUsername())) {
                notif = String.format("%s has joined the game as %s\n", username, command.getTeamColor());
            } else {
                notif = String.format("%s has joined the game\n", username);
            }
        }
        return notif;
    }

    private void makeMove(String username, MoveCommand command, Session session) throws Exception {
        assertPlaying();
        var gameName = gameDAO.getGameID(command.getGameID());
        if (gameName != null) {
            var gameData = gameDAO.getGameData(gameName);
            var chess = gameData.game();
            if (determineStatus(username, chess, gameData)) {
                return;
            }
            if (command.getMove() == null && Objects.equals(username, gameData.blackUsername())) {
                var game = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, chess, ChessGame.TeamColor.BLACK);
                connections.broadcastLoadGame(username, command.getGameID(), game);
                return;
            } else if (command.getMove() == null && Objects.equals(username, gameData.whiteUsername())) {
                var game = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, chess, ChessGame.TeamColor.WHITE);
                connections.broadcastLoadGame(username, command.getGameID(), game);
                return;
            } else if (command.getMove() == null) {
                var game = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, chess, ChessGame.TeamColor.WHITE);
                connections.broadcastLoadGame(username, command.getGameID(), game);
                return;
            } else if (Objects.equals(username, gameData.blackUsername()) && chess.getTeamTurn() == ChessGame.TeamColor.BLACK) {
                try {
                    var message = moveMessage(username, command, gameName, chess);
                    var selfGame = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, chess, ChessGame.TeamColor.BLACK);
                    var otherGame = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, chess, ChessGame.TeamColor.WHITE);
                    connections.broadcastOthers(username, command.getGameID(), message);
                    connections.broadcastLoadGame(username, command.getGameID(), selfGame);
                    connections.broadcastLoadGameForOthers(username, command.getGameID(), otherGame);
                } catch (InvalidMoveException e) {
                    throw new RuntimeException(e);
                }
            } else if (Objects.equals(username, gameData.whiteUsername()) && chess.getTeamTurn() == ChessGame.TeamColor.WHITE) {
                try {
                    var message = moveMessage(username, command, gameName, chess);
                    var selfGame = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, chess, ChessGame.TeamColor.WHITE);
                    var otherGame = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, chess, ChessGame.TeamColor.BLACK);
                    connections.broadcastOthers(username, command.getGameID(), message);
                    if (gameData.blackUsername() != null) {
                        connections.broadcastLoadGameForOthers(gameData.blackUsername(), command.getGameID(), selfGame);
                        connections.broadcastLoadGame(gameData.blackUsername(), command.getGameID(), otherGame);
                    } else {
                        connections.broadcastLoadGame(username, command.getGameID(), selfGame);
                        connections.broadcastLoadGameForOthers(username, command.getGameID(), selfGame);
                    }
                } catch (InvalidMoveException e) {
                    throw new RuntimeException(e);
                }
            } else {throw new Exception("Not your turn\n");}
        } else {
            throw new DataAccessException("Invalid game ID\n");
        }
    }

    private NotificationMessage moveMessage(String username, MoveCommand command, String gameName, ChessGame chess) throws Exception {
        var move = command.getMove();
        var start = move.getStartPosition().toString();
        var end = move.getEndPosition().toString();
        var moveString = start + " " + end;
        var notif = String.format("%s has made a move %s\n", username, moveString);
        chess.makeMove(command.getMove());
        gameDAO.updateGame(gameName, chess);
        return new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, notif);
    }

    private String getUsername(String authToken) throws Exception {
        AuthData authData = authDAO.getUserFromAuth(authToken);
        if (authData == null) {
            throw new Exception("Incorrect authToken\n");
        }
        return authData.username();
    }

    private boolean determineStatus(String username, ChessGame chess, GameData gameData) throws IOException {
        if (chess.isInCheckmate(ChessGame.TeamColor.BLACK) && Objects.equals(username, gameData.blackUsername())) {
            var error = "You are in checkmate.";
            var checkMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, error);
            connections.broadcastOwnError(username, gameData.gameID(), checkMessage);
            return true;
        } else if (chess.isInCheckmate(ChessGame.TeamColor.WHITE) && Objects.equals(username, gameData.whiteUsername())) {
            var notif = "You are in checkmate.";
            var checkMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, notif);
            connections.broadcastOwnError(username, gameData.gameID(), checkMessage);
            return true;
        } else if (chess.isInCheck(ChessGame.TeamColor.BLACK) && Objects.equals(username, gameData.blackUsername())) {
            var notif = "You are in check.";
            var checkMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, notif);
            connections.broadcastOwnError(username, gameData.gameID(), checkMessage);
            return true;
        } else if (chess.isInCheck(ChessGame.TeamColor.WHITE) && Objects.equals(username, gameData.whiteUsername())) {
            var notif = "You are in check.";
            var checkMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, notif);
            connections.broadcastOwnError(username, gameData.gameID(), checkMessage);
            return true;
        }
        return false;
    }

    private void assertPlaying() throws Exception{
        if (state == WebSocketState.RESIGNED) {
            throw new Exception("You are resigned. Gameplay has ended\n");
        }
    }
}
