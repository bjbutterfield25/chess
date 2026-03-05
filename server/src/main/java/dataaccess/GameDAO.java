package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    GameData createGame(GameData gameData) throws DataAccessException;
    GameData getGame(int gameID);
    void deleteGame(int gameID);
    ArrayList<GameData> listGames();
    void clear() throws DataAccessException;
}
