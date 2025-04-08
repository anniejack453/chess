package websocket;

import websocket.messages.ServerMessage;
public interface ServerMessageHandler {
    void message(ServerMessage message);
}
