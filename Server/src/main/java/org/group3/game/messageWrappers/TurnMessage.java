package org.group3.game.messageWrappers;

import org.group3.game.model.card.Card;
import org.group3.game.model.game.District;
import org.group3.game.model.game.Game;
import org.group3.game.model.game.Player;
import org.group3.game.model.user.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class TurnMessage{
    private Integer gameId;
    private String gameName;
    private List<Card> hand;//5 cards 
    private int maxWorkers;//3 initially
    private int maxMoney;//3 initially
    private boolean isInProgress;
    private int districtPointer;
    private int playerIndex;
    private boolean isUserTurn;
    private boolean needsUserConfirmation;
    private String gameType;

    private boolean isDebate;


    List<District> districts;

    private String userToken;





    public TurnMessage(User user,Player player, Game game){
        this.gameId = game.getGameId();
        this.gameName = game.getGameName();
        this.maxWorkers = player.getMaxWorkers();
        this.maxMoney = player.getMaxMoney();
        this.districts = game.getDistricts();
        this.userToken = user==null ? null : user.getToken();
        this.isInProgress = game.isInProgress();
        this.districtPointer = game.getDistrictPointer();
        this.playerIndex = player.getPlayerIndex();
        this.isUserTurn = player == game.getCurrentPlayer();
        this.gameType = game.getType();

        this.hand = player.isDebating() ? new ArrayList<Card>() :player.getHand();

        this.isDebate = player.isDebating();

        //implicitly true provided that the invitee is always the second player (index 1)
        this.needsUserConfirmation = (!this.isInProgress) && (player.getPlayerIndex() == 1);
    }


    public boolean isDebate() {
        return isDebate;
    }

    public void setDebate(boolean isDebate) {
        this.isDebate = isDebate;
    }

    public boolean isUserTurn() {
        return isUserTurn;
    }

    public void setUserTurn(boolean isUserTurn) {
        this.isUserTurn = isUserTurn;
    }

    public boolean isNeedsUserConfirmation() {
        return needsUserConfirmation;
    }

    public void setNeedsUserConfirmation(boolean needsUserConfirmation) {
        this.needsUserConfirmation = needsUserConfirmation;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public int getDistrictPointer() {
        return districtPointer;
    }

    public void setDistrictPointer(int districtPointer) {
        this.districtPointer = districtPointer;
    }

    public boolean isInProgress() {
        return isInProgress;
    }

    public void setInProgress(boolean isInProgress) {
        this.isInProgress = isInProgress;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public List<District> getDistricts() {
        return districts;
    }

    public void setDistricts(List<District> districts) {
        this.districts = districts;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void setHand(List<Card> hand) {
        this.hand = hand;
    }

    public int getMaxWorkers() {
        return maxWorkers;
    }

    public void setMaxWorkers(int maxWorkers) {
        this.maxWorkers = maxWorkers;
    }

    public int getMaxMoney() {
        return maxMoney;
    }

    public void setMaxMoney(int maxMoney) {
        this.maxMoney = maxMoney;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
    
    public String getGameType() {
    	return gameType;
    }
    
    public void setGameType(String gameType) {
    	this.gameType = gameType;
    }
}
