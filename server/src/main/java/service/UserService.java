package service;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;

import java.util.Collection;

public class UserService {
    private final UserDAO dataAccess;

    public UserService(UserDAO dataAccess) {
        this.dataAccess = dataAccess;
    }

    public UserData getUser(String username) throws DataAccessException {
        return dataAccess.getUser(username);
    }

    public UserData createUser(UserData userData) throws Exception{
        return dataAccess.createUser(userData);
    }

    public Collection<UserData> listUsers() throws DataAccessException {
        return dataAccess.listUsers();
    }

    public void clearUsers() throws DataAccessException {
        dataAccess.clearUsers();
    }

}
