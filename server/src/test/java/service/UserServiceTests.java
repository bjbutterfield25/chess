package service;

import dataaccess.DataAccessException;
import model.LoginRequest;
import model.LoginResult;
import model.RegisterRequest;
import model.RegisterResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class UserServiceTests {
    private static UserService service;

    @BeforeAll
    public static void setup() {
        service = new UserService();
    }

    @Test
    public void registerSuccess() throws DataAccessException {
        RegisterRequest request = new RegisterRequest("test", "hello", "test@mail.com");
        RegisterResult result = service.register(request);
        Assertions.assertNotNull(result);
        Assertions.assertEquals("test", result.username());
        Assertions.assertNotNull(result.authToken());
    }

    @Test
    public void registerFailure() {
        RegisterRequest request = new RegisterRequest("", "hello", "test@mail.com");
        Assertions.assertThrows(DataAccessException.class, () -> service.register(request));
    }

    @Test
    public void loginSuccess() throws DataAccessException {
        service.register(new RegisterRequest("user", "pass", "test@mail.com"));
        LoginRequest request = new LoginRequest("user", "pass");
        LoginResult result = service.login(request);
        Assertions.assertNotNull(result.authToken());
        Assertions.assertEquals("user", result.username());
    }

    @Test
    public void loginFailure() throws DataAccessException {
        service.register(new RegisterRequest("user", "pass", "test@mail.com"));
        LoginRequest request = new LoginRequest("user", "password");
        Assertions.assertThrows(DataAccessException.class, () -> service.login(request));
    }

    @Test
    public void logoutSuccess() throws DataAccessException {
        RegisterResult registerResult = service.register(new RegisterRequest("user", "pass", "test@mail.com"));
        Assertions.assertDoesNotThrow(() -> service.logout(registerResult.authToken()));
    }

    @Test
    public void logoutFailure() throws DataAccessException {
        service.register(new RegisterRequest("user", "pass", "test@mail.com"));
        Assertions.assertThrows(DataAccessException.class, () -> service.logout("bad-auth"));
    }
}