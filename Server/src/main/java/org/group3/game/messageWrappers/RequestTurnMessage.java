package org.group3.game.messageWrappers;

import org.group3.game.model.card.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alleninteractions on 6/23/14.
 */
public class RequestTurnMessage {

    private String userEmail;
    private String userToken;
    private Integer gameId;
    private List<Card> cardsPlayed; //probably just list of card ids, could include general actions?
    private boolean burnTurn;
    private int debateScore;

    public int getDebateScore() {
        return debateScore;
    }

    public void setDebateScore(int debateScore) {
        this.debateScore = debateScore;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public List<Card> getCardsPlayed() {
        return cardsPlayed;
    }

    public void setCardsPlayed(List<Card> cardsPlayed) {
        this.cardsPlayed = cardsPlayed;
    }
    
    public boolean getBurnTurn()
    {
    	
    	return burnTurn;
    	
    }
    
    public void setBurnTurn(boolean burnTurn)
    {
    	
    	this.burnTurn = burnTurn;
    	
    }
    
}


