package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDataAccess;
import io.javalin.*;
import io.javalin.http.Context;
import model.AuthData;
import model.RegisterRequest;
import model.UserData;
import service.ChessService;

public class Server {
    private final ChessService service;
    private final Javalin javalin;

    public Server() {
        this.service = new ChessService(new MemoryAuthDataAccess());
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .post("/user", this::register)
                .delete("/db", this::clearApplication);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    private void register(Context ctx) throws DataAccessException {
        RegisterRequest registerRequest = new Gson().fromJson(ctx.body(), RegisterRequest.class);
        AuthData registerResponse = service.register(registerRequest);
        ctx.result(new Gson().toJson(registerResponse));
    }

    private void clearApplication(Context ctx) throws DataAccessException {
        service.clearApplication();
    }

    @Override
    public void login() throws DataAccessException {
        service.login(loginRequest);
    }
}
