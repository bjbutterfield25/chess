package dataaccess;

import model.GameData;

import java.util.ArrayList;

public class MemoryGameDAO implements GameDAO{
    ArrayList<GameData> games = new ArrayList<>();
    public void createGame(GameData gameData) {
        games.add(gameData);
    }

    public void deleteGame(int gameID) {
        games.remove(getGame(gameID));
    }

    public ArrayList<GameData> listGames() {
        return games;
    }

    public GameData getGame(int gameID) {
        for (GameData game: games){
            if (game.gameID() == gameID){
                return game;
            }
        }
        return null;
    }

    public void clear() {
        games = new ArrayList<>();
    }
}
