package dataaccess;

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
import service.ChessService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DataAccessTests {
    ChessService testService;
    AuthDataAccess testAuthDataAccess;
    UserDataAccess testUserDataAccess;
    GameDataAccess testGameDataAccess;
    UserData testUserData = new UserData("bob", "bob", "bob");
    AuthData testAuthData = new AuthData("authToken", "bob");
    GameData testGameData = new GameData(1,
            null,
            null,
            "bobs game",
            new ChessGame());
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
    void createUserPos() throws DataAccessException {
        testUserDataAccess.createUser(testUserData);
    }

    @Test
    void getUserPos() throws DataAccessException {
        testUserDataAccess.createUser(testUserData);
        UserData accessUserData = testUserDataAccess.getUser(testUserData.username());
        Assertions.assertEquals(accessUserData, testUserData);
    }

    @Test
    void clearUserPos() throws DataAccessException {
        testUserDataAccess.createUser(testUserData);
        testUserDataAccess.clear();
        Assertions.assertNull(testUserDataAccess.getUser(testUserData.username()));
    }

    @Test
    void createUserNeg() throws DataAccessException {
        testUserDataAccess.createUser(testUserData);
        testUserDataAccess.createUser(testUserData);
    }

    @Test
    void getUserNeg() throws DataAccessException {
        testUserDataAccess.createUser(testUserData);
        UserData fakeUser = new UserData("joe", "joe", "joe");
        UserData accessUserData = testUserDataAccess.getUser(fakeUser.username());
        Assertions.assertNotEquals(accessUserData, testUserData);
    }

    @Test
    void createAuthPos() throws DataAccessException {
        testAuthDataAccess.createAuth(testAuthData);
    }

    @Test
    void getAuthPos() throws DataAccessException {
        testAuthDataAccess.createAuth(testAuthData);
        AuthData accessAuth = testAuthDataAccess.getAuth(testAuthData.authToken());
        Assertions.assertEquals(accessAuth, testAuthData);
    }

    @Test
    void deleteAuthPos() throws DataAccessException {
        testAuthDataAccess.createAuth(testAuthData);
        testAuthDataAccess.deleteAuth(testAuthData.authToken());
        AuthData accessAuthData = testAuthDataAccess.getAuth(testAuthData.authToken());
        Assertions.assertNull(accessAuthData);
    }

    @Test
    void clearAuthPos() throws DataAccessException {
        testAuthDataAccess.createAuth(testAuthData);
        testAuthDataAccess.clear();
        Assertions.assertNull(testUserDataAccess.getUser(testUserData.username()));
    }

    @Test
    void createAuthNeg() throws DataAccessException {
        Assertions.assertThrows(DataAccessException.class, () -> {
            testAuthDataAccess.createAuth(testAuthData);
            testAuthDataAccess.createAuth(testAuthData);
        });
    }

    @Test
    void getAuthNeg() throws DataAccessException {
        AuthData fakeAuthData = new AuthData("joe", "joe");
        testAuthDataAccess.createAuth(testAuthData);
        Assertions.assertNull(testAuthDataAccess.getAuth(fakeAuthData.authToken()));
    }

    @Test
    void deleteAuthNeg() throws DataAccessException {
        AuthData fakeAuthData = new AuthData("joe", "joe");
        testAuthDataAccess.createAuth(testAuthData);
        testAuthDataAccess.deleteAuth(fakeAuthData.authToken());
        AuthData accessAuthData = testAuthDataAccess.getAuth(testAuthData.authToken());
        Assertions.assertNotNull(accessAuthData);
    }

    // Game Unit Tests
    @Test
    void createGamePos() throws DataAccessException {
        testGameDataAccess.createGame("bobs game");
    }

    @Test
    void getGamePos() throws DataAccessException {
        testGameDataAccess.createGame("bobs game");
        GameData accessGameData = testGameDataAccess.getGame(1);
        Assertions.assertEquals(accessGameData, testGameData);
    }
    
    @Test
    void getAllGamesPos() throws DataAccessException {
        testGameDataAccess.createGame("bob");
        GameData testGame = new GameData(1,
                null,
                null,
                "bob",
                testGameDataAccess.getGame(1).game());
        Assertions.assertEquals(new ArrayList<GameData>(testGameDataAccess.getAllGames()),
                new ArrayList<GameData> (List.of(testGame)));
    }

    @Test
    void getAllGamesNeg() throws DataAccessException {
        testGameDataAccess.createGame("joe");
        GameData testGame = new GameData(1,
                null,
                null,
                "bob",
                testGameDataAccess.getGame(1).game());
        Assertions.assertNotEquals(new ArrayList<GameData>(testGameDataAccess.getAllGames()),
                new ArrayList<GameData> (List.of(testGame)));
    }

    @Test
    void clearGamePos() throws DataAccessException {
        testGameDataAccess.createGame("bob");
        testGameDataAccess.clear();
        Assertions.assertNull(testGameDataAccess.getGame(1));
    }

    @Test
    void listGamesPos() throws DataAccessException {
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

    // Come back and potentially add tests once makeMove() is implemented
}
