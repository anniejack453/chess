package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import exception.ResponseException;
import model.AuthData;
import model.CreateResult;
import model.GameData;
import model.ListGamesResult;
import websocket.MoveConverter;
import websocket.ServerMessageHandler;
import websocket.WebSocketFacade;

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
    private ChessGame.TeamColor teamColor;

    public ChessClient(Integer port, ServerMessageHandler messageHandler) {
        server = new ServerFacade(port);
        this.messageHandler = messageHandler;
    }

    public void setChess(ChessGame chess) {
        this.chess = chess;
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
                    - highlight <position> - highlight legal moves
                    - resign - forfeit game
                    - help - list possible options
                    - leave - leave game
                    
                    """;
        } return """
                - redraw - redraw chess board
                - highlight <position> - highlight legal moves
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
                case "leave" -> leaveGame(authToken);
                case "redraw" -> redrawBoard();
                case "move" -> move(authToken, params);
                case "highlight" -> highlight(params);
                case "resign" -> resign(authToken);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private String highlight(String[] params) throws Exception {
        assertPostGameJoin();
        if (params.length == 1) {
            var startString = params[0];
            MoveConverter moveConverter = new MoveConverter(startString, null, null);
            try {
                ChessPosition position = moveConverter.convertStartPosition(startString);
                new PrintBoard(chess, teamColor, position);
                return "\n";
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            }
        }
        throw new Exception("Expected: <position>\n");
    }

    private String resign(String authToken) throws ResponseException {
        assertPostGameJoin();
        var listGames = gameList;
        var gameMap = listGames.get(gameNum);
        int gameID = gameMap.gameID();
        Scanner s = new Scanner(System.in);
        System.out.print("Are you sure you want to resign? (yes/no): ");
        String input = s.nextLine().trim().toLowerCase();
        if (!input.equals("yes")) {
            return "Resignation canceled.\n";
        }
        ws.resign(authToken, gameID);
        return "You forfeited the game\n";
    }

    private String move(String authToken, String[] params) throws Exception {
        assertPostGameJoin();
        if (params.length == 2) {
            var startString = params[0];
            var endString = params[1];
            MoveConverter moveConverter = new MoveConverter(startString, endString, null);
            try {
                var chessMove = moveConverter.convert(startString, endString, null);
                var listGames = gameList;
                var gameMap = listGames.get(gameNum);
                int gameID = gameMap.gameID();
                ws.makeMove(authToken, gameID, teamColor, chessMove);
                return "\n";
            } catch (Exception e) {
                throw new ResponseException(400, "Expected move\n");
            }
        } else if (params.length == 3) {
            var startString = params[0];
            var endString = params[1];
            var promoPiece = params[2];
            MoveConverter moveConverter = new MoveConverter(startString, endString, promoPiece);
            try {
                var chessMove = moveConverter.convert(startString, endString, promoPiece);
                var listGames = gameList;
                var gameMap = listGames.get(gameNum);
                int gameID = gameMap.gameID();
                ws.makeMove(authToken, gameID, teamColor, chessMove);
                return "\n";
            } catch (Exception e) {
                throw new ResponseException(400, "Expected move\n");
            }
        }
        throw new ResponseException(400, "Expected: <start position> <end position>\n");
    }

    private String redrawBoard() throws ResponseException {
        assertPostGameJoin();
        var listGames = gameList;
        var gameMap = listGames.get(gameNum);
        int gameID = gameMap.gameID();
        ws.makeMove(authToken, gameID, teamColor, null);
        return "Game board redrawn\n";
    }

    private String leaveGame(String authToken) throws ResponseException {
        assertPostGameJoin();
        var listGames = gameList;
        var gameMap = listGames.get(gameNum);
        int gameID = gameMap.gameID();
        ws.leaveGame(authToken, gameID);
        state = State.POSTLOGIN;
        assertPostLogin();
        return "You have left the game\n";
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
                var listGames = gameList;
                var gameMap = listGames.get(gameNum);
                int gameID = gameMap.gameID();
                server.joinGame(authToken, playerColor, gameID);
                teamColor = ChessGame.TeamColor.valueOf(playerColor);
                state = State.POSTJOINGAME;
                assertPostGameJoin();
                ws = new WebSocketFacade(server.serverUrl, messageHandler);
                ws.joinGame(authToken, gameID, teamColor);
                chess = gameMap.game();
                return String.format("You joined game as %s.\n", playerColor);
            }
            throw new ResponseException(400, "Number needs to be in list\n");
        }
        throw new ResponseException(400, "Expected: <ID> [WHITE|BLACK]\n");
    }

    private String observeGame(String authToken, String[] params) throws ResponseException {
        assertPostLogin();
        if (params.length == 1) {
            try {
                gameNum = Integer.parseInt(params[0]);
            } catch (Exception e) {
                return "Need to give a list number.\n";
            }
            if (gameList.containsKey(gameNum)) {
                var listGames = gameList;
                var gameMap = listGames.get(gameNum);
                ws = new WebSocketFacade(server.serverUrl, messageHandler);
                ws.observeGame(authToken, gameMap.gameID());
                teamColor = ChessGame.TeamColor.WHITE;
                state = State.POSTJOINGAME;
                assertPostGameJoin();
                return String.format("You are observing game %d\n", gameNum);
            }
        }
        throw new ResponseException(400, "Expected: <ID>\n");
    }

    private String logout(String authToken) throws ResponseException {
        assertPostLogin();
        server.logout(authToken);
        state = State.PRELOGIN;
        assertPreLogin();
        return "You have been logged out.\n";
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
                return String.format("You signed in as %s.\n", authData.username());
            }
        }
        throw new ResponseException(400, "Expected: <username> <password>\n");
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
                return String.format("You signed in as %s.\n", authData.username());
            }
        }
        throw new ResponseException(400, "Expected: <username> <password> <email>\n");
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
            return String.format("Game ID: %s\n", game.gameID());
        }
        throw new ResponseException(400, "Expected: <GameName>\n");
    }

    private void assertPostLogin() throws ResponseException {
        if (state != State.POSTLOGIN) {
            throw new ResponseException(400, "You must sign in.\n");
        }
    }

    private void assertPreLogin() throws ResponseException {
        if (state != State.PRELOGIN) {
            throw new ResponseException(400, "You are already signed in.\n");
        }
    }

    private void assertPostGameJoin() throws ResponseException {
        if (state != State.POSTJOINGAME) {
            throw new ResponseException(400, "You must join a game.\n");
        }
    }
}
