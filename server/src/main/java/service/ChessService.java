package service;

import dataaccess.*;
import model.*;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

// The ChessService class is what takes in the request data through handler-created java objects, and calls the correct
// DataAccess functions to interact correctly with the database/memory that the request needs to interact with. All
// input logic is handled here.
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
        if (registerRequest.username() == null ||
            registerRequest.password() == null ||
            registerRequest.email() == null) {
            throw new DataAccessException(("Error: bad request"));
        }

        UserData response = userDataAccess.getUser(registerRequest.username());
        if (response != null) {
            throw new DataAccessException("Error: already taken");
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
        if (loginRequest.username() == null ||
                loginRequest.password() == null) {
            throw new DataAccessException(("Error: bad request"));
        }

        UserData userData = userDataAccess.getUser(loginRequest.username());

        if (userData == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        if (!Objects.equals(userData.password(), loginRequest.password())) {
            throw new DataAccessException("Error: unauthorized");
        }

        AuthData newAuthData = new AuthData(generateToken(), loginRequest.username());
        authDataAccess.createAuth(newAuthData);

        // newAuthData is the register result
        return newAuthData;
    }

    public void logout(LogoutRequest logoutRequest) throws DataAccessException {
        if (logoutRequest.authToken() == null) {
            throw new DataAccessException(("Error: bad request"));
        }

        AuthData response = authDataAccess.getAuth(logoutRequest.authToken());
        if (response == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        authDataAccess.deleteAuth(response.authToken());
    }

    public ListGamesResponse listGames(ListGamesRequest listGamesRequest) throws DataAccessException {
        if (listGamesRequest.authToken() == null) {
            throw new DataAccessException(("Error: bad request"));
        }

        AuthData response = authDataAccess.getAuth(listGamesRequest.authToken());
        if (response == null) {
            throw new DataAccessException("Error: unauthorized");
        }

        return new ListGamesResponse(new ArrayList<GameData>(gameDataAccess.getAllGames()));
    }

    public CreateGameResponse createGame(CreateGameRequest createGameRequest) throws DataAccessException {
        if (createGameRequest.gameName() == null ||
            createGameRequest.authToken() == null) {
            throw new DataAccessException(("Error: bad request"));
        }

        AuthData response = authDataAccess.getAuth(createGameRequest.authToken());
        if (response == null) {
            throw new DataAccessException("Error: unauthorized");
        }

        return gameDataAccess.createGame(createGameRequest.gameName());
    }

    public void joinGame(JoinGameRequest joinGameRequest) throws DataAccessException {
        if (joinGameRequest.playerColor() == null ||
            joinGameRequest.gameID() == null ||
            joinGameRequest.authToken() == null) {
            throw new DataAccessException("Error: bad request");
        }

        AuthData authData = authDataAccess.getAuth(joinGameRequest.authToken());

        if (authData == null) {
            throw new DataAccessException("Error: unauthorized");
        }

        GameData gameData = gameDataAccess.getGame(joinGameRequest.gameID());

        // Usernames are set to null ptr on default
        if (gameData == null) {
            throw new DataAccessException("Error: bad request");
        } else if (joinGameRequest.playerColor().equals("WHITE")) {
            if (gameData.whiteUsername() != null) {
                throw new DataAccessException("Error: already taken");
            }
        } else if (joinGameRequest.playerColor().equals("BLACK")) {
            if (gameData.blackUsername() != null) {
                throw new DataAccessException("Error: already taken");
            }
        } else {
            // In case they input a color that isn't WHITE or BLACK
            throw new DataAccessException("Error: bad request");
        }

        if (joinGameRequest.playerColor().equals("WHITE")) {
            gameDataAccess.updateGameDataWhite(joinGameRequest.gameID(), authData.username());
        } else {
            gameDataAccess.updateGameDataBlack(joinGameRequest.gameID(), authData.username());
        }
    }
}
