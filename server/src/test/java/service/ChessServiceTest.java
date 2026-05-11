package service;

import chess.ChessGame;
import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import model.*;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChessServiceTest {
    ChessService testService;
    AuthDataAccess testAuthDataAccess;
    UserDataAccess testUserDataAccess;
    GameDataAccess testGameDataAccess;
    RegisterRequest testRequest = new RegisterRequest("bob", "bob", "bob");
    LoginRequest testLoginRequest = new LoginRequest("bob", "bob");

    @BeforeEach
    void createService() {
        testService = new ChessService();

        testAuthDataAccess = testService.getAuthDataAccess();

        testUserDataAccess = testService.getUserDataAccess();

        testGameDataAccess = testService.getGameDataAccess();
    }

    @Test
    void registerPositive() throws DataAccessException {
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

        Assertions.assertNull(testAuthDataAccess.getAuth("authToken"));
        Assertions.assertNull(testUserDataAccess.getUser("bob"));
        Assertions.assertNull(testGameDataAccess.getGame(1));
    }

    @Test
    void loginPositive() throws DataAccessException {
        testService.register(testRequest);

        AuthData userAuthData = testService.login(testLoginRequest);

        Assertions.assertEquals("bob", userAuthData.username());
        Assertions.assertNotNull(userAuthData.authToken());

        UserData accessUserData = testUserDataAccess.getUser("bob");
        UserData testUserData = new UserData("bob", "bob", "bob");
        Assertions.assertEquals(accessUserData, testUserData);

        // Test auth
        AuthData accessAuthData = testAuthDataAccess.getAuth(userAuthData.authToken());
        Assertions.assertEquals("bob", accessAuthData.username());
    }

    @Test
    void logoutPositive() throws DataAccessException {
        testService.register(testRequest);
        AuthData testAuthData = testService.login(testLoginRequest);
        testService.logout(new LogoutRequest(testAuthData.authToken()));

        Assertions.assertNull(testAuthDataAccess.getAuth(testAuthData.authToken()));
    }

    @Test
    void listGamesPositive() throws DataAccessException {
        AuthData testAuthData = testService.register(testRequest);
        testService.createGame(new CreateGameRequest("bob", testAuthData.authToken()));
        ListGamesResponse listResponse = testService.listGames(new ListGamesRequest(testAuthData.authToken()));
        GameData testGame = new GameData(1,
                null,
                null,
                "bob",
                testGameDataAccess.getGame(1).game());
        // "List.of()" lets me set the ArrayList to that initial value
        Assertions.assertEquals(new ListGamesResponse(new ArrayList<GameData>(List.of(testGame))), listResponse);
    }

    @Test
    void createGamePositive() throws DataAccessException {
        AuthData testAuthData = testService.register(testRequest);
        CreateGameResponse testGameResponse =
                testService.createGame(new CreateGameRequest("bob", testAuthData.authToken()));
        Assertions.assertEquals(1, testGameResponse.gameID());

        GameData accessGameData = testGameDataAccess.getGame(1);
        Assertions.assertNotNull(accessGameData);
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