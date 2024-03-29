package org.group3.game.controllers;

import org.group3.game.messageWrappers.RequestUpdateInfoMessage;
import org.group3.game.model.user.User;
import org.group3.game.model.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.apache.commons.validator.routines.EmailValidator;

@Controller
@RequestMapping("/user")
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
    @Autowired
    UserService userService;


    @RequestMapping(value="/loginWithToken",method = RequestMethod.GET)
    public @ResponseBody LoginMessage registerUser(@RequestParam String email,@RequestParam String token) {
        User user = userService.getUserByEmailToken(email, token);


        String message;
        if(user == null){
            message = "There is no user with those credentials";
        }else{
            boolean isValid = userService.assertTokenValid(user);

            if(isValid){
                message = "Successful login";
                userService.updateLastLogin(user);
            }else{
                message="Token has expired, Please log in again.";
                user = null;
            }

        }
        return new LoginMessage(message,user);
    }


	@RequestMapping(value="/register",method = RequestMethod.POST)
	public @ResponseBody LoginMessage registerUser(@RequestParam String name,@RequestParam String email,@RequestParam String password) {

        logger.info("Attempting to create user: " + email);
        
        User newUser;
        
        String resultMessage;
        
        EmailValidator validator = EmailValidator.getInstance();
        
        //Check if any of the params are empty
        if (name == null || name.length() == 0 || email == null || email.length() == 0 || password == null || password.length() == 0)
        {
        	
        	resultMessage = "Error: Please ensure all form fields are filled.";
        	
        	newUser = null;
        	
        }
        
        //Check if email is already registered
        else if (userService.getUserByEmail(email) != null)
        {
        	
        	newUser = null;
        	
        	resultMessage = "Error: Email already registered!";
        	
        }
        
        //Check if email is valid
        else if (!validator.isValid(email))
        {
        	
        	newUser = null;
        	
        	resultMessage = "Error: Please enter a valid email";
        	
        }
        
        //Ok, nothing wrong
        else
        {
        	
        	newUser = userService.registerUser(email, password, name);
        	
        	resultMessage = "Succesfully registered";
        	
        }

		return new LoginMessage(resultMessage, newUser);
	}

    @RequestMapping(value="/login",method = RequestMethod.GET)
    public @ResponseBody LoginMessage getUser(@RequestParam String email,@RequestParam String password) {

        logger.info("Attempt to login for: " + email);
        User user = userService.getUserByEmailPassword(email, password);

        String message;
        if(user == null){
            message = "There is no user with those credentials";
        }else{
            message = "Successful Login";
            userService.updateLastLogin(user);
        }


        return new LoginMessage(message,user);
    }


    @MessageMapping("/updateSendEmailOnTurn")
    public void updateSendEmailOnTurn(RequestUpdateInfoMessage message){
        User user = userService.getUserByEmailToken(message.getUserEmail(),message.getUserToken());
        userService.updateSendEmailOnTurn(user,message.isSendEmailOnTurn());
    }



    public class LoginMessage{
        String message;
        UserWrapper user;

        public LoginMessage(String message, User user) {
            this.message = message;
            this.user = user == null? null : new UserWrapper(user);
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
        private int wins;
        private int losses;
        private boolean isSendEmailOnTurn;

        public UserWrapper(User user) {
            this.name = user.getName();
            this.email = user.getEmail();
            this.token = user.getToken();
            this.tokenExpirationDate = user.getTokenExpirationDate().toString();
            this.isAdmin = user.isAdmin();
            this.wins = user.getWins();
            this.losses = user.getLosses();
            this.isSendEmailOnTurn = user.isSendEmailOnTurn();
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

        public int getWins() {
            return wins;
        }

        public void setWins(int wins) {
            this.wins = wins;
        }

        public int getLosses() {
            return losses;
        }

        public void setLosses(int losses) {
            this.losses = losses;
        }

        public boolean isSendEmailOnTurn() {
            return isSendEmailOnTurn;
        }

        public void setSendEmailOnTurn(boolean isSendEmailOnTurn) {
            this.isSendEmailOnTurn = isSendEmailOnTurn;
        }
    }
}
