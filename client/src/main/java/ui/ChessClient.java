package ui;

import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.CreateResult;
import model.GameData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ChessClient {
    private final ServerFacade server;
    private State state = State.PRELOGIN;
    private String authToken = null;

    public ChessClient(Integer port) {
        server = new ServerFacade(port);
    }

    public String help() {
        if (state == State.PRELOGIN) {
            return """
                    - register <USERNAME> <PASSWORD> <EMAIL> - create account
                    - login <USERNAME> <PASSWORD>
                    - help - list possible options
                    - quit - exit program
                    """;
        } else if (state == State.POSTLOGIN) {
            return """
                    - create <NAME> - create a game
                    - join <ID> [WHITE|BLACK] - join a game
                    - list - list games
                    - observe <ID> - watch a game
                    - logout
                    - help - list possible options
                    - quit - exit program
                    """;
        }
        return """
                 - move - make chess move
                 - help - list possible options
                 - quit - exit program
                """;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "create" -> create(authToken, params);
                case "list" -> listGames(authToken);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    private String listGames(String authToken) throws ResponseException {
        assertPostLogin();
        var games = (Map) server.listGames(authToken);
        ArrayList gameVal = (ArrayList<GameData>) games.get("games");
        var result = new StringBuilder();
        var gson = new Gson();
        for (var game : gameVal) {
            result.append(gson.toJson(game)).append('\n');
        }
        return result.toString();
    }

    public String create(String authToken, String... params) throws ResponseException {
        assertPostLogin();
        if (params.length == 1) {
            var game = (CreateResult) server.createGame(authToken, params[0]);
            return String.format("Game ID: %s", game.gameID());
        }
        throw new ResponseException(400, "Expected: <GameName>");
    }

    public String register(String... params) throws ResponseException {
        if (params.length == 3) {
            var username = params[0];
            var password = params[1];
            var email = params[2];
            if (!username.isEmpty() && !password.isEmpty() && email.contains("@")) {
                AuthData authData = (AuthData) server.register(params[0], params[1], params[2]);
                authToken = authData.authToken();
                state = State.POSTLOGIN;
                return String.format("You signed in as %s.", authData.username());
            }
        }
        throw new ResponseException(400, "Expected: <username> <password> <email>");
    }

    private void assertPostLogin() throws ResponseException {
        if (state != State.POSTLOGIN) {
            throw new ResponseException(400, "You must sign in");
        }
    }

    private void assertPostJoinGame() throws ResponseException {
        if (state != State.POSTJOINGAME) {
            throw new ResponseException(400, "You must join a game");
        }
    }
}
