package ui;

import chess.ChessGame;
import exception.ResponseException;
import model.AuthData;
import model.CreateResult;
import model.GameData;
import model.ListGamesResult;
import websocket.ServerMessageHandler;
import websocket.WebSocketFacade;
import websocket.messages.*;

import java.util.*;

public class ChessClient {
    private final ServerFacade server;
    private State state = State.PRELOGIN;
    private String authToken = null;
    private Map<Integer, GameData> gameList = new HashMap<>();
    private PrintBoard board;
    private Integer gameNum;
    private ChessGame chess;
    private final ServerMessageHandler messageHandler;
    private WebSocketFacade ws;

    public ChessClient(Integer port, ServerMessageHandler messageHandler) {
        server = new ServerFacade(port);
        this.messageHandler = messageHandler;
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
        } else if (state == State.POSTJOINGAME) {
            return """
                    - move <current position> <new position> - make chess move
                    - redraw - redraw chess board
                    - highlight - highlight legal moves
                    - resign - forfeit game
                    - help - list possible options
                    - leave - leave game
                    """;
        } return """
                - redraw - redraw chess board
                - help - list possible options
                - leave - leave game
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
                case "leave" -> leaveGame();
                case "redraw" -> redrawBoard();
                case "move" -> move(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    private String move(String[] params) throws ResponseException {
        assertPostGameJoin();
        return "";
    }

    private String redrawBoard() throws ResponseException {
        assertPostGameJoin();
        return "";
    }

    private String leaveGame() throws ResponseException { //TODO: set old player thing to null
        assertPostGameJoin();
        state = State.POSTLOGIN;
        assertPostLogin();
        return "You have left the game";
    }

    private String joinGame(String authToken, String[] params) throws ResponseException {
        assertPostLogin();
        if (params.length == 2) {
            try {
                gameNum = Integer.parseInt(params[0]);
            } catch (Exception e) {
                return "Need to give a list number.\n";
            }
            if (gameList.containsKey(gameNum)) {
                var playerColor = params[1].toUpperCase();
                ChessGame.TeamColor teamColor;
                if (playerColor.equals("WHITE")) {
                    teamColor = ChessGame.TeamColor.WHITE;
                } else {
                    teamColor = ChessGame.TeamColor.BLACK;
                }
                var listGames = gameList;
                var gameMap = listGames.get(gameNum);
                int gameID = gameMap.gameID();
                chess = gameMap.game();
                server.joinGame(authToken, playerColor, gameID);
                ws = new WebSocketFacade(server.serverUrl, messageHandler);
                ws.joinGame(authToken, gameID);
                board = new PrintBoard(chess, teamColor);
                return String.format("You joined game as %s.", playerColor);
            }
            throw new ResponseException(400, "Number needs to be in list");
        }
        throw new ResponseException(400, "Expected: <ID> [WHITE|BLACK]");
    }

    private String observeGame(String authToken, String[] params) throws ResponseException { //TODO: add websocket
        assertPostLogin();
        int gameNum;
        if (params.length == 1) {
            try {
                gameNum = Integer.parseInt(params[0]);
            } catch (Exception e) {
                return "Need to give a list number.\n";
            }
            if (gameList.containsKey(gameNum)) {
                var listGames = gameList;
                var gameMap = listGames.get(gameNum);
                board = new PrintBoard(gameMap.game(), ChessGame.TeamColor.WHITE);
                state = State.POSTJOINGAME;
                assertPostGameJoin();
                return String.format("You are observing game %d", gameNum);
            }
        }
        throw new ResponseException(400, "Expected: <ID>");
    }

    private String logout(String authToken) throws ResponseException {
        assertPostLogin();
        server.logout(authToken);
        state = State.PRELOGIN;
        assertPreLogin();
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
        ListGamesResult games = (ListGamesResult) server.listGames(authToken);
        var gameString = new StringBuilder();
        ArrayList gameVal = (ArrayList<GameData>) games.games();
        for (int i=0; i<gameVal.size(); i++) {
            var gameData = (GameData) gameVal.get(i);
            var gameName = gameData.gameName();
            var whiteUsername = gameData.whiteUsername();
            var blackUsername = gameData.blackUsername();
            gameString.append(String.format("%d. %s - Player White: %s Player Black: %s\n", i+1, gameName, whiteUsername, blackUsername));
            gameList.put(i+1,gameData);
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

    private void assertPostGameJoin() throws ResponseException {
        if (state != State.POSTJOINGAME) {
            throw new ResponseException(400, "You must join a game.");
        }
    }
}
