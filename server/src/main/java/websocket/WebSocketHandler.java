package websocket;

import com.google.gson.Gson;
import dataaccess.*;
import exception.ResponseException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.messages.ServerMessage;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.util.Timer;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
//        Action action = new Gson().fromJson(message, Action.class);
//        switch (action.type()) {
//            case ENTER -> enter(action.visitorName(), session);
//            case EXIT -> exit(action.visitorName());
//        }
    }
}
