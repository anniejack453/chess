package dataaccess;

import model.AuthData;
import java.util.UUID;
import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO{
    final private HashMap<String, AuthData> authTokens = new HashMap<>();

    @Override
    public AuthData createAuth(String username) {
        AuthData authData = new AuthData(username,UUID.randomUUID().toString());
        authTokens.put(username,authData);
        return authData;
    }
}
