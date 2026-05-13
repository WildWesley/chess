package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.CreateGameResponse;
import model.GameData;
import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

// MemoryGameDataAccess holds the hashmap that is used to store all the game data on the server with the GameData
// record type. It implements the UserDataAccess interface so that when database implementation is added, it can easily
// be switched between the two.
public class MySqlGameDataAccess implements GameDataAccess {
    // String is the authToken
    private int runningGameID = 1;

    public MySqlGameDataAccess() throws DataAccessException {
        DatabaseManager.configureDatabase(createStatements);
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  games (
              `gameID` varchar(256) NOT NULL,
              `whiteUsername` varchar(256) DEFAULT NULL,
              `blackUsername` varchar(256) DEFAULT NULL,
              `gameName` varchar(256) NOT NULL,
              `game` TEXT NOT NULL,
              PRIMARY KEY (`gameID`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    @Override
    public CreateGameResponse createGame(String gameName) throws DataAccessException {
        var statement = "INSERT INTO games (gameID, gameName, game) VALUES (?, ?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                ChessGame game = new ChessGame();
                String json = new Gson().toJson(game);
                preparedStatement.setInt(1, runningGameID);
                preparedStatement.setString(2, gameName);
                preparedStatement.setString(3, json);
                preparedStatement.executeUpdate();
                CreateGameResponse gameResponse = new CreateGameResponse(runningGameID);
                runningGameID++;
                return gameResponse;
            }
        } catch (Exception e) {
            throw new DataAccessException("Unable to add to database");
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games WHERE gameID=?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setInt(1, gameID);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    var json = resultSet.getString("game");
                    GameData gameData = new GameData(resultSet.getInt("gameID"),
                            resultSet.getString("whiteUsername"),
                            resultSet.getString("blackUsername"),
                            resultSet.getString("gameName"),
                            new Gson().fromJson(json, ChessGame.class));
                    return gameData;
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Unable to add to database");
        }
    }

    @Override
    public Collection<GameData> getAllGames() throws DataAccessException {
        var gameList = new ArrayList<GameData>();
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet resultSet = ps.executeQuery()) {
                    while (resultSet.next()) {
                        var json = resultSet.getString("game");
                        GameData gameData = new GameData(resultSet.getInt("gameID"),
                                resultSet.getString("whiteUsername"),
                                resultSet.getString("blackUsername"),
                                resultSet.getString("gameName"),
                                new Gson().fromJson(json, ChessGame.class));
                        gameList.add(gameData);
                    }
                    return gameList;
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Unable to add to database");
        }
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE TABLE games";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException("Unable to add to database");
        }
    }

    // UPDATE pet SET name = 'fido' WHERE id = 1

    @Override
    public void updateGameDataWhite(int gameID, String username) throws DataAccessException {
        var statement = "UPDATE games SET whiteUsername=? WHERE gameID=?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, username);
                preparedStatement.setInt(2, gameID);
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException("Unable to add to database");
        }
    }

    @Override
    public void updateGameDataBlack(int gameID, String username) throws DataAccessException {
        var statement = "UPDATE games SET blackUsername=? WHERE gameID=?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, username);
                preparedStatement.setInt(2, gameID);
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException("Unable to add to database");
        }
    }
}