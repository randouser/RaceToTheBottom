package org.group3.game.model.user;


import java.sql.Timestamp;

public interface UserDao {


    void registerUser(String email,String password,String name, String salt, String token, String passwordHash, int wins, int losses, Timestamp tokenExpirationDate);
    User getUserByEmailToken(String email,String token);

    User getUserByEmail(String email);
}
