package dataaccess;

import com.google.gson.Gson;
import model.GameData;

import java.util.ArrayList;

public class SQLGameDAO implements GameDAO {
    public SQLGameDAO() throws DataAccessException {
        DatabaseManager.createTables();
    }

    public GameData createGame(GameData gameData) throws DataAccessException {
        var statement = "INSERT INTO games (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
        var game = new Gson().toJson(gameData.game());
        int gameID = DatabaseManager.executeUpdate(statement, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), game);
        return new GameData(gameID, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), gameData.game());
    }

    public GameData getGame(int gameID) {
        return null;
    }

    public void deleteGame(int gameID) {

    }

    public ArrayList<GameData> listGames() {
        return null;
    }

    public void clear() throws DataAccessException {
        var statement = "TRUNCATE games";
        DatabaseManager.executeUpdate(statement);
    }
}
