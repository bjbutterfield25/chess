package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    void createGame(GameData gameData);
    GameData getGame(int gameID);
    void deleteGame(int gameID);
    ArrayList<GameData> listGames();
    void clear();
}
