package service;

import model.*;
import dataaccess.*;
import org.eclipse.jetty.util.log.Log;

import java.util.UUID;

public class UserService {
    UserDAO userDAO;
    AuthDAO authDAO;
    public UserService(){
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDao();
    }

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        if (registerRequest.username() == null || registerRequest.password() == null || registerRequest.email() == null
            || registerRequest.username().isBlank() || registerRequest.password().isBlank() || registerRequest.email().isBlank()){
            throw new DataAccessException("Error: bad request");
        }
        if (userDAO.getUser(registerRequest.username()) != null){
            throw new DataAccessException("Error: already taken");
        }
        UserData user = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
        userDAO.createUser(user);
        AuthData auth = new AuthData(UUID.randomUUID().toString(), registerRequest.username());
        authDAO.createAuth(auth);
        return new RegisterResult(auth.username(), auth.authToken());
    }

    public void clear(){
        userDAO.clear();
        authDAO.clear();
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        if(loginRequest.username() == null || loginRequest.password() == null ||
                loginRequest.username().isBlank() || loginRequest.password().isBlank()){
            throw new DataAccessException("Error: bad request");
        }
        if(userDAO.getUser(loginRequest.username()) == null || !userDAO.getUser(loginRequest.username()).password().equals(loginRequest.password())){
            throw new DataAccessException("Error: unauthorized");
        }
        AuthData auth = new AuthData(UUID.randomUUID().toString(), loginRequest.username());
        authDAO.createAuth(auth);
        return new LoginResult(auth.username(), auth.authToken());
    }

    public void logout(String authToken) throws DataAccessException {
        AuthData auth = authDAO.getAuth(authToken);
        if (auth == null ) {
            throw new DataAccessException("Error: unauthorized");
        }
        authDAO.deleteAuth(authToken);
    }

}
