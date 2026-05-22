package websocket.messages;

import chess.*;
import com.google.gson.Gson;

public class LoadGameMessage extends ServerMessage {
    ChessGame game;

    public LoadGameMessage(ChessGame game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public ChessGame getGame() { return game; }
    public String toString() {
        return new Gson().toJson(this);
    }
}
