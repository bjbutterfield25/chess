package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    GameData createGame(GameData gameData) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    void deleteGame(int gameID) throws DataAccessException;
    ArrayList<GameData> listGames() throws DataAccessException;
    void clear() throws DataAccessException;
    void joinGame(int i, String whiteUsername, String blackUsername) throws DataAccessException;
    void updateGame(int gameID, ChessGame gameData, String whiteUsername, String blackUsername) throws DataAccessException;
}
