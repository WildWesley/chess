package service;

import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.UserDataAccess;
import model.AuthData;
import model.RegisterRequest;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChessServiceTest {
    ChessService testService;
    AuthDataAccess testAuthDataAccess;
    UserDataAccess testUserDataAccess;

    @BeforeEach
    void createService() {
        testService = new ChessService();

        testAuthDataAccess = testService.getAuthDataAccess();

        testUserDataAccess = testService.getUserDataAccess();
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
    void clearApplicationPositive() {
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