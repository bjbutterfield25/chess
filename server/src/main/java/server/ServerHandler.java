package server;

import dataaccess.DataAccessException;
import model.LoginRequest;
import model.LoginResult;
import model.RegisterRequest;
import model.RegisterResult;
import service.UserService;

public class ServerHandler {
    UserService userService;
    public ServerHandler(){
        userService = new UserService();
    }
    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        return userService.register(registerRequest);
    }

    public void clear(){
        userService.clear();
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        return userService.login(loginRequest);
    }

    public void logout(String authToken) throws DataAccessException {
        userService.logout(authToken);
    }

}
