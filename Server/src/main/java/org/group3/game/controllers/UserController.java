package org.group3.game.controllers;

import org.group3.game.model.user.User;
import org.group3.game.model.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
    @Autowired
    UserService userService;


	@RequestMapping(value="/register",method = RequestMethod.POST)
	public @ResponseBody UserWrapper registerUser(@RequestParam String name,@RequestParam String email,@RequestParam String password) {

        logger.info("REGISTER POST!");
        User newUser = userService.registerUser(email,password,name);

		return new UserWrapper(newUser);
	}




    public class RegisterParams implements Serializable {
        private String email;
        private String password;
        private String name;

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }


    private class UserWrapper{
        private String name;
        private String email;
        private String token;
        private String tokenExpirationDate;

        private UserWrapper(User user) {
            this.name = user.getName();
            this.email = user.getEmail();
            this.token = user.getToken();
            this.tokenExpirationDate = user.getTokenExpirationDate();
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
    }
}