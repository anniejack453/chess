package ui;

import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.CreateResult;
import model.GameData;

import java.util.*;

public class ChessClient {
    private final ServerFacade server;
    private State state = State.PRELOGIN;
    private String authToken = null;
    private Map<Integer, Map> gameList = new HashMap<>();

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
                case "login" -> login(params);
                case "logout" -> logout(authToken);
                case "create" -> create(authToken, params);
                case "list" -> listGames(authToken);
                case "join" -> joinGame(authToken, params);
                case "observe" -> observeGame(authToken, params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    /*
    turn string into map to query??
    enter in number from listGames, white or black. make white or black all caps, use authtoken to get username
    from authdata to enter as player name. call joinGame.
    set postJoinGame. call class print board. white perspective if white, black perspective if black
    */

    private String joinGame(String authToken, String[] params) throws ResponseException {
        assertPostLogin();
        if (params.length == 2) {
            int gameNum = Integer.parseInt(params[0]);
            var playerColor = params[1].toUpperCase();
            var listGames = gameList;
            var game = listGames.get(gameNum);
            Double gameIDDouble = (Double) game.get("gameID");
            int gameID = (int) Math.round(gameIDDouble);
            server.joinGame(authToken, playerColor, gameID);
            state = State.POSTJOINGAME;
            // add in print board functionality
            return String.format("You joined game as %s.", playerColor);
        }
        throw new ResponseException(400, "Expected: <ID> [WHITE|BLACK]");
    }

    private String observeGame(String authToken, String[] params) throws ResponseException {
        assertPostLogin();
        if (params.length == 1) {
            int gameNum = Integer.parseInt(params[0]);
            var listGames = gameList;
            var game = listGames.get(gameNum);
            //print board of game
            state = State.POSTJOINGAME;
            return String.format("You are observing game %d", gameNum);
        }
        throw new ResponseException(400, "Expected: <ID>");
    }

    private String logout(String authToken) throws ResponseException {
        assertPostLogin();
        server.logout(authToken);
        state = State.PRELOGIN;
        return "You have been logged out.";
    }

    public String login(String... params) throws ResponseException {
        assertPreLogin();
        if (params.length == 2) {
            var username = params[0];
            var password = params[1];
            if (!username.isEmpty() && !password.isEmpty()) {
                AuthData authData = (AuthData) server.login(username, password);
                authToken = authData.authToken();
                state = State.POSTLOGIN;
                return String.format("You signed in as %s.", authData.username());
            }
        }
        throw new ResponseException(400, "Expected: <username> <password>");
    }

    public String register(String... params) throws ResponseException {
        assertPreLogin();
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

    public String listGames(String authToken) throws ResponseException {
        assertPostLogin();
        var games = (Map) server.listGames(authToken);
        var gameString = new StringBuilder();
        ArrayList gameVal = (ArrayList<GameData>) games.get("games");
        for (int i=0; i<gameVal.size(); i++) {
            var gameMap = (Map) gameVal.get(i);
            var gameName = gameMap.get("gameName");
            var whiteUsername = gameMap.get("whiteUsername");
            var blackUsername = gameMap.get("blackUsername");
            gameString.append(String.format("%d. %s - Player W: %s Player B: %s\n", i+1, gameName, whiteUsername, blackUsername));
            gameList.put(i+1,gameMap);
        }
        if (!gameString.isEmpty()) {
            return gameString.toString();
        }
        return "There are no games currently.\n";
    }

    public String create(String authToken, String... params) throws ResponseException {
        assertPostLogin();
        if (params.length == 1) {
            var game = (CreateResult) server.createGame(authToken, params[0]);
            return String.format("Game ID: %s", game.gameID());
        }
        throw new ResponseException(400, "Expected: <GameName>");
    }

    private void assertPostLogin() throws ResponseException {
        if (state != State.POSTLOGIN) {
            throw new ResponseException(400, "You must sign in.");
        }
    }

    private void assertPreLogin() throws ResponseException {
        if (state != State.PRELOGIN) {
            throw new ResponseException(400, "You are already signed in.");
        }
    }

    private void assertPostJoinGame() throws ResponseException {
        if (state != State.POSTJOINGAME) {
            throw new ResponseException(400, "You must join a game.");
        }
    }
}
