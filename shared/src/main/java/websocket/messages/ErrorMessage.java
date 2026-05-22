package websocket.messages;

import com.google.gson.Gson;

public class ErrorMessage extends ServerMessage {
    String errorMessage;

    public ErrorMessage(String message) {
        super(ServerMessageType.ERROR);
        this.errorMessage = message;
    }

    public String getMessage() { return errorMessage; }
    public String toString() {
        return new Gson().toJson(this);
    }
}
