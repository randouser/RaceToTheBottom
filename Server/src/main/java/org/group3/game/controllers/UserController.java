package org.group3.game.controllers;

import org.group3.game.model.user.User;
import org.group3.game.model.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
    @Autowired
    UserService userService;


	@RequestMapping(value="/register",method = RequestMethod.POST)
	public @ResponseBody LoginMessage registerUser(@RequestParam String name,@RequestParam String email,@RequestParam String password) {

        logger.info("REGISTER POST!");
        User newUser = userService.registerUser(email,password,name);

		return new LoginMessage("Successful Register",newUser);
	}

    @RequestMapping(value="/login",method = RequestMethod.GET)
    public @ResponseBody LoginMessage getUser(@RequestParam String email,@RequestParam String password) {

        logger.info("LOGIN GET!");
        User user = userService.getUserByEmailPassword(email, password);

        String message;
        if(user == null){
            message = "You are no logged in";
        }else{
            message = "there is no user with those credentials";
        }


        return new LoginMessage(message,user);
    }



    public class LoginMessage{
        String message;
        UserWrapper user;

        public LoginMessage(String message, User user) {
            this.message = message;
            this.user = new UserWrapper(user);
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public UserWrapper getUser() {
            return user;
        }

        public void setUser(UserWrapper user) {
            this.user = user;
        }
    }



    public class UserWrapper{
        private String name;
        private String email;
        private String token;
        private String tokenExpirationDate;
        private boolean isAdmin;

        public UserWrapper(User user) {
            this.name = user.getName();
            this.email = user.getEmail();
            this.token = user.getToken();
            this.tokenExpirationDate = user.getTokenExpirationDate();
            this.isAdmin = user.isAdmin();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getTokenExpirationDate() {
            return tokenExpirationDate;
        }

        public void setTokenExpirationDate(String tokenExpirationDate) {
            this.tokenExpirationDate = tokenExpirationDate;
        }

        public boolean isAdmin() {
            return isAdmin;
        }

        public void setAdmin(boolean isAdmin) {
            this.isAdmin = isAdmin;
        }
    }
}
