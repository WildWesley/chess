package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDataAccess;
import io.javalin.*;
import io.javalin.http.Context;
import model.*;
import server.websocket.WebSocketHandler;
import service.ChessService;

import javax.xml.crypto.Data;
import java.util.ArrayList;

// The server takes in the URL from the client and determines what request is being made
// It then passes the information given to the correct handler
public class Server {
    private final ChessService service;
    private final Javalin javalin;
    private final WebSocketHandler webSocketHandler;

    // This constructor sets up the javalin URLs which interact with each of the handlers. By using different requests
    // such as post, delete, get, and put, different endpoints can share the same URL, simplifying URL structure.
    public Server() {
        this.service = new ChessService();

        webSocketHandler = new WebSocketHandler(this.service);

        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .post("/user", this::register)
                .delete("/db", this::clearApplication)
                .post("/session", this::login)
                .delete("/session", this::logout)
                .get("/game", this::listGames)
                .post("/game", this::createGame)
                .put("/game", this::joinGame)
                .get("/echo/{msg}", ctx -> ctx.result("HTTP response: " + ctx.pathParam("msg")))
                .ws("/ws", ws -> {
                    ws.onConnect(webSocketHandler);
                    ws.onMessage(webSocketHandler);
                    ws.onClose(webSocketHandler);
                });
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    // This error handling method takes in the Javalin context and the exception, publishes it to the Javalin result,
    // and posts the correct error status code number
    private void errorHandling(Context ctx, DataAccessException e) {
        ctx.result(new Gson().toJson(new ErrorMessage(e.getMessage())));
        switch (e.getMessage()) {
            case "Error: bad request" -> ctx.status(400);
            case "Error: unauthorized" -> ctx.status(401);
            case "Error: already taken" -> ctx.status(403);
            default -> ctx.status(500);
        }
    }

    private void handleWebsocketMessages(Context ctx) throws DataAccessException {

    }

    // These are the various handlers. Depending on what request is being made, one of these will be called, which will
    // translate the serialized information given into a java object and pass that to the correct service functions.
    // Finally, the handlers will create the Javalin outputs.
    private void register(Context ctx) throws DataAccessException {
        try {
            RegisterRequest registerRequest = new Gson().fromJson(ctx.body(), RegisterRequest.class);
            AuthData registerResponse = service.register(registerRequest);
            ctx.result(new Gson().toJson(registerResponse));
        } catch (DataAccessException e) {
            errorHandling(ctx, e);
        }
    }

    private void clearApplication(Context ctx) throws DataAccessException {
        try {
            service.clearApplication();
        } catch (DataAccessException e) {
            errorHandling(ctx, e);
        }
    }

    public void login(Context ctx) throws DataAccessException {
        try {
            LoginRequest loginRequest = new Gson().fromJson(ctx.body(), LoginRequest.class);
            AuthData loginResponse = service.login(loginRequest);
            ctx.result(new Gson().toJson(loginResponse));
        } catch (DataAccessException e) {
            errorHandling(ctx, e);
        }
    }
    
    public void logout(Context ctx) throws DataAccessException {
        try {
            String authToken = ctx.header("authorization");
            LogoutRequest logoutRequest = new LogoutRequest(authToken);
            service.logout(logoutRequest);
        } catch (DataAccessException e) {
            errorHandling(ctx, e);
        }
    }

    public void listGames(Context ctx) throws DataAccessException {
        try {
            String authToken = ctx.header("authorization");
            ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);
            ListGamesResponse listGamesResponse = service.listGames(listGamesRequest);
            ctx.result(new Gson().toJson(listGamesResponse));
        } catch (DataAccessException e) {
            errorHandling(ctx, e);
        }
    }

    public void createGame(Context ctx) throws DataAccessException {
        try {
            CreateGameRequest createGameRequest = new Gson().fromJson(ctx.body(), CreateGameRequest.class);
            createGameRequest = createGameRequest.addAuthorization(ctx.header("authorization"));
            CreateGameResponse createGameResponse = service.createGame(createGameRequest);
            ctx.result(new Gson().toJson(createGameResponse));
        } catch (DataAccessException e) {
            errorHandling(ctx, e);
        }
    }

    public void joinGame(Context ctx) throws DataAccessException {
        try {
            String authToken = ctx.header("authorization");
            JoinGameRequest joinGameRequest = new Gson().fromJson(ctx.body(), JoinGameRequest.class);
            joinGameRequest = joinGameRequest.addAuthorization(authToken);
            service.joinGame(joinGameRequest);
        } catch (DataAccessException e) {
            errorHandling(ctx, e);
        }
    }
}
