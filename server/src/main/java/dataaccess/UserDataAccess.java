package dataaccess;

import model.UserData;

public interface UserDataAccess {
    // TODO: Figure out what return types to use for these
    void createUser(UserData userData) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;
}