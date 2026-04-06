package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Session> connections = new ConcurrentHashMap<>();

    public void add(String username, Session session) {
        connections.put(username, session);
    }

    public void remove(String username) {
        connections.remove(username);
    }

    public void broadcast(Session excludeSession, ServerMessage notification) throws IOException {
        String msg = notification.toString();
        for (Session c : connections.values()) {
            if (c.isOpen()) {
                if (!c.equals(excludeSession)) {
                    c.getRemote().sendString(msg);
                }
            }
        }
    }
}