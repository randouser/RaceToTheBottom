package org.group3.game.messageWrappers;

import org.group3.game.model.card.Card;
import org.group3.game.model.game.District;

import java.io.Serializable;
import java.util.List;


public class TurnMessage implements Serializable{

    private List<Card> hand;
    private int maxWorkers;
    private int maxMoney;

    List<District> districts;

    private String userToken;

    public TurnMessage(String userToken, List<Card> hand, int maxWorkers, int maxMoney,List<District> districts) {
        this.hand = hand;
        this.maxWorkers = maxWorkers;
        this.maxMoney = maxMoney;
        this.districts = districts;
        this.userToken = userToken;
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

}
