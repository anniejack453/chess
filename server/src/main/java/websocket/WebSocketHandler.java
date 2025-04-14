package websocket;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.*;
import exception.ResponseException;
import model.AuthData;
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
                throw new DataAccessException("Invalid authToken");
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
        var notif = String.format("%s has left", username);
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
            throw new DataAccessException("Invalid game ID");
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
            throw new Exception("Observer cannot resign");
        }
        if (identity == UserGameCommand.IdentityType.OBSERVER) {
            throw new Exception("Observer cannot resign");
        }
        var notif = String.format("%s has resigned", username);
        if (gameName != null) {
            var message = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, notif);
            connections.broadcastResign(command.getGameID(), message);
            state = WebSocketState.RESIGNED;
        } else {
            throw new DataAccessException("Invalid game ID");
        }
    }

    private void connect(String username, UserGameCommand command, Session session) throws IOException, DataAccessException {
        var gameName = gameDAO.getGameID(command.getGameID());
        var notif = String.format("%s has joined the game", username);
        if (gameName != null) {
            connections.add(username, command.getGameID(), session);
            var chess = gameDAO.getGameData(gameName).game();
            var message = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, notif);
            var game = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, chess);
            connections.broadcastLoadGameAfterConnect(username, command.getGameID(), game);
            connections.broadcastOthers(username, command.getGameID(), message);
        } else {
            throw new DataAccessException("Invalid game ID");
        }
    }

    private void makeMove(String username, MoveCommand command, Session session) throws Exception {
        assertPlaying();
        var gameName = gameDAO.getGameID(command.getGameID());
        if (gameName != null) {
            var gameData = gameDAO.getGameData(gameName);
            var chess = gameData.game();
            var notif = String.format("%s has made a move", username);
            if (Objects.equals(username, gameData.blackUsername())) {
                try {
                    chess.makeMove(command.getMove());
                    var message = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, notif);
                    var game = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, chess);
                    connections.broadcastOthers(username, command.getGameID(), message);
                    connections.broadcastLoadGameAfterMove(command.getGameID(), game);
                    chess.setTeamTurn(ChessGame.TeamColor.WHITE);
                } catch (InvalidMoveException e) {
                    throw new RuntimeException(e);
                }
            } else if (Objects.equals(username, gameData.whiteUsername())) {
                try {
                    chess.makeMove(command.getMove());
                    var message = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, notif);
                    var game = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, chess);
                    connections.broadcastOthers(username, command.getGameID(), message);
                    connections.broadcastLoadGameAfterMove(command.getGameID(), game);
                    chess.setTeamTurn(ChessGame.TeamColor.BLACK);
                } catch (InvalidMoveException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            throw new DataAccessException("Invalid game ID");
        }

    }

    private String getUsername(String authToken) throws Exception {
        AuthData authData = authDAO.getUserFromAuth(authToken);
        if (authData == null) {
            throw new Exception("Incorrect authToken");
        }
        return authData.username();
    }

    private void assertPlaying() throws Exception{
        if (state == WebSocketState.RESIGNED) {
            throw new Exception("You are resigned. Gameplay has ended");
        }
    }
}
