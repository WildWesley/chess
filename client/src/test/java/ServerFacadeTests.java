import Facade.ServerFacade;
import Facade.ServerFacadeException;
import model.*;
import org.junit.jupiter.api.*;
import server.Server;

import java.util.ArrayList;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;
    private RegisterRequest testRegisterRequest = new RegisterRequest("bob", "bob", "bob");
    private LoginRequest testLoginRequest = new LoginRequest("bob", "bob");

    @BeforeAll
    public static void init() throws ServerFacadeException {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("http://localhost:" + port);
        serverFacade.clearApplication();
    }

    @AfterEach
    void clearServer() throws ServerFacadeException {
        serverFacade.clearApplication();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void register() throws ServerFacadeException {
        AuthData authData = serverFacade.register(testRegisterRequest);
        Assertions.assertNotNull(authData.authToken());
        Assertions.assertEquals(testRegisterRequest.username(), authData.username());
    }

    @Test
    public void registerNeg() throws ServerFacadeException {
        Assertions.assertThrows(ServerFacadeException.class, () -> {
            serverFacade.register(testRegisterRequest);
            serverFacade.register(testRegisterRequest);
        });
    }

    @Test
    public void login() throws ServerFacadeException {
        serverFacade.register(testRegisterRequest);
        AuthData loginResponse = serverFacade.login(testLoginRequest);
        Assertions.assertEquals(testLoginRequest.username(), loginResponse.username());
        Assertions.assertNotNull(loginResponse.authToken());
    }

    @Test
    public void loginNeg() throws ServerFacadeException {
        Assertions.assertThrows(ServerFacadeException.class, () -> {
            serverFacade.register(testRegisterRequest);
            serverFacade.login(new LoginRequest("bob", "joe"));
        });
    }

    @Test
    public void logout() throws ServerFacadeException {
        serverFacade.register(testRegisterRequest);
        AuthData loginResponse = serverFacade.login(testLoginRequest);
        serverFacade.logout(new LogoutRequest(loginResponse.authToken()));
        Assertions.assertThrows(ServerFacadeException.class, () -> {
            serverFacade.listGames(new ListGamesRequest(loginResponse.authToken()));
        });
    }

    @Test
    public void logoutNeg() throws ServerFacadeException {
        serverFacade.register(testRegisterRequest);
        AuthData loginResponse = serverFacade.login(testLoginRequest);
        serverFacade.logout(new LogoutRequest(loginResponse.authToken()));
        Assertions.assertThrows(ServerFacadeException.class, () -> {
            serverFacade.logout(new LogoutRequest(loginResponse.authToken()));
        });
    }

    @Test
    public void listGames() throws ServerFacadeException {
        serverFacade.register(testRegisterRequest);
        AuthData loginResponse = serverFacade.login(testLoginRequest);
        ListGamesResponse listGamesResponse = serverFacade.listGames(new ListGamesRequest(loginResponse.authToken()));
        Assertions.assertEquals(new ArrayList<GameData>(), listGamesResponse.games());
    }

    @Test
    public void listGamesNeg() throws ServerFacadeException {
        serverFacade.register(testRegisterRequest);
        serverFacade.login(testLoginRequest);
        Assertions.assertThrows(ServerFacadeException.class, () -> {
            ListGamesResponse listGamesResponse = serverFacade.listGames(new ListGamesRequest("fakeAuth"));
            Assertions.assertEquals(new ArrayList<GameData>(), listGamesResponse.games());
        });
    }

    @Test
    public void createGame() throws ServerFacadeException {
        serverFacade.register(testRegisterRequest);
        AuthData loginResponse = serverFacade.login(testLoginRequest);
        CreateGameResponse createGameResponse = serverFacade.createGame(new CreateGameRequest("bob",
                loginResponse.authToken()));
        Assertions.assertEquals(1, createGameResponse.gameID());
    }

    @Test
    public void createGameNeg() throws ServerFacadeException {
        serverFacade.register(testRegisterRequest);
        serverFacade.login(testLoginRequest);
        Assertions.assertThrows(ServerFacadeException.class, () -> {
            serverFacade.createGame(new CreateGameRequest("bob", "fakeAuth"));
        });
    }

    @Test
    public void joinGame() throws ServerFacadeException {
        serverFacade.register(testRegisterRequest);
        AuthData loginResponse = serverFacade.login(testLoginRequest);
        serverFacade.createGame(new CreateGameRequest("bob", loginResponse.authToken()));
        serverFacade.joinGame(new JoinGameRequest("WHITE", 1, loginResponse.authToken()));
        ListGamesResponse listGamesResponse = serverFacade.listGames(new ListGamesRequest(loginResponse.authToken()));
        GameData game = listGamesResponse.games().get(0);
        Assertions.assertEquals(loginResponse.username(), game.whiteUsername());
    }

    @Test
    public void joinGameNeg() throws ServerFacadeException {
        serverFacade.register(testRegisterRequest);
        AuthData loginResponse = serverFacade.login(testLoginRequest);
        Assertions.assertThrows(ServerFacadeException.class, () -> {
            serverFacade.joinGame(new JoinGameRequest("GREEN", 1, loginResponse.authToken()));
        });
    }

    @Test
    public void clearApplication() throws ServerFacadeException {
        serverFacade.register(testRegisterRequest);
        serverFacade.clearApplication();
        serverFacade.register(testRegisterRequest);
    }
}
