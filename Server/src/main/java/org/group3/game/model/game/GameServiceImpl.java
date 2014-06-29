package org.group3.game.model.game;

import org.group3.game.messageWrappers.GameMessage;
import org.group3.game.messageWrappers.JoinGameMessage;
import org.group3.game.messageWrappers.LobbyMessage;
import org.group3.game.messageWrappers.TurnMessage;
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

    //TODO better data structure?
    private static ConcurrentHashMap<Integer,Game> activeGames = new ConcurrentHashMap<>();
    private static int gameCount = 0;

    @Autowired
    CardService cardService;

    @Autowired
    UserService userService;


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


        Game game = new Game(getGameCount(),playerOne,playerTwo,districts,type);

        activeGames.put(game.getGameId(),game);

        return new GameMessage(game.getGameId(),GameMessage.GAME_START,game.getGameName());
    }

    @Override
    public TurnMessage joinGame(User user, String inviteEmail,int gameId) {
        //first check if the game exists
        if(activeGames.containsKey(gameId)){
            Game game = activeGames.get(gameId);
            Player playerTwo = game.getPendingPlayer();

            //check if this game has started

            if(!game.isInProgress()
               && playerTwo.getEmail().equals(inviteEmail)){

                playerTwo.setEmail(user.getEmail());
                playerTwo.setId(user.getId());
                game.setInProgress(true);

                //TODO random turn start?
                //return message for other player
                Player curPlayer = game.getCurrentPlayer();
                User curUser = userService.getUserById(curPlayer.getId());
                return new TurnMessage(curUser,curPlayer,game);

            }else{
                return null;
            }

        }else{
            return null;
        }


    }

    @Override
    public TurnMessage takeTurn(User user, int gameId, List<Card> cardsPlayed, boolean burnTurn) {
        if(activeGames.containsKey(gameId)){
            Game game = activeGames.get(gameId);

            //play the cards!
            boolean success = game.playCards(user.getEmail(),cardsPlayed, burnTurn);

            if(success){
                if(game.getWinnerEmail() != null){
                    game.setInProgress(false);
                }

                //change the turn
                game.toggleTurn();

                //return message for other player
                Player curPlayer = game.getCurrentPlayer();
                User curUser = userService.getUserById(curPlayer.getId());
                return new TurnMessage(curUser,curPlayer,game);

            }else{
                return null; //what happens when you try to play cards that you can't
            }

        }else{
            return null; //you get nothing if you ask for a gameId that doesn't exist
        }
    }


    @Override
    public LobbyMessage getActiveGamesForUser(User user) {
        List<TurnMessage> games = new ArrayList<>();
        List<JoinGameMessage> invites = new ArrayList<>();

        for(Game game : activeGames.values()){
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


    private synchronized int getGameCount() {
        return gameCount++;
    }
}
