package org.group3.game.controllers;

import org.group3.game.messageWrappers.JoinGameMessage;
import org.group3.game.messageWrappers.RequestTurnMessage;
import org.group3.game.messageWrappers.StartGameMessage;
import org.group3.game.messageWrappers.TurnMessage;
import org.group3.game.model.game.GameService;
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

    @Autowired
    GameService gameService;



    private final SimpMessagingTemplate messagingTemplate;

    @SuppressWarnings("SpringJavaAutowiringInspection")
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

        //make the game
        Integer id = gameService.createGame(user,message.getGameType(), message.getEmailToNotify());


        //TODO if user, notify, check if online?  If not user, email
        if(invitee != null){
            messagingTemplate.convertAndSend("/queue/"+invitee.getToken()+"/invite", new JoinGameMessage(id,message.getEmailToNotify()));
        }

        messagingTemplate.convertAndSend("/queue/"+user.getToken()+"/message","You started a game with id:" + id);


    }

    @MessageMapping("/joinGame")
    public void joinGame(StartGameMessage message) throws Exception {

        logger.info("User: " +message.getUserEmail() +"has accepted to join the game:" /* +message.getGameId() */);

        User user = userService.getUserByEmailToken(message.getUserEmail(),message.getUserToken());
        if(user == null){
            throw new IllegalArgumentException("There is no user with these credentials");
        }

        //join the game
        TurnMessage turnMessage = gameService.joinGame(user,message.getEmailToNotify(),message.getGameId());

        //start the turn for the player waiting
        messagingTemplate.convertAndSend("/queue/"+turnMessage.getUserToken()+"/getTurn",turnMessage);

        //tell the joiner that the game has started
        messagingTemplate.convertAndSend("/queue/"+user.getToken()+"/message","gameStart");

    }


    @MessageMapping("/takeTurn")
    public void takeTurn(RequestTurnMessage message) throws Exception {

        User user = userService.getUserByEmailToken(message.getUserEmail(),message.getUserToken());
        if(user == null){
            throw new IllegalArgumentException("There is no user with these credentials");
        }


        TurnMessage turnMessage = gameService.takeTurn(user,message.getGameId(),message.getCardsPlayed());

        //start the turn for the player other player
        if(turnMessage.isInProgress()) {
            messagingTemplate.convertAndSend("/queue/"+turnMessage.getUserToken()+"/getTurn",turnMessage);
        }else{
            messagingTemplate.convertAndSend("/queue/"+user.getToken()+"/message","gameEnd");
            messagingTemplate.convertAndSend("/queue/"+turnMessage.getUserToken()+"/message","gameEnd");
        }

    }






    @SendTo("/topic/greetings")
    public StartGameMessage notifyAllUsers(StartGameMessage message) throws Exception {
        return message;

    }




}
