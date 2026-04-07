package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

public record Connection(Session session, int gameID) {
}
