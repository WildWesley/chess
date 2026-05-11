package service;

import chess.ChessGame;
import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import model.AuthData;
import model.GameData;
import model.RegisterRequest;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChessServiceTest {
    ChessService testService;
    AuthDataAccess testAuthDataAccess;
    UserDataAccess testUserDataAccess;
    GameDataAccess testGameDataAccess;

    @BeforeEach
    void createService() {
        testService = new ChessService();

        testAuthDataAccess = testService.getAuthDataAccess();

        testUserDataAccess = testService.getUserDataAccess();

        testGameDataAccess = testService.getGameDataAccess();
    }

    @Test
    void registerPositive() throws DataAccessException {
        RegisterRequest testRequest = new RegisterRequest("bob", "bob", "bob");
        AuthData testAuthData = testService.register(testRequest);
        Assertions.assertNotNull(testAuthData);
        Assertions.assertEquals("bob", testAuthData.username());
        Assertions.assertNotNull(testAuthData.authToken());

        AuthData accessAuthData = testAuthDataAccess.getAuth(testAuthData.authToken());
        Assertions.assertEquals(testAuthData, accessAuthData);

        UserData accessUserData = testUserDataAccess.getUser(testAuthData.username());
        Assertions.assertEquals(testRequest.username(), accessUserData.username());
        Assertions.assertEquals(testRequest.password(), accessUserData.password());
        Assertions.assertEquals(testRequest.email(), accessUserData.email());
    }

    @Test
    void clearApplicationPositive() throws DataAccessException {
        AuthData testAuthData = new AuthData("authToken", "bob");
        testAuthDataAccess.createAuth(testAuthData);

        UserData testUserData = new UserData("bob", "bob", "bob");
        testUserDataAccess.createUser(testUserData);

        testGameDataAccess.createGame("bob");

        testService.clearApplication();

        Assertions.assertNull(testUserDataAccess.getUser("bob"));
        Assertions.assertNull(testUserDataAccess.getUser("bob"));
        Assertions.assertNull(testUserDataAccess.getUser("bob"));
    }

    @Test
    void loginPositive() {
    }

    @Test
    void logoutPositive() {
    }

    @Test
    void listGamesPositive() {
    }

    @Test
    void createGamePositive() {
    }

    @Test
    void joinGamePositive() {

    }

    @Test
    void registerFail() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            RegisterRequest testRequest = new RegisterRequest("bob", null, "bob");
            AuthData testAuthData = testService.register(testRequest);
        });
    }

    @Test
    void loginFail() {
    }

    @Test
    void logoutFail() {
    }

    @Test
    void listGamesFail() {
    }

    @Test
    void createGameFail() {
    }

    @Test
    void joinGameFail() {
    }
}