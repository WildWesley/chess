package model;

public record CreateGameRequest(String gameName, String authToken) {
    public CreateGameRequest addAuthorization(String authToken) {
        return new CreateGameRequest(this.gameName(), authToken);
    }
}
