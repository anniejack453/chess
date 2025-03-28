package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;

import java.util.Collection;

public class AuthService{
    private final AuthDAO dataAccess;

    public AuthService(AuthDAO dataAccess){
        this.dataAccess = dataAccess;
    }

    public AuthData createAuth(String username) throws Exception{
        AuthData authData = dataAccess.createAuth(username);
        return authData;
    }

    public AuthData getAuth(String authToken) throws Exception {
        AuthData authData = dataAccess.getUserFromAuth(authToken);
        return authData;
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        dataAccess.deleteAuth(authToken);
    }

    public Collection<AuthData> listAuths() throws DataAccessException {
        return dataAccess.listAuths();
    }

    public void clearAuths() throws DataAccessException {
        dataAccess.clearAuths();
    }

}
