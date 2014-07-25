package org.group3.game.model.game;


import org.group3.game.model.card.Card;

import java.util.List;

public class CardsLog extends GameLog{

    private List<Card> cardsPlayed;
    private int cardsDamage;
    protected Player curPlayer;
    protected Player oppPlayer;
    protected int curTurn;
    protected int districtIndex;

    public CardsLog(){
    	super();
    	
    }

    public CardsLog(List<Card> cardsPlayed, Player curPlayer, Player oppPlayer, int districtIndex, District curDistrict, int cardsDamage)
    {
        super();

        this.cardsDamage = cardsDamage;
        this.cardsPlayed = cardsPlayed;
        this.curPlayer = curPlayer;
        this.oppPlayer = oppPlayer;
        this.curTurn = curDistrict.getTurn();
        this.districtIndex = districtIndex;

        String districtTurnPrefix = "District " + (districtIndex + 1) + " Turn " + curTurn + ": ";

        //Generate message in form of: Player x played y card for z damage
        this.logMessage = districtTurnPrefix + generateMessage();



    }

    private String generateMessage()
    {


        //check if they burned a turn, if so return a message saying so

        if (cardsPlayed.size() == 0)
        {
            return ("Player " + curPlayer.getEmail() + " burned a turn to generate resources.");
        }

        String firstHalfMessage = "Player " + curPlayer.getEmail() + " played";

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

    public Player getCurPlayer() {
        return curPlayer;
    }

    public void setCurPlayer(Player curPlayer) {
        this.curPlayer = curPlayer;
    }

    public Player getOppPlayer() {
        return oppPlayer;
    }

    public void setOppPlayer(Player oppPlayer) {
        this.oppPlayer = oppPlayer;
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
}
