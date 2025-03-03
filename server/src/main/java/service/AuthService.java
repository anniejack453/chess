package service;

import dataaccess.AuthDAO;
import model.AuthData;

import java.util.Collection;

public class AuthService{
    private final AuthDAO dataAccess;

    public AuthService(AuthDAO dataAccess){
        this.dataAccess = dataAccess;
    }

    public AuthData createAuth(String username) {
        AuthData authData = dataAccess.createAuth(username);
        return authData;
    }

    public AuthData getAuth(String authToken){
        AuthData authData = dataAccess.getUserFromAuth(authToken);
        return authData;
    }

    public void deleteAuth(String authToken){
        dataAccess.deleteAuth(authToken);
    }

    public Collection<AuthData> listAuths(){
        return dataAccess.listAuths();
    }

    public void clearAuths(){
        dataAccess.clearAuths();
    }

}
