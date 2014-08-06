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
import org.apache.commons.validator.routines.EmailValidator;
import java.net.URLDecoder;

@Controller
public class GameController {
	
	private static final Logger logger = LoggerFactory.getLogger(GameController.class);
	
	private static final int MAX_ACTIVE_GAMES = 10;
	
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




    @MessageMapping("/startGameWrapper")
    public void startGameWrapper(StartGameMessage message) {

    	try {
    		
    		startGame(message);
    		
    	}
    	catch(IllegalArgumentException ex)
    	{
    		
    		messagingTemplate.convertAndSend("/queue/"+message.getUserToken()+"/message", new ErrorMessage(ex.getMessage()));
    		return;
    		
    	}
    	catch(Exception ex)
    	{
    		
    		logger.info("Exception when creating game: " + ex.getMessage());
    		
    		return;
    		
    	}
       
        
    }
    
    public void startGame(StartGameMessage message) throws IllegalArgumentException, Exception  {
    	
    	 logger.info("User: " +message.getUserEmail() + " has requested to start a game with: " + message.getEmailToNotify());

         
         User user = userService.getUserByEmailToken(message.getUserEmail(),message.getUserToken());

         if(user == null){
             throw new Exception("There is no user with these credentials");
         }

         //Check if inviter has max active games
         if (gameService.getActiveGameCountForUser(user) >= MAX_ACTIVE_GAMES)
         {
        	 
        	 throw new IllegalArgumentException("You have too many active games. Finish some games and try again.");
        	 
         }
         
    	 //Check if some smart alec tried to invite themselves
    	 if (message.getEmailToNotify().equals(message.getUserEmail()))
    	 {
    		 
    		 throw new IllegalArgumentException("Choose \"Single Player\" to play the AI.");
    		 
    	 }

         GameMessage gMessage;
         String gameType = message.getGameType();

         //if we are inviting a new player, we send an email invite
         if(gameType.equals("newPlayer")){
        	 
        	 //Check if invitee is valid email
        	 if (!verifyEmail(message.getEmailToNotify()))
        	 {
        		 
        		 throw new IllegalArgumentException(message.getEmailToNotify() + " is not a valid email address.");
        		 
        	 }
        	 
        	 //Check if invitee is already registerd
        	 if (userService.getUserByEmail(message.getEmailToNotify()) != null)
        	 {
        		 
        		 throw new IllegalArgumentException(message.getEmailToNotify() + " is already registered. Invite using \"Invite Player\".");
        		 
        	 }
        	 
        	 
             gMessage = gameService.createGame(user,message.getGameType(), message.getEmailToNotify(),null);
             emailService.sendEmailInvite(user, message.getEmailToNotify(), gMessage.getGameId());
         } 
         //if solo match, no email or database actions just get into the game
         else if (gameType.equals("single")){
         	gMessage = gameService.createGame(user,message.getGameType(),"Politibot",null);
         	messagingTemplate.convertAndSend("/queue/"+user.getToken()+"/message",gMessage);
         	StartGameMessage aIGMessage = aiService.invite(gMessage);//return a new start game message
         	joinGame(aIGMessage);//needs a slightly different start game message       	
         }
         //if we are inviting a registered player, check database for user
         else{
             User invitee = userService.getUserByEmail(message.getEmailToNotify());
             
             //Check if email is valid
        	 if (!verifyEmail(message.getEmailToNotify()))
        	 {
        		 
        		 throw new IllegalArgumentException(message.getEmailToNotify() + " is not a valid email address.");
        		 
        	 }
             
             //If they aren't registered, tell them to invite new player.
             if(invitee == null) {
                 
            	 throw new IllegalArgumentException("Error: " + message.getEmailToNotify() + " is not registered. Choose invite new player.");
             }
             
             //check if invitee is at active games cap
             if (gameService.getActiveGameCountForUser(invitee) >= MAX_ACTIVE_GAMES)
             {
            	 
            	 throw new IllegalArgumentException(message.getEmailToNotify() + " has too many active games. Try again later.");
            	 
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
    
    @MessageMapping("/rejectInvite")
    public void rejectInvite(StartGameMessage message) throws Exception {
    	
    	logger.info("User: " + message.getUserEmail() + " has rejected invite to " + message.getGameId());
    	logger.info("Game is " + message.getGameType());
    	
    	User user = null;
    	
    	if (message.getGameType().equals("single")){
    		user = null;
    	} else {
    		
    		user = userService.getUserByEmailToken(message.getUserEmail(), message.getUserToken());
    		
    		if (user == null)
    			{
    			
    				throw new IllegalArgumentException("There is no user with these credentials");
    			
    			}
    		
    	}
    	
    	User playerOne = gameService.getPlayerOneByGame(message.getGameId());
    	
    	gameService.deleteGameById(message.getGameId());
    	
    	messagingTemplate.convertAndSend("/queue/"+playerOne.getToken()+"/message", new GameMessage(message.getGameId(), GameMessage.GAME_REJECT, "User " + message.getUserEmail() + " rejected game invite."));
    	
    }


    @MessageMapping("/takeTurn")
    public void takeTurn(RequestTurnMessage message) throws Exception {

        User user = userService.getUserByEmailToken(message.getUserEmail(),message.getUserToken());
        if(user == null){
            throw new IllegalArgumentException("There is no user with these credentials");
        }

        //the "message" comes from the UI (when it is the user's turn)
        TurnMessage turnMessage = gameService.takeTurn(user,message.getGameId(),message.getCardsPlayed(), message.getBurnTurn(),message.getDebateScore(),message.isSurrender());

        
        //start the turn for the player if it's in progress
        if(turnMessage.isInProgress()) {
        	//AI player
        	if(turnMessage.getUserToken() == null && turnMessage.getGameType().equals("single")){

                RequestTurnMessage rtMessage = aiService.takeTurn(turnMessage);
        		turnMessage = gameService.takeTurn(null,rtMessage.getGameId(),rtMessage.getCardsPlayed(),rtMessage.getBurnTurn(),rtMessage.getDebateScore(),rtMessage.isSurrender());

                if(turnMessage.isInProgress()){
                    messagingTemplate.convertAndSend("/queue/"+turnMessage.getUserToken()+"/getTurn",turnMessage);
                }else{
                    endGame(null, turnMessage);
                }

        	//Non AI player
        	} else {
                messagingTemplate.convertAndSend("/queue/"+turnMessage.getUserToken()+"/getTurn",turnMessage);
        	}

        //end the game if it's not in progress
        }else{
            endGame(user, turnMessage);
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

    public void endGame(User curUser,TurnMessage turnMessage){
    	
        LobbyMessage updateLeaderBoard = new LobbyMessage();
        
        updateLeaderBoard.setLeaderBoard(userService.getTopPlayers(5));
        
        messagingTemplate.convertAndSend("/queue/updateLeaderboard", updateLeaderBoard);

        EndGameMessage endMessage = new EndGameMessage(turnMessage);

        if(curUser != null){
            messagingTemplate.convertAndSend("/queue/"+curUser.getToken()+"/message",endMessage);
        }
        if(turnMessage.getUserToken() != null){
            messagingTemplate.convertAndSend("/queue/"+turnMessage.getUserToken()+"/message",endMessage);
        }

        gameService.deleteGameById(turnMessage.getGameId());


    }
    
    private boolean verifyEmail(String email)
    {
    	
    	EmailValidator validator = EmailValidator.getInstance();
    	
    	return validator.isValid(email);
    	
    }


    @SendTo("/topic/greetings")
    public StartGameMessage notifyAllUsers(StartGameMessage message) throws Exception {
        return message;

    }




}
