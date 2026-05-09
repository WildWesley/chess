package model;

public record JoinGameRequest(String playerColor, Integer gameID, String authToken) {
    public JoinGameRequest addAuthorization(String authToken) {
        return new JoinGameRequest(this.playerColor, this.gameID(), authToken);
    }
}
