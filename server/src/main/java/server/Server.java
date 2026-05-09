package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDataAccess;
import io.javalin.*;
import io.javalin.http.Context;
import model.*;
import service.ChessService;

import java.util.ArrayList;

public class Server {
    private final ChessService service;
    private final Javalin javalin;

    public Server() {
        this.service = new ChessService(new MemoryAuthDataAccess());
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .post("/user", this::register)
                .delete("/db", this::clearApplication)
                .post("/session", this::login)
                .delete("/session", this::logout)
                .get("/game", this::listGames)
                .post("/game", this::createGame)
                .put("/game", this::joinGame);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    private void errorHandling(Context ctx, DataAccessException e) {
        ctx.result(new Gson().toJson(new ErrorMessage(e.getMessage())));
        switch (e.getMessage()) {
            case "Error: bad request" -> ctx.status(400);
            case "Error: unauthorized" -> ctx.status(401);
            case "Error: already taken" -> ctx.status(403);
        }
    }

    private void register(Context ctx) throws DataAccessException {
        RegisterRequest registerRequest = new Gson().fromJson(ctx.body(), RegisterRequest.class);
        AuthData registerResponse = service.register(registerRequest);
        ctx.result(new Gson().toJson(registerResponse));
    }

    private void clearApplication(Context ctx) throws DataAccessException {
        service.clearApplication();
    }

    public void login(Context ctx) throws DataAccessException {
        LoginRequest loginRequest = new Gson().fromJson(ctx.body(), LoginRequest.class);
        try {
            AuthData loginResponse = service.login(loginRequest);
            ctx.result(new Gson().toJson(loginResponse));
        } catch (DataAccessException e) {
            errorHandling(ctx, e);
        }
    }

    public void logout(Context ctx) throws DataAccessException {
        LogoutRequest logoutRequest = new Gson().fromJson(ctx.body(), LogoutRequest.class);
        service.logout(logoutRequest);
    }

    public void listGames(Context ctx) throws DataAccessException {
        ListGamesRequest listGamesRequest = new Gson().fromJson(ctx.body(), ListGamesRequest.class);
        ArrayList<GameData> listGamesResponse = service.listGames(listGamesRequest);
        ctx.result(new Gson().toJson(listGamesResponse));
    }

    public void createGame(Context ctx) throws DataAccessException {
        CreateGameRequest createGameRequest = new Gson().fromJson(ctx.body(), CreateGameRequest.class);
        CreateGameResponse createGameResponse = service.createGame(createGameRequest);
        ctx.result(new Gson().toJson(createGameResponse));
    }

    public void joinGame(Context ctx) throws DataAccessException {
        JoinGameRequest joinGameRequest = new Gson().fromJson(ctx.body(), JoinGameRequest.class);
        service.joinGame(joinGameRequest);
    }
}
