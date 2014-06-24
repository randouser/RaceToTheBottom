package org.group3.game.controllers;

import org.group3.game.messageWrappers.StartGameMessage;
import org.group3.game.model.user.User;
import org.group3.game.model.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {
	
	private static final Logger logger = LoggerFactory.getLogger(GameController.class);
	
	@Autowired
    UserService userService;

      //goes here eventually
//    @Autowired
//    GameService gameService;



    private final SimpMessagingTemplate messagingTemplate;
    @Autowired
    public GameController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }




    @MessageMapping("/startGame")
    public void startGame(StartGameMessage message) throws Exception {

        logger.info("User: " +message.getUserEmail() + " has requested to start a game with: " + message.getEmailToNotify());

        User user = userService.getUserByEmailToken(message.getUserEmail(),message.getUserToken());
        User invitee = userService.getUserByEmail(message.getEmailToNotify());

        if(user == null){
            throw new IllegalArgumentException("There is no user with these credentials");
        }


        //gameService.createGame(user,message.getGameType(), message.getEmailToNotify);
        messagingTemplate.convertAndSend("/queue/"+invitee.getToken()+"/invite","Hello from" + user.getEmail());


    }

    @MessageMapping("/joinGame")
    public void joinGame(StartGameMessage message) throws Exception {

        logger.info("User: " +message.getUserEmail() +"has accepted to join the game:" /* +message.getGameId() */);

        User user = userService.getUserByEmailToken(message.getUserEmail(),message.getUserToken());
        if(user == null){
            throw new IllegalArgumentException("There is no user with these credentials");
        }

        //gameService.joinGame(user,message.getGameId());



    }


    @MessageMapping("/takeTurn")
    public void takeTurn(StartGameMessage message) throws Exception {

        User user = userService.getUserByEmailToken(message.getUserEmail(),message.getUserToken());
        if(user == null){
            throw new IllegalArgumentException("There is no user with these credentials");
        }

        //note:this should probably be a different message
        //gameService.takeTurn(user,message);
    }




    public void sendToUser(User user,StartGameMessage message) throws Exception {

        messagingTemplate.convertAndSend("/queue/"+user.getToken(),message);


    }



    @SendTo("/topic/greetings")
    public StartGameMessage notifyAllUsers(StartGameMessage message) throws Exception {
        return message;

    }




}
