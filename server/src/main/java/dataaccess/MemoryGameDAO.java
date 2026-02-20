package dataaccess;

import model.GameData;

import java.util.ArrayList;

public class MemoryGameDAO implements GameDAO{
    ArrayList<GameData> games = new ArrayList<>();
    public void createGame(GameData gameData) {
        games.add(gameData);
    }

    public GameData updateGame(GameData newGameData) {
        return null;
    }

    public ArrayList<GameData> listGames() {
        return null;
    }

    public void clear() {
        games = new ArrayList<>();
    }
}
