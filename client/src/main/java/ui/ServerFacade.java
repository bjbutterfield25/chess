package ui;

import model.*;

import java.net.*;
import java.net.http.*;

public class ServerFacade {
    private final String serverUrl;
    private final ClientCommunicator communicator;

    public ServerFacade(String url){
        serverUrl = url;
        communicator = new ClientCommunicator(serverUrl);
    }

    public RegisterResult register(RegisterRequest req) throws ResponseException {
        HttpRequest request = communicator.buildRequest("POST", "/user", req, null);
        HttpResponse<String> response = communicator.sendRequest(request);
        return communicator.handleResponse(response, RegisterResult.class);
    }

    public LoginResult login(LoginRequest req) throws ResponseException {
        HttpRequest request = communicator.buildRequest("POST", "/session", req, null);
        HttpResponse<String> response = communicator.sendRequest(request);
        return communicator.handleResponse(response, LoginResult.class);
    }

    public void logout(String authToken) throws ResponseException {
        HttpRequest request = communicator.buildRequest("DELETE", "/session", null, authToken);
        HttpResponse<String> response = communicator.sendRequest(request);
        communicator.handleResponse(response, null);
    }

    public CreateGameResult create(CreateGameRequest req, String authToken) throws ResponseException {
        HttpRequest request = communicator.buildRequest("POST", "/game", req, authToken);
        HttpResponse<String> response = communicator.sendRequest(request);
        return communicator.handleResponse(response, CreateGameResult.class);
    }

    public ListGamesResult list(String authToken) throws ResponseException {
        HttpRequest request = communicator.buildRequest("GET", "/game", null, authToken);
        HttpResponse<String> response = communicator.sendRequest(request);
        return communicator.handleResponse(response, ListGamesResult.class);
    }

    public void join(JoinGameRequest req, String authToken) throws ResponseException {
        HttpRequest request = communicator.buildRequest("PUT", "/game", req, authToken);
        HttpResponse<String> response = communicator.sendRequest(request);
        communicator.handleResponse(response, null);
    }

    public void clear() throws ResponseException {
        HttpRequest request = communicator.buildRequest("DELETE", "/db", null, null);
        HttpResponse<String> response = communicator.sendRequest(request);
        communicator.handleResponse(response, null);
    }
}
