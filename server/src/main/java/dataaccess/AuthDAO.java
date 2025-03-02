package dataaccess;

import model.AuthData;

import java.util.Collection;

public interface AuthDAO {
    public AuthData createAuth(String username);
    public void deleteAuth(String authToken);
    public AuthData getUserFromAuth(String authToken);
    public Collection<AuthData> listAuths();
}
