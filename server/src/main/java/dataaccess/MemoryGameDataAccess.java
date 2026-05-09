package dataaccess;

import chess.ChessGame;
import model.*;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDataAccess implements GameDataAccess {
    // String is the authToken
    final private HashMap<Integer, GameData> gameDatas = new HashMap<>();
    private int runningGameID = 0;

    @Override
    public CreateGameResponse createGame(String gameName) throws DataAccessException {
        GameData newGameData = new GameData(runningGameID,
                "",
                "",
                gameName,
                new ChessGame());
        gameDatas.put(newGameData.gameID(), newGameData);
        runningGameID++;
        return new CreateGameResponse(newGameData.gameID());
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
    public void updateGameDataWhite(int gameID, String username) {
        GameData oldGameData = gameDatas.get(gameID);
        GameData newGameData = new GameData(oldGameData.gameID(),
                username,
                oldGameData.blackUsername(),
                oldGameData.gameName(),
                oldGameData.game());
        gameDatas.put(gameID, newGameData);
    }

    @Override
    public void updateGameDataBlack(int gameID, String username) {
        GameData oldGameData = gameDatas.get(gameID);
        GameData newGameData = new GameData(oldGameData.gameID(),
                oldGameData.whiteUsername(),
                username,
                oldGameData.gameName(),
                oldGameData.game());
        gameDatas.put(gameID, newGameData);
    }
}