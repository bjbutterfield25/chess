package service;

import dataaccess.DataAccessException;
import model.RegisterRequest;
import model.RegisterResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class UserServiceTests {
    private static UserService service;
    @BeforeAll
    public static void setup(){
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

}
