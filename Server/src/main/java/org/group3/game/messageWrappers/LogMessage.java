package org.group3.game.messageWrappers;

import org.group3.game.model.card.Card;
import org.group3.game.model.game.District;
import org.group3.game.model.game.Game;
import org.group3.game.model.game.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LogMessage {
	
	private Player curPlayer;
	private Player oppPlayer;
	private List<Card> cardsPlayed;
	private int totalDamage;
	private String logMessage;
	
	public LogMessage(Player curPlayer, Player oppPlayer, List<Card> cardsPlayed, int totalDamage, String logMessage)
	{
		
		this.curPlayer = curPlayer;
		this.oppPlayer = oppPlayer;
		this.cardsPlayed = cardsPlayed;
		this.totalDamage = totalDamage;
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

	public String getLogMessage() {
		return logMessage;
	}

	public void setLogMessage(String logMessage) {
		this.logMessage = logMessage;
	}

}
