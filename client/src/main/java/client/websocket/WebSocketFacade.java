package client.websocket;

import facade.ServerFacadeException;
import com.google.gson.Gson;
import jakarta.websocket.*;
import websocket.commands.UserGameCommand;
import websocket.messages.*;
import chess.*;

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
                    ServerMessage msg = new Gson().fromJson(message, ServerMessage.class);
                    if (msg.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
                        Notification notification = new Gson().fromJson(message, Notification.class);
                        notificationHandler.notify(notification);
                    } else if (msg.getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
                        ErrorMessage errorMsg = new Gson().fromJson(message, ErrorMessage.class);
                        // Create a notifyError method in client AND NotificationHandler to handle error messages
                        notificationHandler.notifyError(errorMsg);
                    } else if (msg.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
                        // Create a loadGame method in client AND NotificationHandler to call draw_board method
                        LoadGameMessage loadGameMessage = new Gson().fromJson(message, LoadGameMessage.class);
                        notificationHandler.loadGame(loadGameMessage.getGame());
                    }
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

    public void newPlayer(String authToken, int gameID) throws ServerFacadeException {
        try {
            UserGameCommand userGameCommand = new UserGameCommand(UserGameCommand.CommandType.CONNECT,
                    authToken, gameID, null);
            this.session.getBasicRemote().sendText(new Gson().toJson(userGameCommand));
        } catch (Exception ex) {
            throw new ServerFacadeException("Error: unable to notify of new player.");
        }
    }

    public void moveMade(ChessMove move, String authToken, int gameID) throws ServerFacadeException {
        try {
            var action = new UserGameCommand(UserGameCommand.CommandType.MAKE_MOVE,
                    authToken, gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (Exception ex) {
            throw new ServerFacadeException(ex.getMessage());
        }
    }

    public void leftGame(String authToken, int gameID) throws ServerFacadeException {
        try {
            var action = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID, null);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (Exception ex) {
            throw new ServerFacadeException("Error: unable to notify of player leaving.");
        }
    }

    public void playerResigned(String authToken, int gameID) throws ServerFacadeException {
        try {
            var action = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID, null);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (Exception ex) {
            throw new ServerFacadeException("Error: unable to notify of player resigning.");
        }
    }
}

