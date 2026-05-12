package dataaccess;

import chess.ChessGame;
import model.CreateGameResponse;
import model.GameData;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

// MemoryGameDataAccess holds the hashmap that is used to store all the game data on the server with the GameData
// record type. It implements the UserDataAccess interface so that when database implementation is added, it can easily
// be switched between the two.
public class MySqlGameDataAccess implements GameDataAccess {
    // String is the authToken
    private int runningGameID = 1;

    public MySqlGameDataAccess() throws DataAccessException {
        configureDatabase();
    }

    // TODO: Figure out how to store ChessGame in a variable
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  games (
              `gameID` varchar(256) NOT NULL,
              `whiteUsername` varchar(256) NOT NULL,
              `blackUsername` varchar(256) NOT NULL,
              `gameName` varchar(256) NOT NULL,
              `game` varchar(256) NOT NULL,
              PRIMARY KEY (`gameID`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };


    private void configureDatabase() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (Exception ex) {
            throw new DataAccessException("Unable to configure database");
        }
    }

    @Override
    public CreateGameResponse createGame(String gameName) throws DataAccessException {
        var statement = "INSERT INTO games (gameID, whiteUsername, blackUsername, gameName, gameBoard, gameTeamTurn) " +
                "VALUES (?, ?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, userData.username());
                preparedStatement.setString(2, userData.password());
                preparedStatement.setString(3, userData.email());
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException("Unable to add to database");
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return gameDatas.get(gameID);
    }

    @Override
    public Collection<GameData> getAllGames() throws DataAccessException {
        return gameDatas.values();
    }

    @Override
    public void clear() throws DataAccessException {
        gameDatas.clear();
    }

    @Override
    public void updateGameDataWhite(int gameID, String username) throws DataAccessException {
        GameData oldGameData = gameDatas.get(gameID);
        GameData newGameData = new GameData(oldGameData.gameID(),
                username,
                oldGameData.blackUsername(),
                oldGameData.gameName(),
                oldGameData.game());
        gameDatas.put(gameID, newGameData);
    }

    @Override
    public void updateGameDataBlack(int gameID, String username) throws DataAccessException {
        GameData oldGameData = gameDatas.get(gameID);
        GameData newGameData = new GameData(oldGameData.gameID(),
                oldGameData.whiteUsername(),
                username,
                oldGameData.gameName(),
                oldGameData.game());
        gameDatas.put(gameID, newGameData);
    }
}