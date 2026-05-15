package server;

import Facade.ServerFacadeException;
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

    private HttpRequest buildRequest(String method, String path, Object body) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
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

    public void register(RegisterRequest registerRequest) throws ServerFacadeException {
        var request = buildRequest("POST", "/user", registerRequest);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    public void clearApplication() throws ServerFacadeException {
        var request = buildRequest("DELETE", "/db", null);
        sendRequest(request);
    }

    public void login(LoginRequest loginRequest) throws ServerFacadeException {
        var request = buildRequest("POST", "/session", pet);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    public void login(LoginRequest loginRequest) throws ServerFacadeException {
        var request = buildRequest("POST", "/session", pet);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    public Pet addPet(Pet pet) throws ResponseException {
        var request = buildRequest("POST", "/pet", pet);
        var response = sendRequest(request);
        return handleResponse(response, Pet.class);
    }

    public void deletePet(int id) throws ResponseException {
        var path = String.format("/pet/%s", id);
        var request = buildRequest("DELETE", path, null);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    public void deleteAllPets() throws ResponseException {
        var request = buildRequest("DELETE", "/pet", null);
        sendRequest(request);
    }

    public PetList listPets() throws ResponseException {
        var request = buildRequest("GET", "/pet", null);
        var response = sendRequest(request);
        return handleResponse(response, PetList.class);
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
