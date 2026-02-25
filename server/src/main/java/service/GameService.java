package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.MemoryGameDAO;
import model.*;

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

    public void joinGame(JoinGameRequest joinGameRequest, AuthData authData) throws DataAccessException {
        if (joinGameRequest.playerColor() == null){
            throw new DataAccessException("Error: bad request");
        }
        if (!joinGameRequest.playerColor().equalsIgnoreCase("White") &&
                !joinGameRequest.playerColor().equalsIgnoreCase("Black")) {
            throw new DataAccessException("Error: bad request");
        }
        GameData currentGameData = gameData.getGame(joinGameRequest.gameID());
        String whiteUsername;
        String blackUsername;
        if (currentGameData == null){
            throw new DataAccessException("Error: bad request");
        }
        if (joinGameRequest.playerColor().equalsIgnoreCase("White")){
            if (currentGameData.whiteUsername() == null){
                whiteUsername = authData.username();
                blackUsername = currentGameData.blackUsername();
            } else {
                throw new DataAccessException("Error: already taken");
            }
        } else {
            if (currentGameData.blackUsername() == null){
                blackUsername = authData.username();
                whiteUsername = currentGameData.whiteUsername();
            } else {
                throw new DataAccessException("Error: already taken");
            }
        }
        GameData updatedGame = new GameData(joinGameRequest.gameID(), whiteUsername,
                blackUsername, currentGameData.gameName(), currentGameData.game());
        gameData.deleteGame(currentGameData.gameID());
        gameData.createGame(updatedGame);
    }

    public ListGamesResult listGames(){
        return new ListGamesResult(gameData.listGames());
    }
}
