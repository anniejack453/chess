package service;

import dataaccess.AuthDAO;
import model.AuthData;

public class AuthService{
    private final AuthDAO dataAccess;

    public AuthService(AuthDAO dataAccess){
        this.dataAccess = dataAccess;
    }

    public AuthData createAuth(String username) {
        AuthData authData = dataAccess.createAuth(username);
        return authData;
    }

}
