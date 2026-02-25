package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.*;
import model.*;

import java.util.Map;

public class Server {

    private final Javalin javalin;
    private ServerHandler handler = new ServerHandler();

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        registerEndpoint(javalin);
        clearEndpoint(javalin);
        loginEndpoint(javalin);
        logoutEndpoint(javalin);
        createGameEndpoint(javalin);
        joinGameEndpoint(javalin);
        listGamesEndpoint(javalin);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    public void registerEndpoint(Javalin server){
        server.post("/user", ctx -> {
            var serializer = new Gson();
            try{
                RegisterRequest request = serializer.fromJson(ctx.body(), RegisterRequest.class);
                RegisterResult response = handler.register(request);
                ctx.status(200).result(serializer.toJson(response));
            } catch (DataAccessException e) {
                String message = e.getMessage();
                if (message.equals("Error: already taken")) {
                    ctx.status(403).result(serializer.toJson(Map.of("message", message)));
                } else if (message.equals("Error: bad request")) {
                    ctx.status(400).result(serializer.toJson(Map.of("message", message)));
                } else {
                    ctx.status(500).result(serializer.toJson(Map.of("message", message)));
                }
            }
        }
        );
    }

    public void clearEndpoint(Javalin server){
        server.delete("/db", ctx -> {
            handler.clear();
            ctx.status(200);
        });
    }

    public void loginEndpoint(Javalin server){
        server.post("/session", ctx -> {
            var serializer = new Gson();
            try{
                LoginRequest request = serializer.fromJson(ctx.body(), LoginRequest.class);
                LoginResult response = handler.login(request);
                ctx.status(200).result(serializer.toJson(response));
            } catch (DataAccessException e) {
                String message = e.getMessage();
                if (message.equals("Error: unauthorized")) {
                    ctx.status(401).result(serializer.toJson(Map.of("message", message)));
                } else if (message.equals("Error: bad request")) {
                    ctx.status(400).result(serializer.toJson(Map.of("message", message)));
                } else {
                    ctx.status(500).result(serializer.toJson(Map.of("message", message)));
                }
            }
        }
        );
    }

    public void logoutEndpoint(Javalin server){
        server.delete("/session", ctx -> {
            var serializer = new Gson();
            try{
                String authToken = ctx.header("Authorization");
                handler.logout(authToken);
                ctx.status(200);
            } catch (DataAccessException e) {
                String message = e.getMessage();
                if (message.equals("Error: unauthorized")) {
                    ctx.status(401).result(serializer.toJson(Map.of("message", message)));
                } else {
                    ctx.status(500).result(serializer.toJson(Map.of("message", message)));
                }
            }
        }
        );
    }

    public void createGameEndpoint(Javalin server){
        server.post("/game", ctx -> {
                    var serializer = new Gson();
                    try{
                        String authToken = ctx.header("Authorization");
                        CreateGameRequest createGameRequest = serializer.fromJson(ctx.body(), CreateGameRequest.class);
                        CreateGameResult response = handler.createGame(authToken, createGameRequest);
                        ctx.status(200).result(serializer.toJson(response));
                    } catch (DataAccessException e) {
                        String message = e.getMessage();
                        if (message.equals("Error: unauthorized")) {
                            ctx.status(401).result(serializer.toJson(Map.of("message", message)));
                        } else if (message.equals("Error: bad request")) {
                            ctx.status(400).result(serializer.toJson(Map.of("message", message)));
                        } else {
                            ctx.status(500).result(serializer.toJson(Map.of("message", message)));
                        }
                    }
                }
        );
    }

    public void joinGameEndpoint(Javalin server){
        server.put("/game", ctx -> {
            var serializer = new Gson();
            try{
                String authToken = ctx.header("Authorization");
                JoinGameRequest joinGameRequest = serializer.fromJson(ctx.body(), JoinGameRequest.class);
                handler.joinGame(authToken, joinGameRequest);
                ctx.status(200);
            } catch (DataAccessException e) {
                String message = e.getMessage();
                if (message.equals("Error: unauthorized")) {
                    ctx.status(401).result(serializer.toJson(Map.of("message", message)));
                } else if (message.equals("Error: bad request")) {
                    ctx.status(400).result(serializer.toJson(Map.of("message", message)));
                } else if (message.equals("Error: already taken")){
                    ctx.status(403).result(serializer.toJson(Map.of("message", message)));
                } else {
                    ctx.status(500).result(serializer.toJson(Map.of("message", message)));
                }
            }
        });
    }

    public void listGamesEndpoint(Javalin server){
        server.get("/game", ctx -> {
            var serializer = new Gson();
            try {
                String authToken = ctx.header("Authorization");
                ListGamesResult response =  handler.listGames(authToken);
                ctx.status(200).result(serializer.toJson(response));
            }
            catch (DataAccessException e) {
                String message = e.getMessage();
                if (message.equals("Error: unauthorized")) {
                    ctx.status(401).result(serializer.toJson(Map.of("message", message)));
                } else {
                    ctx.status(500).result(serializer.toJson(Map.of("message", message)));
                }
            }
        });
    }
}
