package dataaccess;

import model.*;

import java.util.HashMap;

public class MemoryGameDataAccess implements GameDataAccess {
    // String is the authToken
    final private HashMap<Integer, GameData> gameDatas = new HashMap<>();

    @Override
    public void createGame() throws DataAccessException {

    }

    @Override
    public void getGame() throws DataAccessException {

    }

    @Override
    public void getAllGames() throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {
        gameDatas.clear();
    }
}