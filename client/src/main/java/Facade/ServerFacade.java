package Facade;

import com.google.gson.Gson;
import model.*;

import java.net.*;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    private HttpRequest buildRequest(String method, String path, Object body, String authorization) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        // Try to add authorization to the header
        if (authorization != null) {
            request.setHeader("authorization", authorization);
        }

        return request.build();
    }

    private BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws ServerFacadeException {
        try {
            return client.send(request, BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new ServerFacadeException("Server facade exception");
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ServerFacadeException {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null) {
                throw new ServerFacadeException("Server facade exception");
            }

            throw new ServerFacadeException("Server facade exception");
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }

        return null;
    }

    public AuthData register(RegisterRequest registerRequest) throws ServerFacadeException {
        var request = buildRequest("POST", "/user", registerRequest, null);
        var response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }

    public void clearApplication() throws ServerFacadeException {
        var request = buildRequest("DELETE", "/db", null, null);
        sendRequest(request);
    }

    public AuthData login(LoginRequest loginRequest) throws ServerFacadeException {
        var request = buildRequest("POST", "/session", loginRequest, null);
        var response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }

    public void logout(LogoutRequest logoutRequest) throws ServerFacadeException {
        var request = buildRequest("DELETE", "/session", logoutRequest, logoutRequest.authToken());
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    public ListGamesResponse listGames(ListGamesRequest listGamesRequest) throws ServerFacadeException {
        var request = buildRequest("GET", "/game", listGamesRequest, listGamesRequest.authToken());
        var response = sendRequest(request);
        return handleResponse(response, ListGamesResponse.class);
    }

    public CreateGameResponse createGame(CreateGameRequest createGameRequest) throws ServerFacadeException {
        var request = buildRequest("POST", "/game", createGameRequest, createGameRequest.authToken());
        var response = sendRequest(request);
        return handleResponse(response, CreateGameResponse.class);
    }

    public void joinGame(JoinGameRequest joinGameRequest) throws ServerFacadeException {
        var request = buildRequest("PUT", "/game", joinGameRequest, joinGameRequest.authToken());
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
