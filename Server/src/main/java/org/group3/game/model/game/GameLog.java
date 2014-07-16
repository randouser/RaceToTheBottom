package org.group3.game.model.game;


import org.group3.game.model.card.Card;
import org.group3.game.model.game.Player;
import org.group3.game.model.game.District;

import java.util.List;
import java.util.ArrayList;

public class GameLog {
    	
    	private List<Card> cardsPlayed;
    	private String logMessage;
    	private Player curPlayer;
    	private Player oppPlayer;
        private int cardsDamage;
    	private int districtIndex;
    	private int curTurn;

		public GameLog(){}
    	
    	public GameLog(List<Card> cardsPlayed, Player curPlayer, Player oppPlayer, int districtIndex, District curDistrict, int cardsDamage)
    	{
    		
    		this.districtIndex = districtIndex;
    		this.cardsDamage = cardsDamage;
    		this.cardsPlayed = cardsPlayed;
    		this.curPlayer = curPlayer;
    		this.oppPlayer = oppPlayer;
    		this.districtIndex = districtIndex;
    		this.curTurn = curDistrict.getTurn();
    		
    		String districtTurnPrefix = "District " + (districtIndex + 1) + " Turn " + curTurn + ": ";
    		
    		//Generate message in form of: Player x played y card for z damage
    		this.logMessage =districtTurnPrefix + generateMessage();
    		
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

		public String getLogMessage() {
			return logMessage;
		}

		public void setLogMessage(String logMessage) {
			this.logMessage = logMessage;
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

		public int getCardsDamage() {
			return cardsDamage;
		}

		public void setCardsDamage(int cardsDamage) {
			this.cardsDamage = cardsDamage;
		}

		public int getDistrictIndex() {
			return districtIndex;
		}

		public void setDistrictIndex(int districtIndex) {
			this.districtIndex = districtIndex;
		}

		public int getCurTurn() {
			return curTurn;
		}

		public void setCurTurn(int curTurn) {
			this.curTurn = curTurn;
		}


}
