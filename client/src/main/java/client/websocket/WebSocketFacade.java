package client.websocket;

import facade.ServerFacadeException;
import com.google.gson.Gson;
import jakarta.websocket.*;
import websocket.messages.Action;
import websocket.messages.Notification;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ServerFacadeException {
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
                    Notification notification = new Gson().fromJson(message, Notification.class);
                    notificationHandler.notify(notification);
                }
            });
        } catch (Exception ex) {
            throw new ServerFacadeException("Error: websocket initialization failed.");
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void newPlayer(String playerName) throws ServerFacadeException {
        try {
            var action = new Action(Action.Type.ENTER, playerName);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (Exception ex) {
            throw new ServerFacadeException("Error: unable to notify of new player.");
        }
    }

    public void newObserver(String observerName) throws ServerFacadeException {
        try {
            var action = new Action(Action.Type.ENTER, observerName);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (Exception ex) {
            throw new ServerFacadeException("Error: unable to notify of new observer.");
        }
    }

    public void moveMade(String playerName) throws ServerFacadeException {
        try {
            var action = new Action(Action.Type.ENTER, String.format("%s made a move", playerName));
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (Exception ex) {
            throw new ServerFacadeException("Error: unable to notify of move made.");
        }
    }

    public void leftGame(String playerName) throws ServerFacadeException {
        try {
            var action = new Action(Action.Type.ENTER, playerName);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (Exception ex) {
            throw new ServerFacadeException("Error: unable to notify of player leaving.");
        }
    }

    public void playerResigned(String playerName) throws ServerFacadeException {
        try {
            var action = new Action(Action.Type.ENTER, playerName);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (Exception ex) {
            throw new ServerFacadeException("Error: unable to notify of player resigning.");
        }
    }

    public void playerInCheck(String playerName) throws ServerFacadeException {
        try {
            var action = new Action(Action.Type.ENTER, playerName);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (Exception ex) {
            throw new ServerFacadeException("Error: unable to notify of player in check.");
        }
    }

    public void playerInCheckmate(String playerName) throws ServerFacadeException {
        try {
            var action = new Action(Action.Type.ENTER, playerName);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (Exception ex) {
            throw new ServerFacadeException("Error: unable to notify of player in checkmate.");
        }
    }

}

