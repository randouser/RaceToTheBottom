package org.group3.game.model.game;

import org.group3.game.messageWrappers.*;
import org.group3.game.model.card.Card;
import org.group3.game.model.card.CardService;
import org.group3.game.model.user.User;
import org.group3.game.model.user.UserService;
import org.group3.game.model.game.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.joda.time.DateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService{

    private static ConcurrentHashMap<Integer,Game> activeGames = new ConcurrentHashMap<>();

    @Autowired
    CardService cardService;

    @Autowired
    UserService userService;

    @Autowired
    GameDao gameDao;


    
    public GameMessage createGame(User user, String type, String inviteeEmail, User invitee) {

        //make the decks
        List<Card> deck1 = cardService.getRandomDeck(60);
        List<Card> deck2 = cardService.getRandomDeck(60);

        Integer inviteeId = (invitee == null) ? null:invitee.getId();
        //make the 'players'
        Player playerOne = new Player(user.getId(),deck1,3,3,user.getEmail(),0,Player.BLUE);
        Player playerTwo = new Player(inviteeId,deck2,3,3,inviteeEmail,1,Player.RED);

        //make the districts
        List<District> districts = new ArrayList<>();
        districts.add(new District("Blue home",60,40));
        districts.add(new District("Red home",40,60));
        districts.add(new District("neutral",50,50));
        districts.add(new District("neutral",50,50));
        districts.add(new District("neutral",50,50));

        //create initial game
        Game game = new Game(playerOne,playerTwo,districts,type);

        //save it to the database
        int gameId = gameDao.save(game);
        //set the ID that we generate from the database column.
        game.setGameId(gameId);

        //add to the live list
        activeGames.put(game.getGameId(),game);

        return new GameMessage(game.getGameId(),GameMessage.GAME_START,GameMessage.WAIT_ACCEPT,game.getType(),game.getGameName());
    }

    
    public TurnMessage joinGame(User user, String inviteEmail,int gameId) {
        //first check if the game exists

        Game game = loadGame(gameId);
        Player playerTwo = game.getPendingPlayer();

        //add the AI logic here
        if(game.getType().equals("single") && !game.isInProgress()) {
        	
        	playerTwo.setEmail("Politibot");
        	playerTwo.setId(-1);
        	game.setInProgress(true);
        	Player curPlayer = game.getCurrentPlayer();//
        	User curUser = userService.getUserById(curPlayer.getId());
        	saveGame(game); 
        	return new TurnMessage(curUser,curPlayer,game);
        		
        } else if(!game.isInProgress() && playerTwo.getEmail().equals(inviteEmail)){

            playerTwo.setEmail(user.getEmail());
            playerTwo.setId(user.getId());
            game.setInProgress(true);
            game.updateGameName();
            //TODO random turn start?
            //return message for other player
            Player curPlayer = game.getCurrentPlayer();
            User curUser = userService.getUserById(curPlayer.getId());

            saveGame(game);

            return new TurnMessage(curUser,curPlayer,game);

        }else{
            return null;
        }

    }

    
    public TurnMessage takeTurn(User user, int gameId, List<Card> cardsPlayed, boolean burnTurn,int debateScore,boolean surrender) {

        Game game = loadGame(gameId);

        if(surrender){
            game.surrender(user.getId());
        }
        else if(game.isDebate()){
            game.finishDebate((game.getType().equals("single") && user == null ?-1: user.getId()),debateScore);
        }else{
            //play the cards!
        	game.playCards((game.getType().equals("single") && user == null ?-1: user.getId()),cardsPlayed, burnTurn);

        }

        if(game.getWinnerEmail() != null){
            game.setInProgress(false);
            //update the scores if the game IS NOT a single player game
            if(!game.getType().equals("single")){
                Integer winnerId = game.getWinnerId();
                Integer loserId = game.getLoserId();
                if(winnerId != null && winnerId > 0){
                    userService.incrementWinById(winnerId);
                }
                if(loserId != null && loserId > 0){
                    userService.incrementLossById(loserId);
                }
            }
        }

        //change the turn
        game.toggleTurn();

        //return message for other player
        Player curPlayer = game.getCurrentPlayer();
        User curUser = null;
        if(!curPlayer.getId().equals(-1)) {
        	curUser = userService.getUserById(curPlayer.getId());
        }

        saveGame(game);


        
        
        return new TurnMessage(curUser,curPlayer,game);

    }

    public int getActiveGameCountForUser(User user)
    {
    	
    	List<Game> gameList = gameDao.getSavedGamesForUser(user);
    	
    	return gameList.size();
    	
    }

    /**
     * This returns a smaller version of the turnMessage for game UI update
     * purposes.  Since takeTurn() only returns the turn for the opposite player,
     * this method allows the service to send information back to the player
     * who just took a turn and is waiting (or potentially any player we want).
     */
    public PreTurnMessage getGameStateForPlayer(User user,Integer gameId){
        Game game = loadGame(gameId);
        for(Player player : game.getPlayers()){
            if(user.getId().equals(player.getId())){
                return new PreTurnMessage(user,player,game);
            }
        }
        throw new RuntimeException("Game: " + game.getGameId() + " does not contain user " + user.getEmail());
    }


    
    public LobbyMessage getActiveGamesForUser(User user) {
        List<TurnMessage> games = new ArrayList<>();
        List<JoinGameMessage> invites = new ArrayList<>();

        List<Game> gameList = gameDao.getSavedGamesForUser(user);

        for(Game game : gameList){
            for (Player player : game.getPlayers()) {
                //note, player.getId() can return null if the player is not a user
                if (user.getId().equals(player.getId())) {
                    if(!game.isInProgress() && player.getPlayerIndex()==1){
                        invites.add(new JoinGameMessage(game.getGameId(),player.getEmail()));
                    }else{
                        games.add(new TurnMessage(user, player, game));
                    }

                    break; //onto next game
                }
            }
        }

        return new LobbyMessage(games,invites);
    }


    
    public void deleteGameById(int gameId){
        if(activeGames.containsKey(gameId)){
            activeGames.remove(gameId);
        }

        gameDao.delete(gameId);
    }
    
    public List<Integer> getInactiveGameIds(){
    	
    	DateTime dt = new DateTime();
    	
    	dt = dt.minusWeeks(1);
    	
    	return gameDao.getInactiveGameIdsByDate(dt);
    	
    }
    
    public User getPlayerOneByGame(int gameId)
    {
    	
    	Game game;
    	
    	if (activeGames.containsKey(gameId))
    		game = activeGames.get(gameId);
    	else{
    		
    		game = gameDao.getGameById(gameId);
    		if (game == null){
    			
    			throw new IllegalArgumentException("Couldn't load game " + gameId);
    			
    		}
    		
    	}
    	
    	Player[] players = game.getPlayers();
    	
    	return userService.getUserByEmail(players[0].getEmail());
    	
    }
    
    public User getPlayerTwoByGame(int gameId)
    {
    	
    	Game game;
    	
    	if (activeGames.containsKey(gameId))
    		game = activeGames.get(gameId);
    	else{
    		
    		game = gameDao.getGameById(gameId);
    		if (game == null){
    			
    			throw new IllegalArgumentException("Couldn't load game " + gameId);
    			
    		}
    		
    	}
    	
    	Player[] players = game.getPlayers();
    	
    	return userService.getUserByEmail(players[1].getEmail());
    	
    }
    
    public Game getGameById(int gameId)
    {
    	
    	return loadGame(gameId);
    	
    }


    /**
     * Loads the game from the database or the activeGamesCache.
     * Will place the game in the cache if it's not there.
     *
     */
    private Game loadGame(int id){
        if(activeGames.containsKey(id)){
            return activeGames.get(id);
        }else{
            Game game = gameDao.getGameById(id);
            if(game != null){
                //dat cache
                activeGames.put(game.getGameId(),game);
                return game;
            }else{
                throw new IllegalArgumentException("Attempted to load Game for id '" + id +"' but it does not exist");
            }
        }
    }


    /**
     * Persists the game to the database and ensures the cache is updated.
     */
    private void saveGame(final Game game){
        activeGames.put(game.getGameId(),game);

        //save to disk without blocking
        new Thread(){
            
            public void run(){
                gameDao.update(game);
            }
        }.start();
    }


}
