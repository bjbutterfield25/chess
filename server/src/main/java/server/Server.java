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
        createHandlers(javalin);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    public void createHandlers(Javalin server){
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

}
