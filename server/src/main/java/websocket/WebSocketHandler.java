package websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.*;
import exception.ResponseException;
import model.AuthData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.util.Timer;

@WebSocket
public class WebSocketHandler {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;


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

    private void resign(String username, UserGameCommand command, Session session) throws DataAccessException, IOException {
        var gameName = gameDAO.getGameID(command.getGameID());
        var notif = String.format("%s has resigned", username);
        if (gameName != null) {
            var message = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, notif);
            connections.broadcastResign(command.getGameID(), message);
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

    private void makeMove(String username, UserGameCommand command, Session session) {

    }

    private String getUsername(String authToken) throws Exception {
        AuthData authData = authDAO.getUserFromAuth(authToken);
        if (authData == null) {
            throw new Exception("Incorrect authToken");
        }
        return authData.username();
    }
}
