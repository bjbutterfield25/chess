package service;

import dataaccess.DataAccessException;
import model.CreateGameRequest;
import model.CreateGameResult;
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
}
