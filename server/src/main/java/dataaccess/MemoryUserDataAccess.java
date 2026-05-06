package dataaccess;

import model.*;

import java.util.HashMap;

public class MemoryUserDataAccess implements UserDataAccess {
    final private HashMap<String, UserData> userDatas = new HashMap<>();

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        userDatas.put(userData.username(), userData);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return userDatas.get(username);
    }

    @Override
    public void clear() throws DataAccessException {
        userDatas.clear();
    }
}
