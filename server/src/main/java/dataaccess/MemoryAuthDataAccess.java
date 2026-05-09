package dataaccess;

import model.*;

import java.util.HashMap;

// MemoryGameDataAccess holds the hashmap that is used to store all the authentication data on the server with the
// AuthData record type. It implements the UserDataAccess interface so that when database implementation is added, it
// can easily be switched between the two.
public class MemoryAuthDataAccess implements AuthDataAccess {
    // String is the authToken
    final private HashMap<String, AuthData> authDatas = new HashMap<>();

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        authDatas.put(authData.authToken(), authData);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return authDatas.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        authDatas.remove(authToken);
    }

    @Override
    public void clear() throws DataAccessException {
        authDatas.clear();
    }
}
