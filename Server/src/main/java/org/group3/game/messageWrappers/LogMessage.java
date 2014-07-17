package org.group3.game.messageWrappers;

import org.group3.game.model.card.Card;
import org.group3.game.model.game.District;
import org.group3.game.model.game.Game;
import org.group3.game.model.game.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LogMessage {
	
	private List<Card> cardsPlayed;
	private int totalDamage;
	private String strLogMessage;
	
	public LogMessage(List<Card> cardsPlayed, int totalDamage, String strLogMessage)
	{
		
		this.cardsPlayed = cardsPlayed;
		this.totalDamage = totalDamage;
		this.strLogMessage = strLogMessage;
		
	}

	public List<Card> getCardsPlayed() {
		return cardsPlayed;
	}

	public void setCardsPlayed(List<Card> cardsPlayed) {
		this.cardsPlayed = cardsPlayed;
	}

	public int getTotalDamage() {
		return totalDamage;
	}

	public void setTotalDamage(int totalDamage) {
		this.totalDamage = totalDamage;
	}

	public String getStrLogMessage() {
		return strLogMessage;
	}

	public void setStrLogMessage(String strLogMessage) {
		this.strLogMessage = strLogMessage;
	}

}
