package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String visitorName, Integer gameID, Session session) {
        var connection = new Connection(visitorName, gameID, session);
        connections.put(visitorName, connection);
    }

    public void remove(String visitorName) {
        connections.remove(visitorName);
    }

    public void broadcastError(String username, Integer gameID, ErrorMessage message) throws IOException {
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (c.visitorName.equals(username)) {
                    c.send(new Gson().toJson(message));
                }
            }
        }
    }

    public void broadcastOthers(String excludeVisitorName, Integer gameID, NotificationMessage message) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.visitorName.equals(excludeVisitorName) && Objects.equals(c.gameID, gameID)) {
                    c.send(new Gson().toJson(message));
                }
            } else {
                removeList.add(c);
            }
        }
        for (var c : removeList) {
            connections.remove(c.visitorName);
        }
    }

    public void broadcastLoadGameAfterConnect(String username, Integer gameID, LoadGameMessage message) throws IOException {
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (Objects.equals(c.gameID, gameID) && c.visitorName.equals(username)) {
                    c.send(new Gson().toJson(message));
                }
            }
        }
    }
}

