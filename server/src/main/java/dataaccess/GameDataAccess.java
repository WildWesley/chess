package dataaccess;

import chess.ChessGame;
import chess.ChessMove;
import model.CreateGameRequest;
import model.CreateGameResponse;
import model.GameData;

import java.util.Collection;

public interface GameDataAccess {
    CreateGameResponse createGame(String gameName) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    Collection<GameData> getAllGames() throws DataAccessException;

    void clear() throws DataAccessException;

    void updateGameData(int gameID, GameData gameData) throws DataAccessException;

    void updateGame(int gameID, ChessMove move) throws DataAccessException;

    void updateGameDataWhite(int gameID, String username) throws DataAccessException;

    void updateGameDataBlack(int gameID, String username) throws DataAccessException;
}