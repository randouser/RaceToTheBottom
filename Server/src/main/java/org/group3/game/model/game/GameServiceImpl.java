package org.group3.game.model.game;

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
    public int createGame(User user, String type, String inviteeEmail) {
        //TODO we ignore type for now for the most part

        //make the decks
        List<Card> deck1 = cardService.getRandomDeck(60);
        List<Card> deck2 = cardService.getRandomDeck(60);

        //make the 'players'
        Player playerOne = new Player(deck1,5,5,user.getEmail());
        Player playerTwo = new Player(deck2,5,5,inviteeEmail);

        //make the districts
        List<District> districts = new ArrayList<>();
        for(int i = 0; i < 5; ++i){
            districts.add(new District("neutral",50,50));
        }


        Game game = new Game(getGameCount(),playerOne,playerTwo,districts,type);

        activeGames.put(game.getGameId(),game);

        return game.getGameId();
    }

    @Override
    public boolean joinGame(User user, int gameId) {
        if(activeGames.contains(gameId)){
            Game game = activeGames.get(gameId);
            Player playerTwo = game.getPendingPlayer();
            if(playerTwo.getEmail().equals(user.getEmail())
                && !game.isInProgress()){
                //TODO probably need a way to update email in case new user uses a different email
                game.setInProgress(true);
                return true; //TODO this needs to start the first turn, not return true

            }else{
                return false;
            }

        }else{
            return false;
        }


    }

    @Override
    public TurnMessage takeTurn(User user, int gameId, List<Card> cardsPlayed) {
        if(activeGames.contains(gameId)){
            Game game = activeGames.get(gameId);

            //play the cards!
            boolean success = game.playCards(user.getEmail(),cardsPlayed);

            if(success){
                //change the turn
                game.toggleTurn();

                //return message for other player
                Player curPlayer = game.getCurrentPlayer();
                User curUser = userService.getUserByEmail(curPlayer.getEmail());
                return new TurnMessage(curUser.getToken(),curPlayer.getHand(),curPlayer.getMaxWorkers(),curPlayer.getMaxMoney(),game.getDistricts());

            }else{
                return null; //what happens when you try to play cards that you can'ts
            }

        }else{
            return null; //you get nothing if you ask for a gameId that doesn't exist
        }
    }


    private synchronized int getGameCount() {
        return gameCount++;
    }
}
