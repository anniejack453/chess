package server;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import service.AuthService;
import service.LoginRequest;
import service.LogoutRequest;
import service.UserService;
import spark.*;
import com.google.gson.Gson;
import java.util.*;
import handler.encoderDecoder;
import model.*;

public class Server extends encoderDecoder {
    private final UserService userService = new UserService(new MemoryUserDAO());
    private final AuthService authService = new AuthService(new MemoryAuthDAO());

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user",this::register);
        Spark.post("/session",this::login);
        Spark.delete("/session",this::logout);
        Spark.delete("/db",this::delete);

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
        return null;
    }

    private Object register(Request registerRequest, Response registerResponse) throws Exception {
        UserData registerReq = (UserData) decode(registerRequest,UserData.class);
        var userData = userService.getUser(registerReq.username());
        if (userData == null){
            userData = userService.createUser(registerReq);
        } else {
            return throwError403(registerRequest, registerResponse);
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
            return errorHandler(new Exception("Incorrect password"), request, response);
        }
        var authData = authService.createAuth(userData.username());
        var body = encode(Map.of("username",authData.username(),"authToken",authData.authToken()));
        response.body(body);
        return body;
    }
//FIXME: req.headers() cannot find Authorization
    private Object logout(Request req, Response res){
        LogoutRequest logoutReq = new Gson().fromJson(req.headers("Authorization: "), LogoutRequest.class);
//                (LogoutRequest) decodeHeader(req, LogoutRequest.class);
        var authData = authService.getAuth(logoutReq.authToken());
        if (!Objects.equals(authData.authToken(), logoutReq.authToken())){
            return throwError401(req, res);
        }
        authService.listAuths();
        if (authData.authToken() != null){
            authService.deleteAuth(authData.authToken());
        }
        authService.listAuths();
        return res.body();
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
