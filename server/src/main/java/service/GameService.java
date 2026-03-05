package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.SQLGameDAO;
import model.*;

public class GameService {
    GameDAO gameData;
    public GameService() throws DataAccessException{
        gameData = new SQLGameDAO();
    }

    public void clear() throws DataAccessException{
        gameData.clear();
    }

    public CreateGameResult createGame(CreateGameRequest gameName) throws DataAccessException {
        if (gameName.gameName() == null || gameName.gameName().isBlank()){
            throw new DataAccessException("Error: bad request");
        }
        ChessGame game = new ChessGame();
        GameData currentGameData = new GameData(0, null, null, gameName.gameName(), game);
        GameData finalGameData = gameData.createGame(currentGameData);
        return new CreateGameResult(finalGameData.gameID());
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
