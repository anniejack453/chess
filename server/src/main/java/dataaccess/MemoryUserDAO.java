package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Collection;

public class MemoryUserDAO implements UserDAO{
    final private HashMap<String, UserData> users = new HashMap<>();

    @Override
    public UserData createUser(UserData userData) throws Exception {
        if (users.containsKey(userData.username())){
            throw new Exception("Error: already taken");
        } else{
            users.put(userData.username(),userData);
        }
        return userData;
    }

    @Override
    public UserData getUser(String username) {
        return users.get(username);
    }

    @Override
    public Collection<UserData> listUsers() {
        return users.values();
    }

    @Override
    public void clearUsers() {
        users.clear();
    }
}
