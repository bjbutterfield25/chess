package client;

import model.*;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ResponseException;
import ui.ServerFacade;


public class ServerFacadeTests {
    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        String url = "http://localhost:" + port;
        facade = new ServerFacade(url);
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    public void clearDatabase() throws Exception {
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerPositive() throws Exception {
        var result = facade.register(new RegisterRequest("test", "test", "test@test.com"));
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.authToken());
    }

    @Test
    public void registerNegative() throws Exception {
        facade.register(new RegisterRequest("test", "test", "test@test.com"));
        Assertions.assertThrows(ResponseException.class, () ->
                facade.register(new RegisterRequest("test", "test", "test@test.com")));
    }

    @Test
    public void loginPositive() throws Exception {
        facade.register(new RegisterRequest("test", "test", "test@test.com"));
        var result = facade.login(new LoginRequest("test", "test"));
        Assertions.assertNotNull(result.authToken());
    }

    @Test
    public void loginNegative() {
        Assertions.assertThrows(ResponseException.class, () -> facade.login(new LoginRequest("test", "test")));
    }

    @Test
    public void logoutPositive() throws Exception {
        var register = facade.register(new RegisterRequest("test", "test", "test@test.com"));
        Assertions.assertDoesNotThrow(() -> facade.logout(register.authToken()));
    }

    @Test
    public void logoutNegative() {
        Assertions.assertThrows(ResponseException.class, () -> facade.logout("badauth"));
    }

    @Test
    public void createPositive() throws Exception {
        var register = facade.register(new RegisterRequest("test", "test", "test@test.com"));
        var result = facade.create(new CreateGameRequest("test"), register.authToken());
        Assertions.assertNotNull(result);
    }

    @Test
    public void createNegative() {
        Assertions.assertThrows(ResponseException.class, () ->
                facade.create(new CreateGameRequest("test"), "badauth"));
    }

    @Test
    public void listPositive() throws Exception {
        var register = facade.register(new RegisterRequest("test", "test", "test@test.com"));
        facade.create(new CreateGameRequest("test"), register.authToken());
        facade.create(new CreateGameRequest("test2"), register.authToken());
        facade.create(new CreateGameRequest("test3"), register.authToken());
        var result = facade.list(register.authToken());
        Assertions.assertNotNull(result);
        Assertions.assertEquals(3, result.games().size());
    }

    @Test
    public void listNegative() {
        Assertions.assertThrows(ResponseException.class, () -> facade.list("badauth"));
    }

    @Test
    public void joinPositive() throws Exception {
        var register = facade.register(new RegisterRequest("test", "test", "test@test.com"));
        var result = facade.create(new CreateGameRequest("test"), register.authToken());
        Assertions.assertDoesNotThrow(() -> facade.join(new JoinGameRequest("WHITE", result.gameID()), register.authToken()));
    }

    @Test
    public void joinNegative() throws Exception {
        var register = facade.register(new RegisterRequest("test", "test", "test@test.com"));
        Assertions.assertThrows(ResponseException.class, () ->
                facade.join(new JoinGameRequest("WHITE", 9999), register.authToken()));
    }

    @Test
    public void clearPositive() {
        Assertions.assertDoesNotThrow(() -> facade.clear());
    }
}
