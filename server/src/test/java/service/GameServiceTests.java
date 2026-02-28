package service;

import dataaccess.DataAccessException;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameServiceTests {
    private static GameService service;

    @BeforeEach
    public void setup(){
        service = new GameService();
    }

    @Test
    public void createGameSuccess() throws DataAccessException {
        CreateGameResult result = service.createGame(new CreateGameRequest("test"));
        Assertions.assertTrue(result.gameID() > 0);
    }

    @Test
    public void createGameFailure() {
        Assertions.assertThrows(DataAccessException.class, () -> service.createGame(new CreateGameRequest("")));
    }

    @Test
    public void joinGameSuccess() throws DataAccessException {
        CreateGameResult result = service.createGame(new CreateGameRequest("test"));
        AuthData auth = new AuthData("token", "player");
        JoinGameRequest request = new JoinGameRequest("White", result.gameID());
        service.joinGame(request, auth);
        GameData game = service.gameData.getGame(result.gameID());
        Assertions.assertEquals("player", game.whiteUsername());
        Assertions.assertEquals("test", game.gameName());
    }

    @Test
    public void joinGameFailure() throws DataAccessException {
        CreateGameResult result = service.createGame(new CreateGameRequest("test"));
        AuthData auth = new AuthData("token", "player");
        JoinGameRequest request = new JoinGameRequest("purple", result.gameID());
        Assertions.assertThrows(DataAccessException.class, () -> service.joinGame(request, auth));
    }

    @Test
    public void listGamesSuccess() throws DataAccessException {
        service.createGame(new CreateGameRequest("test1"));
        service.createGame(new CreateGameRequest("test2"));
        service.createGame(new CreateGameRequest("test3"));
        Assertions.assertEquals(3, service.listGames().games().size());
    }

    @Test
    public void listGamesFailure() {
        Assertions.assertNotNull(service.listGames());
        Assertions.assertTrue(service.listGames().games().isEmpty());
    }

    @Test
    public void clearSuccess() throws DataAccessException {
        CreateGameResult result = service.createGame(new CreateGameRequest("test"));
        Assertions.assertNotNull(result);
        service.clear();
        Assertions.assertTrue(service.listGames().games().isEmpty());
    }
}
