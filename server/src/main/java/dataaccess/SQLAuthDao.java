package dataaccess;

import model.AuthData;

public class SQLAuthDao implements AuthDAO{
    public SQLAuthDao() throws DataAccessException {
        DatabaseManager.createTables();
    }

    public void createAuth(AuthData authData) throws DataAccessException {
        var statement = "INSERT INTO auths (username, authToken) VALUES (?, ?)";
        DatabaseManager.executeUpdate(statement, authData.username(), authData.authToken());
    }

    public AuthData getAuth(String authToken) {
        return null;
    }

    public void deleteAuth(String authToken) {

    }

    public void clear() throws DataAccessException {
        var statement = "TRUNCATE auths";
        DatabaseManager.executeUpdate(statement);
    }
}
