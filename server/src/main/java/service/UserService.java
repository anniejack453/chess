package service;

import dataaccess.UserDAO;
import model.UserData;

import java.util.Collection;

public class UserService {
    private final UserDAO dataAccess;

    public UserService(UserDAO dataAccess) {
        this.dataAccess = dataAccess;
    }

    public UserData getUser(String username) {
        return dataAccess.getUser(username);
    }

    public UserData createUser(UserData userData) throws Exception{
        if (dataAccess.listUsers().contains(userData)){
            throw new Exception("Error: already taken");
        } else{
            dataAccess.createUser(userData);
        }
        return userData;
    }

    public Collection<UserData> listUsers(){
        return dataAccess.listUsers();
    }

    public void clearUsers(){
        dataAccess.clearUsers();
    }

}
