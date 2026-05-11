package service;

import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.UserDataAccess;
import model.AuthData;
import model.RegisterRequest;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChessServiceTest {

    @Test
    void registerPositive() throws DataAccessException {
        ChessService testService = new ChessService();
        RegisterRequest testRequest = new RegisterRequest("bob", "bob", "bob");
        AuthData testAuthData = testService.register(testRequest);
        Assertions.assertNotNull(testAuthData);
        Assertions.assertEquals("bob", testAuthData.username());
        Assertions.assertNotNull(testAuthData.authToken());

        AuthDataAccess testAuthDataAccess = testService.getAuthDataAccess();
        AuthData accessAuthData = testAuthDataAccess.getAuth(testAuthData.authToken());
        Assertions.assertEquals(testAuthData, accessAuthData);

        UserDataAccess testUserDataAccess = testService.getUserDataAccess();
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