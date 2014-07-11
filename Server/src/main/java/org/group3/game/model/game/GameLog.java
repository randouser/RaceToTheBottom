package org.group3.game.model.game;


import org.group3.game.model.card.Card;
import org.group3.game.model.game.Player;
import org.group3.game.model.game.District;

import java.util.List;
import java.util.ArrayList;

public class GameLog {
	
	/*	Note: The ObjectMapper throws an error when stringifying game to store in db
	 *  when gamelog was implemented b/c the string gets too big. In order to cut down on each GameLog's size, I
	 *  pass the two Player objects, the District object, and the List<Card> cardsPlayed
	 *  object directly to the methods that generate the message/effect strings instead of
	 *  storing them as fields in the gamelog instance. Testing to see if it reduces
	 *  the size enough so stringifying doesn't throw an error.
	 */
    	
    //	private List<Card> cardsPlayed;
    	private String message;
        private String effect;
   //   private Player curPlayer;
   //   private Player oppPlayer;
        private int cardsDamage;
   // 	private District curDistrict;
    	private int districtIndex;

		public GameLog(){}
    	
    	public GameLog(List<Card> cardsPlayed, Player curPlayer, Player oppPlayer, int districtIndex, District curDistrict, int cardsDamage)
    	{
    		
    		this.districtIndex = districtIndex;
    		this.cardsDamage = cardsDamage;
    		
    		String districtTurnPrefix = "District " + (districtIndex + 1) + " Turn " + curDistrict.getTurn() + ": ";
    		
    		//Generate message in form of: Player x played y card for z damage
    		message =districtTurnPrefix + generateMessage(curPlayer, oppPlayer, cardsPlayed);
    		
    		//Generate effect string in form of: Player x score +y: z \n Player a score -y: c
    		effect =districtTurnPrefix + generateEffect(curDistrict, curPlayer, oppPlayer);
    		
    	}
    	
    	private String generateMessage(Player curPlayer, Player oppPlayer, List<Card> cardsPlayed)
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

    	
    	private String generateEffect(District curDistrict, Player curPlayer, Player oppPlayer)
    	{
    		
    		//Figure out if curPlayer is Player 1 or Player 2
    		int playerIndex = curDistrict.getTurn() % 2 == 0 ? 0 : 1;
    		
    		String curPlayerScore, oppPlayerScore;
    		
    		
    		if (playerIndex == 0)
    		{
    			//curPlayer = Player 1
    			curPlayerScore = "Player " + curPlayer.getEmail() + " +" + cardsDamage + '%' + " :" + " Standing = " + curDistrict.getPlayerOneScore() + "%\n";
    			oppPlayerScore = "Player " + oppPlayer.getEmail() + " -" + cardsDamage + "%" + " :" + " Standing = " + curDistrict.getPlayerTwoScore() + "%"; 
    			
    		}
    		else
    		{
    			//curPlayer = Player 2
    			curPlayerScore = "Player " + curPlayer.getEmail() + " +" + cardsDamage + '%' + " :" + " Standing = " + curDistrict.getPlayerTwoScore() + "%\n";
    			oppPlayerScore = "Player " + oppPlayer.getEmail() + " -" + cardsDamage + "%" + " :" + " Standing = " + curDistrict.getPlayerOneScore() + "%"; 
    			
    		}
    		
    		return curPlayerScore + oppPlayerScore;
    		
    	}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getEffect() {
			return effect;
		}

		public void setEffect(String effect) {
			this.effect = effect;
		}
		
    	public int getDistrictIndex() {
			return districtIndex;
		}

		public void setDistrictIndex(int districtIndex) {
			this.districtIndex = districtIndex;
		}

		public int getCardsDamage() {
			return cardsDamage;
		}

		public void setCardsDamage(int cardsDamage) {
			this.cardsDamage = cardsDamage;
		}


}
