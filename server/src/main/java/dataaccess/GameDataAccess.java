package dataaccess;

public interface GameDataAccess {
    // TODO: Figure out what return types to use for these
    void createGame() throws DataAccessException;

    void getGame() throws DataAccessException;

    void getAllGames() throws DataAccessException;

    void clear() throws DataAccessException;
}