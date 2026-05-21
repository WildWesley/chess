package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ErrorMessage;
import websocket.messages.Notification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, ArrayList<Session>> connections = new ConcurrentHashMap<>();

    public void add(Integer gameID, Session session) {
        if (connections.get(gameID) == null) {
            connections.put(gameID, new ArrayList<>(List.of(session)));
        } else {
            ArrayList<Session> tempArray = connections.get(gameID);
            tempArray.add(session);
            connections.put(gameID, tempArray);
        }
    }

    public void remove(Session session) {
        connections.remove(session);
    }

    // TODO: singleSend(authToken) method that sends to a session
    public void singleSend(Session session, @org.jetbrains.annotations.UnknownNullability ErrorMessage errorMessage) throws IOException {
        String msg = errorMessage.toString();
        if (session.isOpen()) {
            session.getRemote().sendString(msg);
        }
    }

    public void broadcast(Integer gameID, Session excludeSession, Notification notification) throws IOException {
        String msg = notification.toString();
        for (Session c : connections.get(gameID)) {
            if (c.isOpen()) {
                if (!c.equals(excludeSession)) {
                    c.getRemote().sendString(msg);
                }
            }
        }
    }
}
