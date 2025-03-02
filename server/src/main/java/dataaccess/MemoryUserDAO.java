package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Collection;

public class MemoryUserDAO implements UserDAO{
    final private HashMap<String, UserData> users = new HashMap<>();

    @Override
    public void createUser(UserData userData) {
        users.put(userData.username(), userData);
    }

    @Override
    public UserData getUser(String username) {
        return users.get(username);
    }

    @Override
    public Collection<UserData> listUsers() {
        return users.values();
    }
}
