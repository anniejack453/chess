package websocket.messages;

public record NotificationMessage(ServerMessage.ServerMessageType serverMessageType, String message) {}
