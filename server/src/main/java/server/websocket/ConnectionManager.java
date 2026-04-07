package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String username, Session session, int gameID) {
        Connection connection = new Connection(session, gameID);
        connections.put(username, connection);
    }

    public void remove(String username) {
        connections.remove(username);
    }

    public void broadcast(Session excludeSession, ServerMessage notification) throws IOException {
        String msg = new Gson().toJson(notification);
        for (var c : connections.values()) {
            if (c.session().isOpen()) {
                if (!c.session().equals(excludeSession)) {
                    c.session().getRemote().sendString(msg);
                }
            }
        }
    }
}