package server.websocket;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsCloseHandler;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsConnectHandler;
import io.javalin.websocket.WsMessageContext;
import io.javalin.websocket.WsMessageHandler;
import org.eclipse.jetty.websocket.api.Session;
import model.AuthData;
import service.UserService;
import websocket.commands.UserGameCommand;
import websocket.messages.*;
import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx) throws IOException {
        try {
            UserGameCommand command = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            String username = "";
            try {
                UserService userService = new UserService();
                AuthData authData = userService.getAuthData(command.getAuthToken());
                username = authData.username();
            } catch (DataAccessException e) {
                throw new RuntimeException("Unauthorized");
            }
            switch (command.getCommandType()) {
                case CONNECT -> connect(ctx.session, username, command.getGameID());
            }
        } catch (IOException ex) {
            ctx.session.getRemote().sendString(new Gson().toJson(new ErrorMessage(ex.getMessage())));
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    public void connect(Session session, String username, int gameID) throws IOException {
        connections.add(username, session, gameID);
        var message = String.format("%s joined the game", username);
        var notification = new NotificationMessage(message);
        connections.broadcast(session, notification);
    }

}