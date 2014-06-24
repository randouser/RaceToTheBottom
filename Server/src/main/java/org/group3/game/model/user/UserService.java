package org.group3.game.model.user;


public interface UserService {


    User registerUser(String email, String password, String name);

    User getUserByEmailPassword(String email, String password);

    User getUserByEmailToken(String email, String userToken);

    User getUserByEmail(String email);
}
