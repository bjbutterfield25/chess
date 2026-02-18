package server;

import dataaccess.DataAccessException;
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
}
