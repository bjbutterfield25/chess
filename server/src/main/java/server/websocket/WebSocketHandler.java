package server.websocket;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.SQLGameDAO;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsCloseHandler;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsConnectHandler;
import io.javalin.websocket.WsMessageContext;
import io.javalin.websocket.WsMessageHandler;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import model.AuthData;
import service.UserService;
import websocket.commands.ConnectCommand;
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
            String username;
            try {
                UserService userService = new UserService();
                AuthData authData = userService.getAuthData(command.getAuthToken());
                if (authData == null){
                    ctx.session.getRemote().sendString(new Gson().toJson(new ErrorMessage("Error: unauthorized")));
                    return;
                }
                username = authData.username();
            } catch (DataAccessException e) {
                throw new RuntimeException("Unauthorized");
            }
            switch (command.getCommandType()) {
                case CONNECT -> connect(ctx.session, username, new Gson().fromJson(ctx.message(), ConnectCommand.class));
            }
        } catch (IOException | DataAccessException ex) {
            ctx.session.getRemote().sendString(new Gson().toJson(new ErrorMessage(ex.getMessage())));
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    public void connect(Session session, String username, ConnectCommand connectCommand) throws IOException, DataAccessException {
        GameDAO gameDAO = new SQLGameDAO();
        GameData game = gameDAO.getGame(connectCommand.getGameID());
        if (game == null) {
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage("Error: invalid game")));
            return;
        }
        connections.add(username, session, connectCommand.getGameID());
        var message = String.format("%s joined the game as %s", username, connectCommand.teamColor);
        var notification = new NotificationMessage(message);
        connections.broadcast(session, notification);
        session.getRemote().sendString(new Gson().toJson(new LoadGameMessage(game)));
    }

}