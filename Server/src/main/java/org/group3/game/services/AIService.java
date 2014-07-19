package org.group3.game.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.group3.game.model.card.Card;
import org.group3.game.messageWrappers.GameMessage;
import org.group3.game.messageWrappers.RequestTurnMessage;
import org.group3.game.messageWrappers.StartGameMessage;
import org.group3.game.messageWrappers.TurnMessage;
import org.springframework.stereotype.Service;

@Service
public class AIService {
   
	public StartGameMessage invite(GameMessage message){
		
		StartGameMessage sMessage = new StartGameMessage();
		sMessage.setGameId(message.getGameId());
		sMessage.setGameType("single");
		return sMessage;
		
	}
	
	public RequestTurnMessage takeTurn(TurnMessage message) {
		
		//TODO AI Logic:
		
		Random generator = new Random();
		int aIDebateScore = generator.nextInt(50);
		
		RequestTurnMessage rTMessage = new RequestTurnMessage();
		rTMessage.setGameId(message.getGameId());
		
		//playable cards
		List<Card> playables = new ArrayList<>();
		//add cards that can be played to list of playables
		for(int i = 0; i < message.getHand().size(); i++) {
			if(message.getHand().get(i).getMoneyCost() <= message.getMaxMoney() && message.getHand().get(i).getWorkerCost() <= message.getMaxWorkers()) {
				playables.add(message.getHand().get(i));
				message.setMaxWorkers(message.getMaxWorkers()-message.getHand().get(i).getWorkerCost());
				message.setMaxMoney(message.getMaxMoney()-message.getHand().get(i).getMoneyCost());
			}
		}
		
		if(message.isDebate()){
			rTMessage.setCardsPlayed(playables);
			rTMessage.setDebateScore(aIDebateScore);
		} else if(!message.isDebate() && playables.isEmpty()) {
			rTMessage.setCardsPlayed(playables);
			rTMessage.setBurnTurn(true);
		} else {
			rTMessage.setCardsPlayed(playables);
		}
		
		return rTMessage;
		
	}
	
}
