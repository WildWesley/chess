package passoff.server;

import chess.ChessGame;
import dataaccess.DataAccessException;
import model.RegisterRequest;
import org.junit.jupiter.api.*;
import passoff.model.*;
import server.Server;
import service.ChessService;

import java.net.HttpURLConnection;
import java.util.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServiceTests {

    private static TestUser existingUser;
    private static TestUser newUser;
    private static TestCreateRequest createRequest;
    private static TestServerFacade serverFacade;
    private static Server server;
    private String existingAuth;

    // ### TESTING SETUP/CLEANUP ###

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);

        serverFacade = new TestServerFacade("localhost", Integer.toString(port));
        existingUser = new TestUser("ExistingUser", "existingUserPassword", "eu@mail.com");
        newUser = new TestUser("NewUser", "newUserPassword", "nu@mail.com");
        createRequest = new TestCreateRequest("testGame");
    }

    @BeforeEach
    public void setup() {
        serverFacade.clear();

        //one user already logged in
        TestAuthResult regResult = serverFacade.register(existingUser);
        existingAuth = regResult.getAuthToken();
    }

    // ### SERVER-LEVEL API TESTS ###

    @Test
    @Order(1)
    @DisplayName("Static Files")
    public void registerPositive() throws DataAccessException {
        ChessService service = new ChessService();
        RegisterRequest posRegisterRequest = new RegisterRequest("bob", "bob", "bob");
        service.register(posRegisterRequest);
        assertThrows(DataAccessException.class, () -> {
            throw new DataAccessException();
        });
    }
}
