package dataaccess;

import model.AuthData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SQLAuthDao implements AuthDAO{
    public SQLAuthDao() throws DataAccessException {
        DatabaseManager.createTables();
    }

    public void createAuth(AuthData authData) throws DataAccessException {
        var statement = "INSERT INTO auths (username, authToken) VALUES (?, ?)";
        DatabaseManager.executeUpdate(statement, authData.username(), authData.authToken());
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, username FROM auths WHERE authToken=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setNString(1, authToken);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new AuthData(rs.getString("authToken"), rs.getString("username"));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        var statement = "DELETE FROM auths WHERE authToken = ?";
        DatabaseManager.executeUpdate(statement, authToken);
    }

    public void clear() throws DataAccessException {
        var statement = "TRUNCATE auths";
        DatabaseManager.executeUpdate(statement);
    }
}
