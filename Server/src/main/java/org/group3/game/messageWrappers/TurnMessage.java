package org.group3.game.messageWrappers;

import org.group3.game.model.card.Card;
import org.group3.game.model.game.District;
import org.group3.game.model.game.Game;
import org.group3.game.model.game.Player;
import org.group3.game.model.user.User;

import java.io.Serializable;
import java.util.List;


public class TurnMessage implements Serializable{
    private Integer gameId;
    private String gameName;
    private List<Card> hand;
    private int maxWorkers;
    private int maxMoney;
    private boolean isInProgress;
    private int districtPointer;
    private int playerIndex;
    private boolean isUserTurn;
    private boolean needsUserConfirmation;

    List<District> districts;

    private String userToken;

//    public TurnMessage(Integer gameId,String gameName,String userToken, List<Card> hand, int maxWorkers, int maxMoney,List<District> districts,int districtPointer,int playerIndex,boolean isInProgress) {
//        this.gameId = gameId;
//        this.gameName = gameName;
//        this.hand = hand;
//        this.maxWorkers = maxWorkers;
//        this.maxMoney = maxMoney;
//        this.districts = districts;
//        this.userToken = userToken;
//        this.isInProgress = isInProgress;
//        this.districtPointer = districtPointer;
//        this.playerIndex = playerIndex;
//    }

    public TurnMessage(User user,Player player, Game game){
        this.gameId = game.getGameId();
        this.gameName = game.getGameName();
        this.hand = player.getHand();
        this.maxWorkers = player.getMaxWorkers();
        this.maxMoney = player.getMaxMoney();
        this.districts = game.getDistricts();
        this.userToken = user.getToken();
        this.isInProgress = game.isInProgress();
        this.districtPointer = game.getDistrictPointer();
        this.playerIndex = player.getPlayerIndex();
        this.isUserTurn = player == game.getCurrentPlayer();

        //implicitly true provided that the invitee is always the second player (index 1)
        this.needsUserConfirmation = (!this.isInProgress) && (player.getPlayerIndex() == 1);
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
}
