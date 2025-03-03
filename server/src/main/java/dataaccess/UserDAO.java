package dataaccess;

import model.UserData;
import java.util.Collection;

public interface UserDAO {
    public void createUser(UserData userData);
    public UserData getUser(String username);
    public Collection<UserData> listUsers();
    public void clearUsers();
}
