package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import io.javalin.websocket.*;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.websocket.api.Session;
import server.Server;
import service.ChessService;
import websocket.messages.*;
import websocket.commands.UserGameCommand;

import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();
    AuthDataAccess authDataAccess;
    UserDataAccess userDataAccess;
    GameDataAccess gameDataAccess;

    public WebSocketHandler(ChessService service) {
        this.authDataAccess = service.getAuthDataAccess();
        this.userDataAccess = service.getUserDataAccess();
        this.gameDataAccess = service.getGameDataAccess();
    }

    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx) {
        try {
            UserGameCommand userGameCommand = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            switch (userGameCommand.getCommandType()) {
                case MAKE_MOVE -> makeMove(userGameCommand, ctx.session);
                case LEAVE -> leaveGame(userGameCommand, ctx.session);
                case CONNECT -> newPlayer(userGameCommand, ctx.session);
                case RESIGN -> resign(userGameCommand, ctx.session);
            }
        } catch (IOException | DataAccessException ex) {
            ex.printStackTrace();
        }
    }

    private String translateNumber(int col) {
        switch (col) {
            case 1 -> {
                return "a";
            }
            case 2 -> {
                return "b";
            }
            case 3 -> {
                return "c";
            }
            case 4 -> {
                return "d";
            }
            case 5 -> {
                return "e";
            }
            case 6 -> {
                return "f";
            }
            case 7 -> {
                return "g";
            }
            case 8 -> {
                return "h";
            }
            default -> {
                return "error";
            }
        }
    }

    String handlePromotion(ChessMove move) {
        if (move.getPromotionPiece() == null) {
            return "";
        } else {
            String inputString;
            switch (move.getPromotionPiece()) {
                case QUEEN -> {
                    inputString = "Queen";
                }
                case KNIGHT -> {
                    inputString = "Knight";
                }
                case BISHOP -> {
                    inputString = "Bishop";
                }
                case ROOK -> {
                    inputString = "Rook";
                }
                default -> {
                    inputString = "Error";
                }
            }
            return String.format(" The pawn promoted to a %s", inputString);
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    private void makeMove(UserGameCommand userGameCommand, Session session) throws IOException, DataAccessException {
        try {
            // TODO: Make sure userGameCommand has all needed information
            if (userGameCommand.getMove() == null) {
                connections.singleSend(session, new ErrorMessage("Error: move input invalid. Use help to see " +
                        "example."));
                return;
            }
            AuthData authData = authDataAccess.getAuth(userGameCommand.getAuthToken());
            Integer gameID = userGameCommand.getGameID();
            ChessMove move = userGameCommand.getMove();
            try {
                // TODO: Update game data with move
                // TODO: Use try/catch to catch invalid move exception, if there is exception, send the single session a message
                GameData gameData = gameDataAccess.getGame(gameID);
                ChessGame updatedGame = gameData.game();
                ChessGame.TeamColor currentTurn = updatedGame.getTeamTurn();
                if (gameData.whiteUsername().equals(authData.username())) {
                    if (currentTurn != ChessGame.TeamColor.WHITE) {
                        connections.singleSend(session, new ErrorMessage("Error: You can only make moves on " +
                                "your turn."));
                        return;
                    }
                } else if (gameData.blackUsername().equals(authData.username())) {
                    if (currentTurn != ChessGame.TeamColor.BLACK) {
                        connections.singleSend(session, new ErrorMessage("Error: You can only make moves on " +
                                "your turn."));
                        return;
                    }
                } else if (gameData.game().isGameOver()) {
                    connections.singleSend(session, new ErrorMessage("Error: Cannot make move when game is over."));
                    return;
                }
                gameDataAccess.updateGame(gameID, move);
                var loadGameMessage = new LoadGameMessage(updatedGame);
                connections.broadcast(gameID, null, loadGameMessage);

                var message = String.format("%s moved %s%d to %s%d.%s", authData.username(),
                        translateNumber(move.getStartPosition().getColumn()),
                        move.getStartPosition().getRow(),
                        translateNumber(move.getEndPosition().getColumn()),
                        move.getEndPosition().getRow(),
                        handlePromotion(move));
                var notification = new Notification(message);
                connections.broadcast(userGameCommand.getGameID(), session, notification);
                // TODO: Check if in check/checkmate/stalemate, if so broadcast to all sessions
                // TODO: If checkmate/stalemate, make sure that no more moves can be made
                Notification gameStatus;
                if (updatedGame.isInCheckmate(currentTurn)) {
                    if (currentTurn == ChessGame.TeamColor.WHITE) {
                        gameStatus = new Notification("Game over. Black wins.");
                    } else {
                        gameStatus = new Notification("Game over. White wins.");
                    }
                    connections.broadcast(userGameCommand.getGameID(), null, gameStatus);
                } else if (updatedGame.isInCheck(currentTurn)) {
                    if (currentTurn == ChessGame.TeamColor.WHITE) {
                        gameStatus = new Notification("White is in check.");
                    } else {
                        gameStatus = new Notification("Black is in check.");
                    }
                    connections.broadcast(userGameCommand.getGameID(), null, gameStatus);
                } else if (updatedGame.isInStalemate(currentTurn)) {
                    if (currentTurn == ChessGame.TeamColor.WHITE) {
                        gameStatus = new Notification("Game over. White is in stalemate. It's a draw.");
                    } else {
                        gameStatus = new Notification("Game over. Black is in stalemate. It's a draw.");
                    }
                    connections.broadcast(userGameCommand.getGameID(), null, gameStatus);
                }
            } catch (Exception e) {
                connections.singleSend(session, new ErrorMessage(e.getMessage()));
            }
        } catch (Exception e) {
            throw new DataAccessException("Error: Invalid move entered. Try make_move <original_position> " +
                    "<new_position>. Ex: 'make_move e2 e4'.");
        }
    }

    private void leaveGame(UserGameCommand userGameCommand, Session session) throws IOException, DataAccessException {
        try {
            AuthData authData = authDataAccess.getAuth(userGameCommand.getAuthToken());
            Integer gameID = userGameCommand.getGameID();
            String username = authData.username();
            GameData gameData = gameDataAccess.getGame(gameID);
            var message = String.format("%s has left the game.", authData.username());
            var notification = new Notification(message);
            connections.broadcast(gameID, session, notification);
            if (gameData.whiteUsername() != null && gameData.whiteUsername().equals(username)) {
                GameData updatedGameData = new GameData(gameData.gameID(),
                        null,
                        gameData.blackUsername(),
                        gameData.gameName(),
                        gameData.game());
                gameDataAccess.updateGameData(gameID, updatedGameData);
            } else if (gameData.blackUsername() != null && gameData.blackUsername().equals(username)) {
                GameData updatedGameData = new GameData(gameData.gameID(),
                        gameData.whiteUsername(),
                        null,
                        gameData.gameName(),
                        gameData.game());
                gameDataAccess.updateGameData(gameID, updatedGameData);
            } else {
                connections.singleSend(session, new ErrorMessage("Error: game observers cannot resign."));
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private void newPlayer(UserGameCommand userGameCommand, Session session) throws IOException, DataAccessException {
        try {
            String message;
            AuthData authData = authDataAccess.getAuth(userGameCommand.getAuthToken());
            if (authData == null) {
                connections.singleSend(session, new ErrorMessage("Error: user not logged in."));
                return;
            }
            Integer gameID = userGameCommand.getGameID();
            String username = authData.username();
            GameData gameData = gameDataAccess.getGame(gameID);
            if (gameData == null) {
                connections.singleSend(session, new ErrorMessage("Error: invalid gameID. Use list_games to get " +
                        "a list of current, valid game ID's."));
                return;
            }
            if (gameData.whiteUsername().equals(username)) {
                message = String.format("%s has joined the game as white.", username);
            } else if (gameData.blackUsername().equals(username)) {
                message = String.format("%s has joined the game as black.", username);
            } else {
                message = String.format("%s has joined the game as an observer.", username);
            }
            connections.add(gameID, session);

            ChessGame game = gameDataAccess.getGame(gameID).game();
            var loadGameMessage = new LoadGameMessage(game);
            connections.singleSend(session, loadGameMessage);

            var notification = new Notification(message);
            connections.broadcast(gameID, session, notification);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private void resign(UserGameCommand userGameCommand, Session session) throws IOException, DataAccessException {
        try {
            AuthData authData = authDataAccess.getAuth(userGameCommand.getAuthToken());
            Integer gameID = userGameCommand.getGameID();
            String username = authData.username();
            GameData gameData = gameDataAccess.getGame(gameID);
            String message;
            if (!gameData.game().isGameOver()) {
                if (gameData.whiteUsername().equals(username)) {
                    message = String.format("%s resigned from the game. Black wins.", username);
                } else if (gameData.blackUsername().equals(username)) {
                    message = String.format("%s resigned from the game. White wins.", username);
                } else {
                    message = "Error: player was not playing game.";
                }
                // TODO: Ask if this method of updating game will work
                gameData.game().resign();
                gameDataAccess.updateGameData(gameID, new GameData(gameID,
                        gameData.whiteUsername(),
                        gameData.blackUsername(),
                        gameData.gameName(),
                        gameData.game()));
                connections.broadcast(gameID, null, new Notification(message));
            } else {
                connections.singleSend(session, new ErrorMessage("Error: Cannot resign when game is over."));
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}