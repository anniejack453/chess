package server;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import service.*;
import spark.*;
import com.google.gson.Gson;
import java.util.*;
import handler.EncoderDecoder;
import model.*;

public class Server extends EncoderDecoder {
    private final UserService userService = new UserService(new MemoryUserDAO());
    private final AuthService authService = new AuthService(new MemoryAuthDAO());
    private final GameService gameService = new GameService(new MemoryGameDAO());

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user",this::register);
        Spark.post("/session",this::login);
        Spark.delete("/session",this::logout);
        Spark.delete("/db",this::delete);
        Spark.get("/game",this::listGames);
        Spark.post("/game",this::createGame);
        Spark.put("/game",this::joinGame);

        Spark.get("/error",this::throwError400);
        Spark.exception(Exception.class, this::errorHandler);
        Spark.notFound((req, res) -> {
            var msg = String.format("[%s] %s not found", req.requestMethod(), req.pathInfo());
            return errorHandler(new Exception(msg), req, res);
        });

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }


    private Object delete(Request request, Response response) {
        userService.clearUsers();
        authService.clearAuths();
        gameService.clearGames();
        if (gameService.listGames().isEmpty() && userService.listUsers().isEmpty() && authService.listAuths().isEmpty()){
            return Map.of();
        }
        return errorHandler(new Exception(),request,response);
    }

    private Object register(Request registerRequest, Response registerResponse) throws Exception {
        UserData registerReq = (UserData) decode(registerRequest,UserData.class);
        var userData = userService.getUser(registerReq.username());
        if (registerReq.password() != null && registerReq.username() != null && registerReq.email() != null){
            if (userData == null){
                userData = userService.createUser(registerReq);
            } else {
                return throwError403(registerRequest, registerResponse);
            }
        } else {
            return throwError400(registerRequest,registerResponse);
        }
        var authData = authService.createAuth(userData.username());
        var body = encode(Map.of("username",authData.username(),"authToken",authData.authToken()));
        registerResponse.body(body);
        return body;
    }

    private Object login(Request request, Response response) {
        LoginRequest logReq = (LoginRequest) decode(request, LoginRequest.class);
        var userData = userService.getUser(logReq.username());
        if (userData == null){
            return throwError401(request, response);
        }
        if (!Objects.equals(logReq.password(), userData.password())){
            return throwError401(request, response);
        }
        var authData = authService.createAuth(userData.username());
        var body = encode(Map.of("username",authData.username(),"authToken",authData.authToken()));
        response.body(body);
        return body;
    }

    private Object logout(Request req, Response res){
        String logoutReq = decodeHeader(req, AuthRequest.class);
        var authData = authService.getAuth(logoutReq);
        if (!authService.listAuths().contains(authData)){
            return throwError401(req,res);
        }
        if (!Objects.equals(authData.authToken(), logoutReq)){
            return throwError401(req, res);
        }
        if (authData.authToken() != null){
            authService.deleteAuth(authData.authToken());
        }
        return Map.of();
    }

    private Object joinGame(Request req, Response res) throws Exception {
        String authReq = decodeHeader(req, AuthRequest.class);
        JoinRequest joinReq = (JoinRequest) decode(req, JoinRequest.class);
        var authData = authService.getAuth(authReq);
        if (!authService.listAuths().contains(authData)){
            return throwError401(req,res);
        }
        if (!Objects.equals(authData.authToken(), authReq)){
            return throwError401(req, res);
        }
        var gameName = gameService.getGameID(joinReq.gameID());
        if (gameName == null) {
            return throwError400(req,res);
        }
        GameData gameUpdate = gameService.joinGame(gameName, joinReq.playerColor(), authData.username());
        if (gameUpdate == null && !Objects.equals(joinReq.playerColor(), "WHITE") && !Objects.equals(joinReq.playerColor(), "BLACK")){
            return throwError400(req,res);
        } else if (gameUpdate == null && joinReq.playerColor().equals("BLACK")){
            return throwError403(req,res);
        } else if (gameUpdate == null && joinReq.playerColor().equals("WHITE")){
            return throwError403(req,res);
        }
        return Map.of();
    }

    private Object createGame(Request req, Response res) {
        String listReq = decodeHeader(req, AuthRequest.class);
        CreateGameRequest createGameReq = (CreateGameRequest) decode(req, CreateGameRequest.class);
        var authData = authService.getAuth(listReq);
        if (!authService.listAuths().contains(authData)){
            return throwError401(req,res);
        }
        if (!Objects.equals(authData.authToken(), listReq)){
            return throwError401(req, res);
        }
        var gameData = gameService.getGame(createGameReq.gameName());
        if (gameData == null){
            gameData = gameService.createGame(createGameReq.gameName());
        } else {
            return throwError403(req, res);
        }
        var body = encode(Map.of("gameID",gameData.gameID()));
        res.body(body);
        return body;
    }

    private Object listGames(Request req, Response res) {
        String listReq = decodeHeader(req, AuthRequest.class);
        var authData = authService.getAuth(listReq);
        if (!authService.listAuths().contains(authData)){
            return throwError401(req,res);
        }
        if (!Objects.equals(authData.authToken(), listReq)){
            return throwError401(req, res);
        }
        var gameList = gameService.listGames();
        var body = encode(Map.of("games",gameList));
        res.body(body);
        return body;
    }

    private Object throwError401(Request req, Response res) {
        var body = new Gson().toJson(Map.of("message", String.format("Error: unauthorized")));
        res.type("application/json");
        res.status(401);
        res.body(body);
        return body;
    }

    private Object throwError400(Request req, Response res) {
        var body = new Gson().toJson(Map.of("message", String.format("Error: bad request")));
        res.type("application/json");
        res.status(400);
        res.body(body);
        return body;
    }

    private Object throwError403(Request req, Response res) {
        var body = new Gson().toJson(Map.of("message", String.format("Error: already taken")));
        res.type("application/json");
        res.status(403);
        res.body(body);
        return body;
    }

    public Object errorHandler(Exception e, Request req, Response res) {
        var body = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage())));
        res.type("application/json");
        res.status(500);
        res.body(body);
        return body;
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
