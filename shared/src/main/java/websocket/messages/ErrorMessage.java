package websocket.messages;

public record ErrorMessage(ServerMessage.ServerMessageType serverMessageType, String errorMessage) {
}
