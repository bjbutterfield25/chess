package dataaccess;

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
    //User Tests
    @Test
    public void clearUserPositive() {
        Assertions.assertDoesNotThrow(()->authDAO.clear());
    }
    //Game Tests
    @Test
    public void clearGamePositive() {
        Assertions.assertDoesNotThrow(()->authDAO.clear());
    }
}
