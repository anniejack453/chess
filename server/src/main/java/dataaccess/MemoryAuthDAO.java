package dataaccess;

import model.AuthData;

import java.util.Collection;
import java.util.UUID;
import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO{
    final private HashMap<String, AuthData> authTokens = new HashMap<>();

    @Override
    public AuthData createAuth(String username) throws Exception{
        if (username == null || username.isEmpty()){
            throw new Exception("Username cannot be empty");
        }
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken,username);
        authTokens.put(authToken,authData);
        return authData;
    }

    @Override
    public void deleteAuth(String authToken) {
        authTokens.remove(authToken);
    }

    @Override
    public AuthData getUserFromAuth(String authToken) throws Exception {
        if (authToken == null || authToken.isEmpty()){
            throw new Exception("Need authToken");
        }
        return authTokens.get(authToken);
    }

    @Override
    public Collection<AuthData> listAuths() {
        return authTokens.values();
    }

    @Override
    public void clearAuths() {
        authTokens.clear();
    }
}
