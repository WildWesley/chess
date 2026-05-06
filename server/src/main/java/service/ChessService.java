package service;

import dataaccess.*;
import model.*;

import javax.xml.crypto.Data;
import java.util.Collection;
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
}
