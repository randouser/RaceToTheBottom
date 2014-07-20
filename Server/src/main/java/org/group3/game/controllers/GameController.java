package org.group3.game.controllers;

import org.group3.game.messageWrappers.*;
import org.group3.game.model.game.GameService;
import org.group3.game.model.user.User;
import org.group3.game.model.user.UserService;
import org.group3.game.services.EmailService;
import org.group3.game.services.AIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.net.URLDecoder;

@Controller
public class GameController {
	
	private static final Logger logger = LoggerFactory.getLogger(GameController.class);
	
	@Autowired
    UserService userService;

    @Autowired
    GameService gameService;

    @Autowired
    EmailService emailService;
    
    @Autowired
    AIService aiService;



    private final SimpMessagingTemplate messagingTemplate;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    public GameController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }




    @MessageMapping("/startGame")
    public void startGame(StartGameMessage message) throws Exception {
    	
    	//log this for a PvP match only
    	if (message.getEmailToNotify() != "solo") {
    		logger.info("User: " +message.getUserEmail() + " has requested to start a game with: " + message.getEmailToNotify());
    	}
        
        User user = userService.getUserByEmailToken(message.getUserEmail(),message.getUserToken());

        if(user == null){
            throw new IllegalArgumentException("There is no user with these credentials");
        }

        GameMessage gMessage;
        String gameType = message.getGameType();

        //if we are inviting a new player, we send an email invite
        if(gameType.equals("newPlayer")){
            gMessage = gameService.createGame(user,message.getGameType(), message.getEmailToNotify(),null);
            emailService.sendEmailInvite(user, message.getEmailToNotify(), gMessage.getGameId());
        } 
        //if solo match, no email or database actions just get into the game
        else if (gameType.equals("single")){
        	gMessage = gameService.createGame(user,message.getGameType(),null,null);
        	messagingTemplate.convertAndSend("/queue/"+user.getToken()+"/message",gMessage);
        	StartGameMessage aIGMessage = aiService.invite(gMessage);//return a new start game message
        	joinGame(aIGMessage);//needs a slightly different start game message       	
        }
        //if we are inviting a registered player, check database for user
        else{
            User invitee = userService.getUserByEmail(message.getEmailToNotify());
            if(invitee == null) {
                //TODO better error handling, maybe make general messages enums
                messagingTemplate.convertAndSend("/queue/"+user.getToken()+"/message",new ErrorMessage("There is no registered user with email " + message.getEmailToNotify()));
                return;
            }
            gMessage = gameService.createGame(user,message.getGameType(), message.getEmailToNotify(),invitee);
            messagingTemplate.convertAndSend("/queue/" + invitee.getToken() + "/invite", new JoinGameMessage(gMessage.getGameId(), message.getEmailToNotify()));


        }

        //tell the user that the game has been created if not AI
        if(!gameType.equals("single")) {
        	messagingTemplate.convertAndSend("/queue/"+user.getToken()+"/message",gMessage);
        }
        
    }

    @MessageMapping("/joinGame")
    public void joinGame(StartGameMessage message) throws Exception {

        logger.info("User: " +message.getUserEmail() +"has accepted to join the game:" /* +message.getGameId() */);
        logger.info("GameType is: " + message.getGameType());
 
        User user = null;
        if(message.getGameType().equals("single")){
        	user = null;
        } else {
        	user = userService.getUserByEmailToken(message.getUserEmail(),message.getUserToken());
        	if(user == null){
                throw new IllegalArgumentException("There is no user with these credentials");
            }
        }
       
        //join the game
        TurnMessage turnMessage = gameService.joinGame(user,message.getEmailToNotify(),message.getGameId());

        //start the turn for the player waiting
        messagingTemplate.convertAndSend("/queue/"+turnMessage.getUserToken()+"/getTurn",turnMessage);

        //tell the joiner that the game has started if player is not AI
        if(!message.getGameType().equals("single")){
        	messagingTemplate.convertAndSend("/queue/"+user.getToken()+"/message",new GameMessage(turnMessage.getGameId(),GameMessage.GAME_START,turnMessage.getGameName()));
        }
        

    }


    @MessageMapping("/takeTurn")
    public void takeTurn(RequestTurnMessage message) throws Exception {

        User user = userService.getUserByEmailToken(message.getUserEmail(),message.getUserToken());
        if(user == null){
            throw new IllegalArgumentException("There is no user with these credentials");
        }

        //the "message" comes from the UI (when it is the user's turn)
        TurnMessage turnMessage = gameService.takeTurn(user,message.getGameId(),message.getCardsPlayed(), message.getBurnTurn(),message.getDebateScore(),message.isSurrender());

        
        //start the turn for the player
        if(turnMessage.isInProgress()) {
        	//AI player
        	if(turnMessage.getUserToken() == null && turnMessage.getGameType().equals("single")){

                RequestTurnMessage rtMessage = aiService.takeTurn(turnMessage);
        		turnMessage = gameService.takeTurn(null,rtMessage.getGameId(),rtMessage.getCardsPlayed(),rtMessage.getBurnTurn(),rtMessage.getDebateScore(),rtMessage.isSurrender());
            	messagingTemplate.convertAndSend("/queue/"+turnMessage.getUserToken()+"/getTurn",turnMessage);

        	//Non AI player
        	} else {

                messagingTemplate.convertAndSend("/queue/"+turnMessage.getUserToken()+"/getTurn",turnMessage);

        	}
        }else{

            messagingTemplate.convertAndSend("/queue/"+user.getToken()+"/message","gameEnd");
            messagingTemplate.convertAndSend("/queue/"+turnMessage.getUserToken()+"/message","gameEnd");

        }

    }


    @MessageMapping("/getLobbyInfo")
    public void getLobbyInfo(LobbyRequest message) throws Exception {

        User user = userService.getUserByEmailToken(message.getUserEmail(),message.getUserToken());
        if(user == null){
            throw new IllegalArgumentException("There is no user with these credentials");
        }

        //if invitee credentials are passed in, this is a new user registering from an email invite who wants to join a game
        //we assume if the user registered they are implicitly accepting the game invite.
        if(message.getGameId() != null && message.getInviteeEmail() != null){
            String inviteeEmailDecoded = URLDecoder.decode(message.getInviteeEmail(), "UTF-8");
            TurnMessage turnMessage = gameService.joinGame(user, inviteeEmailDecoded,message.getGameId());
            
            //Show list of games and leaderboard to newly registered user accepting game invite
            LobbyMessage gameMessages = gameService.getActiveGamesForUser(user);

            
            //Set leaderboard
            gameMessages.setLeaderBoard(userService.getTopPlayers(5));
            
            //Send leaderboard and game list
            messagingTemplate.convertAndSend("/queue/"+user.getToken()+"/lobby", gameMessages);
            

            //start turn for current player
            messagingTemplate.convertAndSend("/queue/"+turnMessage.getUserToken()+"/getTurn",turnMessage);
            //tell the joiner that the game has started
            messagingTemplate.convertAndSend("/queue/"+user.getToken()+"/message",new GameMessage(turnMessage.getGameId(),GameMessage.GAME_START,turnMessage.getGameName()));

        }
        //else, it's a normal user and we grab existing games
        else{
            LobbyMessage gameMessages =  gameService.getActiveGamesForUser(user);
            
            gameMessages.setLeaderBoard(userService.getTopPlayers(5));
            
            messagingTemplate.convertAndSend("/queue/"+user.getToken()+"/lobby",gameMessages);
        }


        //TODO request leaderboard can go here somewhere

    }


    @SendTo("/topic/greetings")
    public StartGameMessage notifyAllUsers(StartGameMessage message) throws Exception {
        return message;

    }




}
