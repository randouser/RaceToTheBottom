package org.group3.game.messageWrappers;

import org.group3.game.model.card.Card;
import org.group3.game.model.game.District;
import org.group3.game.model.game.Game;
import org.group3.game.model.game.GameLog;
import org.group3.game.model.game.Player;
import org.group3.game.model.user.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nperkins on 8/2/14.
 */
public class PreTurnMessage {

    private Integer gameId;
    private List<Card> hand;
    private int maxWorkers;
    private int maxMoney;
    private List<GameLog> lastTurnLogs;
    private District currentDistrict;
    private int districtPointer;
    private List<Card> lastCardsDrawn;



    public PreTurnMessage(User user, Player player, Game game){
        this.gameId = game.getGameId();
        this.maxWorkers = player.getMaxWorkers();
        this.maxMoney = player.getMaxMoney();
        this.currentDistrict = game.getDistricts().get(game.getDistrictPointer());
        this.districtPointer = game.getDistrictPointer();
        this.lastTurnLogs = player.getLastAction().equals(Player.DO_DEBATE)? null : game.getLastTurnLogs(1);
        this.hand = player.isDebating() ? new ArrayList<Card>() : player.getHand();
        this.lastCardsDrawn = player.getLastCardsDrawn();
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
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



    public List<GameLog> getLastTurnLogs() {
        return lastTurnLogs;
    }

    public void setLastTurnLogs(List<GameLog> lastTurnLogs) {
        this.lastTurnLogs = lastTurnLogs;
    }



    public int getDistrictPointer() {
        return districtPointer;
    }

    public void setDistrictPointer(int districtPointer) {
        this.districtPointer = districtPointer;
    }

    public District getCurrentDistrict() {
        return currentDistrict;
    }

    public void setCurrentDistrict(District currentDistrict) {
        this.currentDistrict = currentDistrict;
    }

    public List<Card> getLastCardsDrawn() {
        return lastCardsDrawn;
    }

    public void setLastCardsDrawn(List<Card> lastCardsDrawn) {
        this.lastCardsDrawn = lastCardsDrawn;
    }
}
