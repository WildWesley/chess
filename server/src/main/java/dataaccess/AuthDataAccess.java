package dataaccess;

import model.*;

public interface AuthDataAccess {
    // TODO: Figure out what return types to use for these
    void createAuth(AuthData authData) throws DataAccessException;

    AuthData getAuth(String authToken) throws DataAccessException;

    void deleteAuth(String authToken) throws DataAccessException;
}