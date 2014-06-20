package org.group3.game.model.user;


public interface UserService {


    User registerUser(String email, String password, String name);

    User getUser(String email, String password);
}
