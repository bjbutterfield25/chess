package server;

import dataaccess.DataAccessException;
import model.*;
import service.GameService;
import service.UserService;

public class ServerHandler {
    UserService userService;
    GameService gameService;
    public ServerHandler(){
        userService = new UserService();
        gameService = new GameService();
    }
    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        return userService.register(registerRequest);
    }

    public void clear(){
        userService.clear();
        gameService.clear();
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        return userService.login(loginRequest);
    }

    public void logout(String authToken) throws DataAccessException {
        userService.logout(authToken);
    }

    public CreateGameResult createGame(String authToken, CreateGameRequest gameName) throws DataAccessException {
        userService.isAuthenticated(authToken);
        return gameService.createGame(gameName);
    }


}
