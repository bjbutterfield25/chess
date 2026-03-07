package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DataAccessTests {
    private static SQLGameDAO gameDAO;
    private static SQLUserDao userDAO;
    private static SQLAuthDao authDAO;

    @BeforeAll
    public static void setUp() throws DataAccessException{
        gameDAO = new SQLGameDAO();
        userDAO = new SQLUserDao();
        authDAO = new SQLAuthDao();
    }

    @BeforeEach
    public void reset() throws DataAccessException {
        gameDAO.clear();
        userDAO.clear();
        authDAO.clear();
    }
    //Auth Tests
    @Test
    public void clearAuthPositive() {
        Assertions.assertDoesNotThrow(()->authDAO.clear());
    }

    @Test
    public void createAuthPositive() throws DataAccessException {
        AuthData auth = new AuthData("token123","user");
        Assertions.assertDoesNotThrow(() -> authDAO.createAuth(auth));
    }

    @Test
    public void createAuthNegative() {
        Assertions.assertThrows(Exception.class, () -> authDAO.createAuth(null));
    }

    @Test
    public void getAuthPositive() throws DataAccessException {
        AuthData auth = new AuthData("token123","user");
        authDAO.createAuth(auth);
        AuthData result = authDAO.getAuth("token123");
        Assertions.assertNotNull(result);
        Assertions.assertEquals("user", result.username());
    }

    @Test
    public void getAuthNegative() throws DataAccessException {
        AuthData auth = new AuthData("token123","user");
        authDAO.createAuth(auth);
        AuthData result = authDAO.getAuth("token");
        Assertions.assertNull(result);
    }

    @Test
    public void deleteAuthPositive() throws DataAccessException {
        AuthData auth = new AuthData("token123","user");
        authDAO.createAuth(auth);
        authDAO.deleteAuth("token123");
        Assertions.assertNull(authDAO.getAuth("token123"));
    }

    @Test
    public void deleteAuthNegative() {
        Assertions.assertDoesNotThrow(() -> authDAO.deleteAuth("token123"));
    }

    //User Tests
    @Test
    public void clearUserPositive() {
        Assertions.assertDoesNotThrow(()->authDAO.clear());
    }

    @Test
    public void createUserPositive() {
        UserData user = new UserData("test", "pass", "test@test.com");
        Assertions.assertDoesNotThrow(() -> userDAO.createUser(user));
    }

    @Test
    public void createUserNegative() {
        Assertions.assertThrows(Exception.class, () -> userDAO.createUser(null));
    }

    @Test
    public void getUserPositive() throws DataAccessException {
        UserData user = new UserData("test", "pass", "test@test.com");
        userDAO.createUser(user);
        UserData result = userDAO.getUser("test");
        Assertions.assertNotNull(result);
        Assertions.assertEquals("test", result.username());
    }

    @Test
    public void getUserNegative() throws DataAccessException {
        UserData result = userDAO.getUser("test");
        Assertions.assertNull(result);
    }

    //Game Tests
    @Test
    public void clearGamePositive() {
        Assertions.assertDoesNotThrow(()->authDAO.clear());
    }

    @Test
    public void createGamePositive() throws DataAccessException {
        GameData game = new GameData(0, null, null, "TestGame", new ChessGame());
        GameData result = gameDAO.createGame(game);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.gameID() > 0);
        Assertions.assertEquals("TestGame", result.gameName());
    }

    @Test
    public void createGameNegative() {
        Assertions.assertThrows(Exception.class, () -> gameDAO.createGame(null));
    }

}
