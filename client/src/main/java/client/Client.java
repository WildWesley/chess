package client;

import java.util.Arrays;
import java.util.Scanner;

import Facade.*;
import com.google.gson.Gson;
import model.*;
import ui.*;

public class Client {
    private String visitorName = null;
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;

    public Client(String serverUrl) throws ServerFacadeException {
        server = new ServerFacade(serverUrl);
    }

    public void run() {
        System.out.println("Welcome to Nate's just super chess server. Sign in to play...");
        System.out.print(help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }


    public void notify(Notification notification) {
        System.out.println(RED + notification.message());
        printPrompt();
    }

    private void printPrompt() {
        System.out.print("\n" + RESET + ">>> " + GREEN);
    }


    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> rescuePet(params);
                case "list" -> listPets();
                case "signout" -> signOut();
                case "adopt" -> adoptPet(params);
                case "adoptall" -> adoptAllPets();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ServerFacadeException ex) {
            return ex.getMessage();
        }
    }

    public String login(String... params) throws ServerFacadeException {
        if (params.length >= 2) {
            try {
                AuthData loginResponse = server.login(new LoginRequest(params[0], params[1]));
                if (loginResponse.authToken() != null) {
                    state = State.SIGNEDIN;
                    return String.format("Welcome back %s.", loginResponse.username());
                }
            } catch (ServerFacadeException e) {
                throw new ServerFacadeException("Login information incorrect. Expected: 'login <username> <password>'." +
                        " Please try again or register");
            }
        }
        throw new ServerFacadeException("Login format incorrect. Expected: 'login <username> <password>'");
    }

    public String register(String... params) throws ServerFacadeException {
        if (params.length >= 3) {
            try {
                AuthData registerResponse = server.register(new RegisterRequest(params[0], params[1], params[2]));
                if (registerResponse.authToken() != null) {
                    return String.format("Successfully registered: %s!", registerResponse.username());
                }
            } catch (ServerFacadeException e) {
                throw new ServerFacadeException("Register failed. User information already registered. Try again " +
                        "using 'register <username> <password> <email>', or if already registered, try logging in.");
            }
        }
        throw new ServerFacadeException("Register format incorrect. Expected: " +
                "'register <username> <password> <email>'");
    }

    public String rescuePet(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length >= 2) {
            String name = params[0];
            PetType type = PetType.valueOf(params[1].toUpperCase());
            var pet = new Pet(0, name, type);
            pet = server.addPet(pet);
            return String.format("You rescued %s. Assigned ID: %d", pet.name(), pet.id());
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <name> <CAT|DOG|FROG>");
    }

    public String listPets() throws ResponseException {
        assertSignedIn();
        PetList pets = server.listPets();
        var result = new StringBuilder();
        var gson = new Gson();
        for (Pet pet : pets) {
            result.append(gson.toJson(pet)).append('\n');
        }
        return result.toString();
    }

    public String adoptPet(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length == 1) {
            try {
                int id = Integer.parseInt(params[0]);
                Pet pet = getPet(id);
                if (pet != null) {
                    server.deletePet(id);
                    return String.format("%s says %s", pet.name(), pet.sound());
                }
            } catch (NumberFormatException ignored) {
            }
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <pet id>");
    }

    public String adoptAllPets() throws ResponseException {
        assertSignedIn();
        var buffer = new StringBuilder();
        for (Pet pet : server.listPets()) {
            buffer.append(String.format("%s says %s%n", pet.name(), pet.sound()));
        }

        server.deleteAllPets();
        return buffer.toString();
    }

    public String signOut() throws ResponseException {
        assertSignedIn();
        ws.leavePetShop(visitorName);
        state = State.SIGNEDOUT;
        return String.format("%s left the shop", visitorName);
    }

    private Pet getPet(int id) throws ResponseException {
        for (Pet pet : server.listPets()) {
            if (pet.id() == id) {
                return pet;
            }
        }
        return null;
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    - signIn <yourname>
                    - quit
                    """;
        }
        return """
                - list
                - adopt <pet id>
                - rescue <name> <CAT|DOG|FROG|FISH>
                - adoptAll
                - signOut
                - quit
                """;
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(ResponseException.Code.ClientError, "You must sign in");
        }
    }
}
