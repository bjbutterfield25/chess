package dataaccess;

import model.UserData;
import org.eclipse.jetty.server.Authentication;

import java.util.ArrayList;

public class MemoryUserDAO implements UserDAO{
    private ArrayList<UserData> users = new ArrayList<>();
    public void createUser(UserData userData) {
        users.add(userData);
    }

    public UserData getUser(String username) {
        for (UserData user: users){
            if (user.username().equals(username)){
                return user;
            }
        }
        return null;
    }

    public void clear() {
        users = new ArrayList<>();
    }
}
