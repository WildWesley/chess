package websocket.messages;

import com.google.gson.Gson;

public class ErrorMessage extends ServerMessage {
    String message;

    public ErrorMessage(String message) {
        super(ServerMessageType.ERROR);
        this.message = message;
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
