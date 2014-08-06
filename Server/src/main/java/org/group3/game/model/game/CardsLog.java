package org.group3.game.model.game;


import org.group3.game.model.card.Card;

import java.util.List;

public class CardsLog extends GameLog{

    private List<Card> cardsPlayed;
    private int cardsDamage;
    protected Integer curPlayerId;
    protected Integer oppPlayerId;
    protected String curPlayerName;
    protected String oppPlayerName;
    protected int curTurn;
    protected int districtIndex;
    private boolean isCardsLog;

    public CardsLog(){
    	super();
    	
    }

    public CardsLog(List<Card> cardsPlayed, Player curPlayer, Player oppPlayer, int districtIndex, District curDistrict, int cardsDamage)
    {
        super();

        this.isCardsLog = true;
        this.cardsDamage = cardsDamage;
        this.cardsPlayed = cardsPlayed;
        this.curPlayerId = curPlayer.getId();
        this.oppPlayerId = oppPlayer.getId();
        this.curTurn = curDistrict.getTurn();
        this.districtIndex = districtIndex;
        this.curPlayerName = curPlayer.getEmail();
        this.oppPlayerName = oppPlayer.getEmail();

        String districtTurnPrefix = "District " + (districtIndex + 1) + " Turn " + curTurn + ": ";

        //Generate message in form of: Player x played y card for z damage
        this.logMessage = districtTurnPrefix + generateMessage();



    }

    private String generateMessage()
    {


        //check if they burned a turn, if so return a message saying so

        if (cardsPlayed.size() == 0)
        {
            return ("Player " + curPlayerName + " burned a turn to generate resources.");
        }

        String firstHalfMessage = "Player " + oppPlayerName + " played";

        String posCardsPlayed = " positive card(s)";

        String negCardsPlayed = " negative card(s)";

        int negCardCount, posCardCount;

        negCardCount = 0;
        posCardCount = 0;

        //Create two strings listing pos cards and neg cards
        for(Card curCard : cardsPlayed)
        {

            if (curCard.getType().equals("positive"))
            {
                posCardsPlayed += (" " + curCard.getName() + ",");

                posCardCount++;

            }
            else
            {
                negCardsPlayed += (" " + curCard.getName() + ",");

                negCardCount++;

            }

        }

        //Concat, return
        String returnStr;

        if (posCardCount > 0 && negCardCount > 0)
        {

            returnStr = firstHalfMessage + posCardsPlayed  + negCardsPlayed + " for " + cardsDamage + " damage.";

        }
        else if (posCardCount > 0)
        {

            returnStr = firstHalfMessage + posCardsPlayed + " for " + cardsDamage + " damage.";

        }
        else
        {
            returnStr = firstHalfMessage + negCardsPlayed + " for " + cardsDamage + " damage.";
        }

        return returnStr;

    }


    public List<Card> getCardsPlayed() {
        return cardsPlayed;
    }

    public void setCardsPlayed(List<Card> cardsPlayed) {
        this.cardsPlayed = cardsPlayed;
    }

    public int getCardsDamage() {
        return cardsDamage;
    }

    public void setCardsDamage(int cardsDamage) {
        this.cardsDamage = cardsDamage;
    }

    public int getCurTurn() {
        return curTurn;
    }

    public void setCurTurn(int curTurn) {
        this.curTurn = curTurn;
    }

    public int getDistrictIndex() {
        return districtIndex;
    }

    public void setDistrictIndex(int districtIndex) {
        this.districtIndex = districtIndex;
    }

    public Integer getCurPlayerId() {
        return curPlayerId;
    }

    public void setCurPlayerId(Integer curPlayerId) {
        this.curPlayerId = curPlayerId;
    }

    public Integer getOppPlayerId() {
        return oppPlayerId;
    }

    public void setOppPlayerId(Integer oppPlayerId) {
        this.oppPlayerId = oppPlayerId;
    }

    public String getCurPlayerName() {
        return curPlayerName;
    }

    public void setCurPlayerName(String curPlayerName) {
        this.curPlayerName = curPlayerName;
    }

    public String getOppPlayerName() {
        return oppPlayerName;
    }

    public void setOppPlayerName(String oppPlayerName) {
        this.oppPlayerName = oppPlayerName;
    }

    public boolean isCardsLog() {
        return isCardsLog;
    }

    public void setCardsLog(boolean isCardsLog) {
        this.isCardsLog = isCardsLog;
    }
}
