package server.websocket;

import chess.ChessGame;
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
                case LEAVE -> leave(ctx.session, username, new Gson().fromJson(ctx.message(), UserGameCommand.class));
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
        GameData game = getGame(connectCommand.getGameID());
        if (game == null) {
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage("Error: invalid game")));
            return;
        }
        connections.add(username, session, connectCommand.getGameID(), connectCommand.teamColor);
        String message;
        if (connectCommand.teamColor != null){
            message = String.format("%s joined the game as %s", username, connectCommand.teamColor);
        } else {
            message = String.format("%s is observing the game", username);
        }
        var notification = new NotificationMessage(message);
        connections.broadcast(session, notification);
        session.getRemote().sendString(new Gson().toJson(new LoadGameMessage(game)));
    }

    public void leave(Session session, String username, UserGameCommand command) throws IOException, DataAccessException {
        GameData game = getGame(command.getGameID());
        if (game == null) {
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage("Error: invalid game")));
            return;
        }
        var connection = connections.getConnection(username);
        ChessGame.TeamColor color = connection.teamColor();
        String whiteUsername, blackUsername;
        if (color != null) {
            if (color.equals(ChessGame.TeamColor.WHITE)) {
                whiteUsername = null;
                blackUsername = game.blackUsername();
            } else if (color.equals(ChessGame.TeamColor.BLACK)) {
                blackUsername = null;
                whiteUsername = game.whiteUsername();
            } else {
                blackUsername = game.blackUsername();
                whiteUsername = game.whiteUsername();
            }
            updateGame(command.getGameID(), whiteUsername, blackUsername);
        }
        var message = String.format("%s left the game", username);
        var notification = new NotificationMessage(message);
        connections.broadcast(session, notification);
        connections.remove(username);
    }

    public GameData getGame(int gameID) throws DataAccessException {
        GameDAO gameDAO = new SQLGameDAO();
        return gameDAO.getGame(gameID);
    }

    public void updateGame(int gameID, String whiteUsername, String blackUsername) throws DataAccessException {
        GameDAO gameDAO = new SQLGameDAO();
        gameDAO.joinGame(gameID, whiteUsername, blackUsername);
    }
}