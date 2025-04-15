package websocket.messages;

import chess.ChessGame;

import java.util.Objects;

public final class LoadGameMessage extends ServerMessage {
    private final ChessGame game;
    private ChessGame.TeamColor teamColor;

    public LoadGameMessage(ServerMessageType type, ChessGame game, ChessGame.TeamColor teamColor) {
        super(type);
        this.game = game;
        this.teamColor = teamColor;
    }

    public ChessGame game() {
        return game;
    }

    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        LoadGameMessage that = (LoadGameMessage) o;
        return Objects.equals(game, that.game);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), game);
    }
}
