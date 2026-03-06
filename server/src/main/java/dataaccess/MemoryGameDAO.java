package dataaccess;

import model.GameData;

import java.util.ArrayList;

public class MemoryGameDAO implements GameDAO{
    ArrayList<GameData> games = new ArrayList<>();
    public GameData createGame(GameData gameData) {
        games.add(gameData);
        return gameData;
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

    public void joinGame(int i, String whiteUsername, String blackUsername) throws DataAccessException {
        //doesn't do anything because it had to be added when the method was added for SQLGameDAO
    }
}
