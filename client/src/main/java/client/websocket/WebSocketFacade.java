package client.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import ui.ResponseException;
import jakarta.websocket.*;
import websocket.commands.ConnectCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.*;
import websocket.messages.ServerMessage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    switch (notification.getServerMessageType()) {
                        case NOTIFICATION:
                            notificationHandler.notify(new Gson().fromJson(message, NotificationMessage.class));
                            break;
                        case LOAD_GAME:
                            notificationHandler.notify(new Gson().fromJson(message, LoadGameMessage.class));
                            break;
                        case ERROR:
                            notificationHandler.notify(new Gson().fromJson(message, ErrorMessage.class));
                            break;
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void connect(String authToken, int gameID, ChessGame.TeamColor teamColor) throws ResponseException {
        try {
            var command = new ConnectCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID, teamColor);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

}