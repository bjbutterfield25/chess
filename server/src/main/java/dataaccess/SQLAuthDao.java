package dataaccess;

import model.AuthData;

public class SQLAuthDao implements AuthDAO{
    public SQLAuthDao() throws DataAccessException {
        DatabaseManager.createTables();
    }

    public void createAuth(AuthData authData) {

    }

    public AuthData getAuth(String authToken) {
        return null;
    }

    public void deleteAuth(String authToken) {

    }

    public void clear() {

    }
}
