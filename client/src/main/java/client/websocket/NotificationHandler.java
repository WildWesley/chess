package client.websocket;

import chess.ChessGame;
import websocket.messages.ErrorMessage;
import websocket.messages.Notification;

public interface NotificationHandler {
    void loadGame(ChessGame game);

    void notifyError(ErrorMessage errorMessage);

    void notify(Notification notification);
}
