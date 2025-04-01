package ui;

import com.google.gson.Gson;
import exception.ResponseException;
import model.*;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Map;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(Integer port){
        serverUrl = "http://localhost:" + port;
    }

    public Object register(String username, String password, String email) throws ResponseException {
        var path = "/user";
        UserData userData = new UserData(username, password, email);
        return this.makeRequest("POST", path, null, userData, AuthData.class);
    }

    public Object login(String username, String password) throws ResponseException {
        var path = "/session";
        LoginRequest loginRequest = new LoginRequest(username, password);
        return this.makeRequest("POST", path, null, loginRequest, AuthData.class);
    }

    public void logout(String authToken) throws ResponseException {
        var path = "/session";
        this.makeRequest("DELETE", path, authToken, null, null);
    }

    public void delete() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null, null);
    }

    public Object createGame(String authToken, String gameName) throws ResponseException {
        var path = "/game";
        CreateGameRequest createGameReq = new CreateGameRequest(gameName);
        return this.makeRequest("POST", path, authToken, createGameReq, CreateResult.class);
    }

    public void joinGame(String authToken, String playerColor, Integer gameID) throws ResponseException {
        var path = "/game";
        JoinRequest joinReq = new JoinRequest(playerColor, gameID);
        this.makeRequest("PUT", path, authToken, joinReq, null);
    }

    public Object listGames(String authToken) throws ResponseException {
        var path = "/game";
        return this.makeRequest("GET", path, authToken, null, ListGamesResult.class);
    }

    private <T> T makeRequest(String method, String path, String authToken, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeHeader(authToken, http);
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeHeader(String authToken, HttpURLConnection http) {
        if (authToken != null) {
            http.addRequestProperty("authorization", authToken);
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw ResponseException.fromJson(status, respErr);
                }
            }
            throw new ResponseException(status, "other failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
