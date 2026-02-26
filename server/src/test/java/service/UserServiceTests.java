package service;

import dataaccess.DataAccessException;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserServiceTests {
    private static UserService service;

    @BeforeEach
    public void setup() {
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

    @Test
    public void isAuthenticatedSuccess() throws DataAccessException {
        RegisterResult registerResult = service.register(new RegisterRequest("user", "pass", "test@mail.com"));
        Assertions.assertTrue(service.isAuthenticated(registerResult.authToken()));
    }

    @Test
    public void isAuthenticatedFailure() throws DataAccessException {
        service.register(new RegisterRequest("user", "pass", "test@mail.com"));
        Assertions.assertThrows(DataAccessException.class, () -> service.isAuthenticated("bad-auth"));
    }

    @Test
    public void getAuthDataSuccess() throws DataAccessException {
        RegisterResult result = service.register(new RegisterRequest("user", "pass", "test@mail.com"));
        String token = result.authToken();
        AuthData auth = service.getAuthData(result.authToken());
        Assertions.assertNotNull(auth);
        Assertions.assertEquals("user", auth.username());
        Assertions.assertEquals(token, auth.authToken());
    }

    @Test
    public void getAuthDataFailure() {
        String badToken = "bad-auth";
        Assertions.assertNull(service.getAuthData(badToken));
    }

    @Test
    public void clearSuccess() throws DataAccessException {
        RegisterResult result = service.register(new RegisterRequest("user", "pass", "test@mail.com"));
        String token = result.authToken();
        Assertions.assertNotNull(token);
        service.clear();
        Assertions.assertNull(service.getAuthData(token));
    }

}