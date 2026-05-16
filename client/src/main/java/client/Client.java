package client;

import java.util.Arrays;
import java.util.Scanner;

import Facade.*;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import model.*;
import ui.*;

public class Client {
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;
    private AuthData loginData = null;
    private ChessBoard starterBoard = new ChessBoard();

    public Client(String serverUrl) throws ServerFacadeException {
        server = new ServerFacade(serverUrl);
        starterBoard.resetBoard();
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
                printBoardWhite();
                result = eval(line);
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }


//    public void notify(Notification notification) {
//        System.out.println(RED + notification.message());
//        printPrompt();
//    }
//
    private void printPrompt() {
        System.out.print("\n" + EscapeSequences.RESET_TEXT_COLOR + ">>> " + EscapeSequences.SET_TEXT_COLOR_GREEN);
    }


    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register(params);
                case "logout" -> logout(params);
//                case "list" -> listPets();
//                case "signout" -> signOut();
//                case "adopt" -> adoptPet(params);
//                case "adoptall" -> adoptAllPets();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ServerFacadeException ex) {
            return ex.getMessage();
        }
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

    public String login(String... params) throws ServerFacadeException {
        if (params.length >= 2) {
            try {
                AuthData loginResponse = server.login(new LoginRequest(params[0], params[1]));
                if (loginResponse.authToken() != null) {
                    state = State.SIGNEDIN;
                    loginData = loginResponse;
                    return String.format("Welcome back %s.", loginResponse.username());
                }
            } catch (ServerFacadeException e) {
                throw new ServerFacadeException("Login information incorrect. Expected: 'login <username> <password>'." +
                        " Please try again or register");
            }
        }
        throw new ServerFacadeException("Login format incorrect. Expected: 'login <username> <password>'");
    }

    public String logout(String... params) throws ServerFacadeException {
        if (loginData.authToken() != null) {
            try {
                server.logout(new LogoutRequest(loginData.authToken()));
                state = State.SIGNEDOUT;
                return "Successfully logged out!";
            } catch (ServerFacadeException e) {
                throw new ServerFacadeException("Logout failed.");
            }
        }
        throw new ServerFacadeException("Must be logged in to perform this action.");
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    - login <username> <password>
                    - register <username> <password> <email>
                    - quit
                    """;
        }
        return """
                - logout
                - list_games
                - create_game <game_name>
                - join_game <game_id> <player_color>
                """;
    }

    public boolean whiteSpace(ChessPosition position) {
        return (position.getRow() + position.getColumn()) % 2 == 1;
    }

    public String handleWhitePiece(ChessPiece piece) {
        String output = "";
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
            output = EscapeSequences.BLACK_PAWN;
        } else if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            output = EscapeSequences.BLACK_KING;
        } else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
            output = EscapeSequences.BLACK_QUEEN;
        } else if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
            output = EscapeSequences.BLACK_BISHOP;
        } else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
            output = EscapeSequences.BLACK_KNIGHT;
        } else if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
            output = EscapeSequences.BLACK_ROOK;
        }
        return output;
    }

    public String handleBlackPiece(ChessPiece piece) {
        String output = "";
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
            output = EscapeSequences.BLACK_PAWN;
        } else if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            output = EscapeSequences.BLACK_KING;
        } else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
            output = EscapeSequences.BLACK_QUEEN;
        } else if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
            output = EscapeSequences.BLACK_BISHOP;
        } else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
            output = EscapeSequences.BLACK_KNIGHT;
        } else if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
            output = EscapeSequences.BLACK_ROOK;
        }
        return output;
    }

    public void handlePiece(ChessPiece piece) {
        String output;
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            output = EscapeSequences.SET_TEXT_COLOR_WHITE + handleWhitePiece(piece);
        } else {
            output = EscapeSequences.SET_TEXT_COLOR_BLACK + handleBlackPiece(piece);
        }
        System.out.print(output);
    }

    // Important to remember that when adding up i and j, if they are odd, it's light,
    // and if they're even, it's dark
    public void printBoardWhite() {
        ChessPosition positionInQuestion;
        ChessPiece pieceAtPosition;
        for (int i = 8; i >= 1; i--) {
            for (int j = 1; j <= 8; j++) {
                positionInQuestion = new ChessPosition(i, j);
                pieceAtPosition = starterBoard.getPiece(positionInQuestion);
                if (whiteSpace(positionInQuestion)) {
                    System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_BROWN);
                } else {
                    System.out.print(EscapeSequences.SET_BG_COLOR_BROWN);
                }

                if (pieceAtPosition == null) {
                    System.out.print(EscapeSequences.EMPTY);
                } else {
                    handlePiece(pieceAtPosition);
                }
            }
            System.out.print(EscapeSequences.RESET_BG_COLOR);
            System.out.print("\n");
        }
    }

    public void printBoardBlack() {
        ChessPosition positionInQuestion;
        ChessPiece pieceAtPosition;
        for (int i = 1; i <= 8; i++) {
            for (int j = 8; j >= 1; j--) {
                positionInQuestion = new ChessPosition(i, j);
                pieceAtPosition = starterBoard.getPiece(positionInQuestion);
                if (whiteSpace(positionInQuestion)) {
                    System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_BROWN);
                } else {
                    System.out.print(EscapeSequences.SET_BG_COLOR_BROWN);
                }

                if (pieceAtPosition == null) {
                    System.out.print(EscapeSequences.EMPTY);
                } else {
                    handlePiece(pieceAtPosition);
                }
            }
            System.out.print(EscapeSequences.RESET_BG_COLOR);
            System.out.print("\n");
        }
    }

    private void assertSignedIn() throws ServerFacadeException {
        if (state == State.SIGNEDOUT) {
            throw new ServerFacadeException("Must be signed in to perform this action.");
        }
    }
}
