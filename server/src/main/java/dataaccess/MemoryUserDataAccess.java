package dataaccess;

import model.*;

import java.util.HashMap;

// MemoryGameDataAccess holds the hashmap that is used to store all the user data on the server with the UserData
// record type. It implements the UserDataAccess interface so that when database implementation is added, it can easily
// be switched between the two.
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
