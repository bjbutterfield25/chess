package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.MemoryGameDAO;
import model.CreateGameRequest;
import model.CreateGameResult;
import model.GameData;

import java.util.Random;

public class GameService {
    GameDAO gameData;
    public GameService(){
        gameData = new MemoryGameDAO();
    }

    public void clear(){
        gameData.clear();
    }

    public CreateGameResult createGame(CreateGameRequest gameName) throws DataAccessException {
        if (gameName.gameName() == null || gameName.gameName().isBlank()){
            throw new DataAccessException("Error: bad request");
        }
        int gameID = Math.abs(new Random().nextInt());
        ChessGame game = new ChessGame();
        GameData newGameData = new GameData(gameID, null, null, gameName.gameName(), game);
        gameData.createGame(newGameData);
        return new CreateGameResult(gameID);
    }

}
