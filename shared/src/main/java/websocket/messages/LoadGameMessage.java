package websocket.messages;

public record LoadGameMessage(ServerMessage.ServerMessageType serverMessageType, Object game) {}
