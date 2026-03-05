package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

public class SQLUserDao implements UserDAO {
    public SQLUserDao() throws DataAccessException {
        DatabaseManager.createTables();
    }

    public void createUser(UserData userData) throws DataAccessException {
        var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        String hash = BCrypt.hashpw(userData.password(), BCrypt.gensalt());
        DatabaseManager.executeUpdate(statement, userData.username(), hash, userData.email());
    }

    public UserData getUser(String username) {
        return null;
    }

    public void clear() throws DataAccessException {
        var statement = "TRUNCATE users";
        DatabaseManager.executeUpdate(statement);
    }
}
