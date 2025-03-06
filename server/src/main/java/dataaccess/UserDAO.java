package dataaccess;

import model.UserData;
import java.util.Collection;

public interface UserDAO {
    public UserData createUser(UserData userData) throws Exception;
    public UserData getUser(String username);
    public Collection<UserData> listUsers();
    public void clearUsers();
}
