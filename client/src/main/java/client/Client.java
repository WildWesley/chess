package client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

import chess.*;
import client.websocket.NotificationHandler;
import client.websocket.WebSocketFacade;
import facade.*;
import model.*;
import ui.*;
import websocket.messages.ErrorMessage;
import websocket.messages.Notification;

public class Client implements NotificationHandler{
    private final ServerFacade server;
    private final WebSocketFacade websocket;
    private State state = State.SIGNEDOUT;
    private ChessGame.TeamColor playerColor = null;
    private AuthData loginData = null;
    private Integer gameID = null;
    private ChessGame currentGame = null;
    private ArrayList<GameData> mostRecentlyListedGames = null;
    private boolean consideringResigning = false;

    public Client(String serverUrl) throws ServerFacadeException {
        server = new ServerFacade(serverUrl);
        websocket = new WebSocketFacade(serverUrl, this);
    }

    public void run() {
        System.out.println("Welcome to Nate's just super chess server. Sign in to play...");
        System.out.print(help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            if (consideringResigning) {
                System.out.print("Are you sure that you want to resign? (yes/no)");
            }
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
                case "list_games" -> listGames(params);
                case "create_game" -> createGame(params);
                case "play_game" -> playGame(params);
                case "observe_game" -> observeGame(params);
                case "quit" -> "quit";
                case "make_move" -> makeMove(params);
                case "redraw_board" -> redrawBoard(null);
                case "resign" -> resign();
                case "leave_game" -> leaveGame();
                case "highlight_moves" -> highlightMoves(params);
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
                    state = State.SIGNEDIN;
                    loginData = registerResponse;
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
                throw new ServerFacadeException("Error: login failed.");
            } catch (ServerFacadeException e) {
                throw new ServerFacadeException("Login information incorrect. Expected: " +
                        "'login <username> <password>'. Please try again or register");
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

    public String listGames(String... params) throws ServerFacadeException {
        if (loginData.authToken() != null) {
            try {
                String output = "Ongoing Games...\n";
                int gameCount = 1;
                ListGamesResponse listGamesResponse = server.listGames(new ListGamesRequest(loginData.authToken()));
                for (GameData game : listGamesResponse.games()) {
                    output = output + String.format("%d.\n\tGame Name: %s\n\tWhite Player: %s\n\tBlack Player: %s\n",
                            gameCount,
                            game.gameName(),
                            game.whiteUsername(),
                            game.blackUsername());
                    gameCount++;
                }
                mostRecentlyListedGames = listGamesResponse.games();
                return output;
            } catch (ServerFacadeException e) {
                throw new ServerFacadeException("List games failed.");
            }
        }
        throw new ServerFacadeException("Must be logged in to perform this action.");
    }

    public String createGame(String... params) throws ServerFacadeException {
        if (loginData.authToken() != null) {
            if (params.length >= 1) {
                try {
                    CreateGameResponse createGameResponse = server.createGame(new CreateGameRequest(params[0],
                            loginData.authToken()));
                    return "Game successfully created!";
                } catch (ServerFacadeException e) {
                    throw new ServerFacadeException("New game information incorrect. Expected: " +
                            "'create_game <game_name>'. Please try again.");
                }
            }
            throw new ServerFacadeException("New game information incorrect. Expected: 'create_game <game_name>'.");
        } else {
            throw new ServerFacadeException("Must be logged in to perform this action.");
        }
    }

    public int getGameIDGivenGameNumber(Integer gameNumber) throws ServerFacadeException {
        try {
            if (mostRecentlyListedGames == null) {
                ListGamesResponse listGamesResponse = server.listGames(new ListGamesRequest(loginData.authToken()));
                mostRecentlyListedGames = listGamesResponse.games();
            }
            return mostRecentlyListedGames.get(gameNumber - 1).gameID();
        } catch (Exception e) {
            throw new ServerFacadeException("Game number does not match listed games. " +
                    "Use list_games to get updated listed games.");
        }
    }

    public String playGame(String... params) throws ServerFacadeException {
        if (loginData.authToken() != null) {
            if (params.length >= 2) {
                try {
                    try {
                        server.joinGame(new JoinGameRequest(params[1].toUpperCase(),
                                getGameIDGivenGameNumber(Integer.parseInt(params[0])),
                                loginData.authToken()));
                        gameID = getGameIDGivenGameNumber(Integer.parseInt(params[0]));
                    } catch (Exception e) {
                        throw new ServerFacadeException("Game number must be a number.");
                    }
                    if (params[1].equalsIgnoreCase("WHITE")) {
                        playerColor = ChessGame.TeamColor.WHITE;
                    } else if (params[1].equalsIgnoreCase("BLACK")) {
                        playerColor = ChessGame.TeamColor.BLACK;
                    } else {
                        throw new ServerFacadeException("Invalid player color. Try 'WHITE' or 'BLACK'.");
                    }
                    state = State.PLAYINGGAME;
                    websocket.newPlayer(loginData.authToken(), gameID);
                    return String.format("Game successfully joined! Game Number: %s\n", params[0]);
                } catch (ServerFacadeException e) {
                    if (e.errorCode == 403) {
                        throw new ServerFacadeException("Player color taken. Use list_games to get updated player " +
                                "colors.");
                    }
                    throw new ServerFacadeException("Failed to join game. Expected: 'play_game <game_number> " +
                            "<playerColor>'. Please try again. Use list_games to get updated valid game_number's.");
                }
            } else {
                throw new ServerFacadeException("New game information incorrect. Expected: 'play_game <game_number> " +
                        "<playerColor>'. Please try again.");
            }
        } else {
            throw new ServerFacadeException("Must be logged in to perform this action.");
        }
    }

    public String observeGame(String... params) throws ServerFacadeException {
        if (loginData.authToken() != null) {
            if (params.length >= 1) {
                try {
                    int gameNum = Integer.parseInt(params[0]);
                    if (gameNum < mostRecentlyListedGames.size()) {
                        state = State.OBSERVINGGAME;
                        return String.format("Game successfully joined! Game Number: %s\n", params[0]);
                    } else {
                        throw new ServerFacadeException("Game number invalid. Use list_games to see valid " +
                                "game_numbers.");
                    }
                } catch (Exception e) {
                    throw new ServerFacadeException("Failed to join game. Expected: 'observe_game <game_number>'. " +
                            "Please try again.");
                }
            }
            throw new ServerFacadeException("New game information incorrect. Expected: 'play_game <game_number>'. " +
                    "Please try again.");
        } else {
            throw new ServerFacadeException("Must be logged in to perform this action.");
        }
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    - login <username> <password>
                    - register <username> <password> <email>
                    - quit
                    """;
        } else if (state == State.SIGNEDIN) {
            return """
                    - logout
                    - list_games
                    - create_game <game_name>
                    - play_game <game_id> <playerColor>
                    - observe_game <game_id>
                    """;
        } else if (state == State.PLAYINGGAME) {
            return """
                    - redraw_board
                    - leave_game
                    - make_move <current_position> <new_position> <promotion_piece (Q/R/N/B/none)>
                    - resign
                    - highlight_moves <current_position>
                    """;
        } else {
            return """
                    - redraw_board
                    - leave_game
                    - highlight_moves <current_position>
                    """;
        }
    }

    public boolean whiteSpace(ChessPosition position) {
        return (position.getRow() + position.getColumn()) % 2 == 1;
    }

    public String printPieces(ChessPiece piece) {
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
            output = EscapeSequences.SET_TEXT_COLOR_WHITE + printPieces(piece);
        } else {
            output = EscapeSequences.SET_TEXT_COLOR_BLACK + printPieces(piece);
        }
        System.out.print(output);
    }

    public void printSpace(ChessBoard board, int row, int col, ChessPosition highlightPosition) {
        ChessPosition positionInQuestion = new ChessPosition(row, col);
        ChessPiece pieceAtPosition = board.getPiece(positionInQuestion);
        ArrayList<ChessPosition> validPositions = new ArrayList<>();
        if (highlightPosition != null) {
            ChessPiece highlightPiece = board.getPiece(highlightPosition);
            ArrayList<ChessMove> validMoves = (ArrayList<ChessMove>) highlightPiece.pieceMoves(board, highlightPosition);
            for (ChessMove move : validMoves) {
                validPositions.add(move.getEndPosition());
            }
        }
        if (whiteSpace(positionInQuestion)) {
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_BROWN);
            if (validPositions.contains(positionInQuestion)) {
                System.out.print(EscapeSequences.SET_BG_COLOR_GREEN);
            }
        } else {
            System.out.print(EscapeSequences.SET_BG_COLOR_BROWN);
            if (validPositions.contains(positionInQuestion)) {
                System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
            }
        }

        if (pieceAtPosition == null) {
            System.out.print(EscapeSequences.EMPTY);
        } else {
            handlePiece(pieceAtPosition);
        }
    }

    // Important to remember that when adding up i and j, if they are odd, it's light,
    // and if they're even, it's dark
    public void printBoardWhite(ChessGame game, ChessPosition highlightPosition) {
        ChessBoard board = game.getBoard();
        ArrayList<String> boardLetters =
                new ArrayList<>(Arrays.asList(EscapeSequences.EMPTY, " a\u2003", " b\u2003", " c\u2003", " d\u2003",
                        " e\u2003", " f\u2003", " g\u2003", " h\u2003",
                        EscapeSequences.EMPTY,
                        EscapeSequences.RESET_BG_COLOR + "\n"));
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK);
        for (String col : boardLetters) {
            System.out.print(col);
        }
        for (int i = 8; i >= 1; i--) {
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY +
                    EscapeSequences.SET_TEXT_COLOR_BLACK + "\u2003" +
                    Integer.toString(i) + " ");
            for (int j = 1; j <= 8; j++) {
                printSpace(board, i, j, highlightPosition);
            }
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY +
                    EscapeSequences.SET_TEXT_COLOR_BLACK + "\u2003" +
                    Integer.toString(i) + " ");
            System.out.print(EscapeSequences.RESET_BG_COLOR);
            System.out.print("\n");
        }
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        for (String col : boardLetters) {
            System.out.print(col);
        }
    }

    public void printBoardBlack(ChessGame game, ChessPosition highlightPosition) {
        ChessBoard board = game.getBoard();
        ArrayList<String> boardLetters =
                new ArrayList<>(Arrays.asList(EscapeSequences.EMPTY, " h\u2003", " g\u2003", " f\u2003", " e\u2003",
                        " d\u2003", " c\u2003", " b\u2003",  " a\u2003",
                        EscapeSequences.EMPTY,
                        EscapeSequences.RESET_BG_COLOR + "\n"));
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK);
        for (String col : boardLetters) {
            System.out.print(col);
        }
        for (int i = 1; i <= 8; i++) {
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY +
                    EscapeSequences.SET_TEXT_COLOR_BLACK + "\u2003" +
                    Integer.toString(i) + " ");
            for (int j = 8; j >= 1; j--) {
                printSpace(board, i, j, highlightPosition);
            }
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY +
                    EscapeSequences.SET_TEXT_COLOR_BLACK + "\u2003" +
                    Integer.toString(i) + " ");
            System.out.print(EscapeSequences.RESET_BG_COLOR);
            System.out.print("\n");
        }
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        for (String col : boardLetters) {
            System.out.print(col);
        }
    }

    private int translateLetter(char col) throws ServerFacadeException {
        switch (col) {
            case 'a' -> {
                return 1;
            }
            case 'b' -> {
                return 2;
            }
            case 'c' -> {
                return 3;
            }
            case 'd' -> {
                return 4;
            }
            case 'e' -> {
                return 5;
            }
            case 'f' -> {
                return 6;
            }
            case 'g' -> {
                return 7;
            }
            case 'h' -> {
                return 8;
            }
            default -> {
                throw new ServerFacadeException("Error: Invalid move format. Use make_move <start_position> " +
                        "<end_position> <promotion_piece>. Ex: 'make_move e2 e4 none'.");
            }
        }
    }

    // e2 e4 none
    public String makeMove(String... params) throws ServerFacadeException {
        if (state == State.PLAYINGGAME) {
            if (params.length >= 3) {
                try {
                    ChessPiece.PieceType promotionPiece;
                    switch (params[2]) {
                        case "Q" -> promotionPiece = ChessPiece.PieceType.QUEEN;
                        case "R" -> promotionPiece = ChessPiece.PieceType.ROOK;
                        case "N" -> promotionPiece = ChessPiece.PieceType.KNIGHT;
                        case "B" -> promotionPiece = ChessPiece.PieceType.BISHOP;
                        default -> promotionPiece = null;
                    }
                    ChessMove move = new ChessMove(new ChessPosition(params[0].charAt(1), translateLetter(params[0].charAt(0))),
                            new ChessPosition(params[0].charAt(1), translateLetter(params[0].charAt(0))),
                            promotionPiece);

                    websocket.moveMade(move, loginData.authToken(), gameID);
                    return "";
                } catch (Exception e) {
                    throw new ServerFacadeException(e.getMessage());
                }
            } else {
                throw new ServerFacadeException("Error: Invalid move format. Use make_move <start_position> " +
                        "<end_position> <promotion_piece>. Ex: 'make_move e2 e4 none'.");
            }
        } else {
            throw new ServerFacadeException("Error: Must be playing a game to perform this action.");
        }
    }

    public String resign(String... params) throws ServerFacadeException {
        if (state == State.PLAYINGGAME) {
            if (!consideringResigning) {
                consideringResigning = true;
                return "You are considering resigning";
            } else {
                try {
                    if (params.length >= 1) {
                        if (Objects.equals(params[0], "yes")) {
                            websocket.playerResigned(loginData.authToken(), gameID);
                            consideringResigning = false;
                            return "Successfully resigned.";
                        } else {
                            consideringResigning = false;
                            return "Resign canceled.";
                        }
                    } else {
                        throw new ServerFacadeException("Error: did not respond yes/no");
                    }
                } catch (Exception e) {
                    throw new ServerFacadeException(e.getMessage());
                }
            }
        } else {
            throw new ServerFacadeException("Error: Invalid move format. Use make_move <start_position> " +
                    "<end_position> <promotion_piece>. Ex: 'make_move e2 e4 none'.");
        }
    }

    public String redrawBoard(ChessPosition highlightPosition) {
        System.out.print("\n");
        if (playerColor == ChessGame.TeamColor.BLACK) {
            printBoardBlack(currentGame, highlightPosition);
        } else {
            printBoardWhite(currentGame, highlightPosition);
        }
        return "Board successfully redrawn!";
    }

    public String leaveGame() throws ServerFacadeException {
        if (state == State.PLAYINGGAME || state == State.OBSERVINGGAME) {
            if (state == State.PLAYINGGAME) {
                websocket.leftGame(loginData.authToken(), gameID);
            } else {
                websocket.leftGame(loginData.authToken(), gameID);
            }
            state = State.SIGNEDIN;
            return "Successfully left game.";
        } else {
            throw new ServerFacadeException("Error: Must be playing or observing game to use command.");
        }
    }

    public String highlightMoves(String... params) throws ServerFacadeException {
        if (state == State.PLAYINGGAME || state == State.OBSERVINGGAME) {
            if (params.length >= 1) {
                try {
                    ChessPosition highlightPosition =
                            new ChessPosition(translateLetter(params[0].charAt(1)), params[0].charAt(0));
                    redrawBoard(highlightPosition);
                    return "";
                } catch (Exception e) {
                    throw new ServerFacadeException("Error: Unable to highlight piece moves.");
                }
            } else {
                throw new ServerFacadeException("Error: Invalid command format. Use 'highlight_moves " +
                        "<piece_position>.");
            }
        } else {
            throw new ServerFacadeException("Error: Must be playing or observing to highlight_moves.");
        }
    }

    @Override
    public void notify(Notification notification) {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + notification.getMessage());
        printPrompt();
    }

    @Override
    public void notifyError(ErrorMessage errorMessage) {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + errorMessage.getMessage());
        printPrompt();
    }

    @Override
    public void loadGame(ChessGame game) {
        currentGame = game;
        redrawBoard(null);
    }
}
