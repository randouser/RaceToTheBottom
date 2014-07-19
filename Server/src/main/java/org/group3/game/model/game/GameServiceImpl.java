package org.group3.game.model.game;

import org.group3.game.messageWrappers.GameMessage;
import org.group3.game.messageWrappers.JoinGameMessage;
import org.group3.game.messageWrappers.LobbyMessage;
import org.group3.game.messageWrappers.TurnMessage;
import org.group3.game.messageWrappers.LogMessage;
import org.group3.game.model.card.Card;
import org.group3.game.model.card.CardService;
import org.group3.game.model.user.User;
import org.group3.game.model.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameServiceImpl implements GameService{

    private static ConcurrentHashMap<Integer,Game> activeGames = new ConcurrentHashMap<>();

    @Autowired
    CardService cardService;

    @Autowired
    UserService userService;

    @Autowired
    GameDao gameDao;


    @Override
    public GameMessage createGame(User user, String type, String inviteeEmail, User invitee) {
        //TODO we ignore type for now for the most part

        //make the decks
        List<Card> deck1 = cardService.getRandomDeck(60);
        List<Card> deck2 = cardService.getRandomDeck(60);

        Integer inviteeId = (invitee == null) ? null:invitee.getId();
        //make the 'players'
        Player playerOne = new Player(user.getId(),deck1,3,3,user.getEmail(),0);
        Player playerTwo = new Player(inviteeId,deck2,3,3,inviteeEmail,1);

        //make the districts
        List<District> districts = new ArrayList<>();
        for(int i = 0; i < 5; ++i){
            districts.add(new District("neutral",50,50));
        }

        //create initial game
        Game game = new Game(playerOne,playerTwo,districts,type);

        //save it to the database
        int gameId = gameDao.save(game);
        //set the ID that we generate from the database column.
        game.setGameId(gameId);

        //add to the live list
        activeGames.put(game.getGameId(),game);

        return new GameMessage(game.getGameId(),GameMessage.GAME_START,game.getGameName());
    }

    @Override
    public TurnMessage joinGame(User user, String inviteEmail,int gameId) {
        //first check if the game exists

        Game game = loadGame(gameId);
        Player playerTwo = game.getPendingPlayer();

        //add the AI logic here
        if(game.getType().equals("single") && !game.isInProgress()) {
        	
        	playerTwo.setEmail(null);
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

    @Override
    public TurnMessage takeTurn(User user, int gameId, List<Card> cardsPlayed, boolean burnTurn,int debateScore,boolean surrender) {

        Game game = loadGame(gameId);

        if(surrender){
            game.surrender(user.getId());
        }
        else if(game.isDebate()){
            game.finishDebate(user.getId(),debateScore);
        }else{
            //play the cards!
        	game.playCards((game.getType().equals("single") && user == null ?-1: user.getId()),cardsPlayed, burnTurn);

            if(game.getWinnerEmail() != null){
                game.setInProgress(false);
            }

        }

        //change the turn
        game.toggleTurn();

        //return message for other player
        Player curPlayer = game.getCurrentPlayer();
        User curUser = null;
        if(!game.getType().equals("single")) {
        	curUser = userService.getUserById(curPlayer.getId());
        }

        saveGame(game);
        
        
        return new TurnMessage(curUser,curPlayer,game);

    }


    @Override
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
    
    @Override
    public LogMessage getLastTurnLog(int gameId)
    {
    
    	Game game = loadGame(gameId);
    	
    	GameLog lastLogEntry = game.getLog().get(game.getLog().size() - 1);
    	
    	LogMessage logMessage = new LogMessage(lastLogEntry.getCardsPlayed(), lastLogEntry.getCardsDamage(), lastLogEntry.getLogMessage());
    	
    	return logMessage;
    	
    }


    @Override
    public void deleteGameById(int gameId){
        if(activeGames.containsKey(gameId)){
            activeGames.remove(gameId);
        }

        gameDao.delete(gameId);
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
            @Override
            public void run(){
                gameDao.update(game);
            }
        }.start();
    }


}
