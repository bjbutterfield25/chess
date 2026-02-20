package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    void createGame(GameData gameData);
    GameData updateGame(GameData newGameData);
    ArrayList<GameData> listGames();
    void clear();
}
