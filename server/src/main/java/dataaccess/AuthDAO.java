package dataaccess;

import model.AuthData;

public interface AuthDAO {
    public AuthData createAuth(String username);
}
