package service;

import dataaccess.*;
import model.*;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

public class ChessService {

    private final AuthDataAccess authDataAccess;
    private final UserDataAccess userDataAccess;
    private final GameDataAccess gameDataAccess;

    public ChessService(AuthDataAccess authDataAccess) {
        this.authDataAccess = new MemoryAuthDataAccess();
        this.userDataAccess = new MemoryUserDataAccess();
        this.gameDataAccess = new MemoryGameDataAccess();
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    // Pet Shop is very simple.
    // A more complicated application would do the business logic in the service.

    public AuthData register(RegisterRequest registerRequest) throws DataAccessException {
        UserData response = userDataAccess.getUser(registerRequest.username());
        if (response != null) {
            throw new DataAccessException("Error: Username already taken");
        }
        UserData newUserData = new UserData(registerRequest.username(),
                registerRequest.password(), registerRequest.email());
        userDataAccess.createUser(newUserData);
        AuthData newAuthData = new AuthData(generateToken(), registerRequest.username());
        authDataAccess.createAuth(newAuthData);

        // newAuthData is the register result
        return newAuthData;
    }

    public void clearApplication() throws DataAccessException {
        authDataAccess.clear();
        userDataAccess.clear();
        gameDataAccess.clear();
    }

    public AuthData login(LoginRequest loginRequest) throws DataAccessException {
        UserData response = userDataAccess.getUser(loginRequest.username());
        if (response == null) {
            throw new DataAccessException("Error: Bad Request");
        }
        if (!Objects.equals(response.password(), loginRequest.password())) {
            throw new DataAccessException("Error: Unauthorized");
        }

        AuthData newAuthData = new AuthData(generateToken(), loginRequest.username());
        authDataAccess.createAuth(newAuthData);

        // newAuthData is the register result
        return newAuthData;
    }

    public void logout(LogoutRequest logoutRequest) throws DataAccessException {
        AuthData response = authDataAccess.getAuth(logoutRequest.authToken());
        if (response == null) {
            throw new DataAccessException("Error: Unauthorized");
        }
        authDataAccess.deleteAuth(response.authToken());
    }

    public ArrayList<GameData> listGames(ListGamesRequest listGamesRequest) throws DataAccessException {
        AuthData response = authDataAccess.getAuth(listGamesRequest.authToken());
        if (response == null) {
            throw new DataAccessException("Error: Unauthorized");
        }

        return (ArrayList<GameData>) gameDataAccess.getAllGames();
    }

    public CreateGameResponse createGame(CreateGameRequest createGameRequest) throws DataAccessException {
        AuthData response = authDataAccess.getAuth(createGameRequest.authToken());
        if (response == null) {
            throw new DataAccessException("Error: Unauthorized");
        }

        return gameDataAccess.createGame(createGameRequest.gameName());
    }

    public void joinGame(JoinGameRequest joinGameRequest) throws DataAccessException {
        AuthData authData = authDataAccess.getAuth(joinGameRequest.authToken());

        if (authData == null) {
            throw new DataAccessException("Error: Unauthorized");
        }

        GameData gameData = gameDataAccess.getGame(joinGameRequest.gameID());

        if (gameData == null) {
            throw new DataAccessException("Error: Bad Request");
        } else if (joinGameRequest.playerColor().equals("WHITE")) {
            if (!gameData.whiteUsername().isEmpty()) {
                throw new DataAccessException("Error: Already taken");
            }
        } else {
            if (!gameData.whiteUsername().isEmpty()) {
                throw new DataAccessException("Error: Already taken");
            }
        }

        if (joinGameRequest.playerColor().equals("WHITE")) {
            gameDataAccess.updateGameDataWhite(joinGameRequest.gameID(), authData.username());
        } else {
            gameDataAccess.updateGameDataBlack(joinGameRequest.gameID(), authData.username());
        }
    }
}
