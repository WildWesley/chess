package dataaccess;

import com.google.gson.Gson;

import model.UserData;
import org.eclipse.jetty.server.Authentication;

import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;


public class MySqlUserDataAccess implements UserDataAccess {

    public MySqlUserDataAccess() throws DataAccessException {
        DatabaseManager.configureDatabase(createStatements);
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  users (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
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
    public UserData getUser(String username) throws DataAccessException {
        var statement = "SELECT username, password, email FROM users WHERE username=?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return new UserData(resultSet.getString("username"),
                            resultSet.getString("password"),
                            resultSet.getString("email"));
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Unable to add to database");
        }
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE TABLE users";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException("Unable to add to database");
        }
    }
}
