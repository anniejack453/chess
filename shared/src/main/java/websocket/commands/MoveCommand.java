package websocket.commands;

import chess.ChessGame;
import chess.ChessMove;

import java.util.Objects;

public final class MoveCommand extends UserGameCommand {
    private final ChessMove move;

    public MoveCommand(CommandType userGameCommandType, String authToken, Integer gameID,
                       IdentityType identity, ChessGame.TeamColor teamColor, ChessMove move) {
        super(userGameCommandType, authToken, gameID, identity, teamColor);
        this.move = move;
    }

    public ChessMove getMove() {
        return move;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        MoveCommand that = (MoveCommand) o;
        return Objects.equals(move, that.move);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), move);
    }
}
