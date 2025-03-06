package dataaccess;

import model.AuthData;

import java.util.Collection;

public interface AuthDAO {
    public AuthData createAuth(String username) throws Exception;
    public void deleteAuth(String authToken);
    public AuthData getUserFromAuth(String authToken) throws Exception;
    public Collection<AuthData> listAuths();

    void clearAuths();
}
