package websocket.commands;

import chess.ChessGame;

import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 *
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class UserGameCommand {

    private final CommandType commandType;

    private final String authToken;

    private final Integer gameID;

    private final IdentityType identity;

    private ChessGame.TeamColor teamColor;

    public UserGameCommand(CommandType commandType, String authToken, Integer gameID, IdentityType identity, ChessGame.TeamColor teamColor) {
        this.commandType = commandType;
        this.authToken = authToken;
        this.gameID = gameID;
        this.identity = identity;
        this.teamColor = teamColor;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserGameCommand that = (UserGameCommand) o;
        return commandType == that.commandType && Objects.equals(authToken, that.authToken) && Objects.equals(gameID, that.gameID) && identity == that.identity && teamColor == that.teamColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(commandType, authToken, gameID, identity, teamColor);
    }

    public enum IdentityType {
        PLAYER,
        OBSERVER
    }

    public enum CommandType {
        CONNECT,
        MAKE_MOVE,
        LEAVE,
        RESIGN
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public String getAuthToken() {
        return authToken;
    }

    public Integer getGameID() {
        return gameID;
    }

    public IdentityType getIdentity() { return identity; }

    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }

}
