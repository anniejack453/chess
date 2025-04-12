package websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.*;
import exception.ResponseException;
import model.AuthData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.messages.LoadGameMessage;
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
            switch (command.getCommandType()) {
                case CONNECT -> connect(username, command, session);
            }
        } catch (Exception e) {
            e.printStackTrace();
            var error = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            session.getRemote().sendString(new Gson().toJson(error));
        }
    }

    private void connect(String username, UserGameCommand command, Session session) throws IOException, DataAccessException {
        connections.add(username, command.getGameID(), session);
        var gameName = gameDAO.getGameID(command.getGameID());
        var chess = gameDAO.getGameData(gameName).game();
        var notif = String.format("%s has joined the game", username);
        var message = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        var game = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, chess);
        var serverMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        connections.broadcastLoadGame(username, command.getGameID(), game);
    }

    private String getUsername(String authToken) throws Exception {
        AuthData authData = authDAO.getUserFromAuth(authToken);
        if (authData == null) {
            throw new Exception("Incorrect authToken");
        }
        return authData.username();
    }
}
