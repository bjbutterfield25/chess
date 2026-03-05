package dataaccess;

import model.UserData;

public class SQLUserDao implements UserDAO {
    public SQLUserDao() throws DataAccessException {
        DatabaseManager.createTables();
    }

    public void createUser(UserData userData) {

    }

    public UserData getUser(String username) {
        return null;
    }

    public void clear() {

    }
}
