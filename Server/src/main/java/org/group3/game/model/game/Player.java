package org.group3.game.model.game;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.group3.game.model.card.Card;
import org.group3.game.model.user.User;

import java.util.ArrayList;
import java.util.List;


public class Player {

    @JsonIgnoreProperties
    private static final int MAX_HAND = 5;

    private Integer id;
    private List<Card> hand;
    private List<Card> deck;
    private int maxMoney;
    private int maxWorkers;
    private String email;



    public Player(Integer id,List<Card> deck, int maxMoney, int maxWorkers, String email) {
        this.id = id;
        this.deck = deck;
        this.maxMoney = maxMoney;
        this.maxWorkers = maxWorkers;
        this.email = email;

        this.hand = new ArrayList<>();
        drawHand();
    }


    public void drawHand(){
        while(this.hand.size() < MAX_HAND && this.deck.size() !=0 ){
            this.hand.add(this.deck.remove(0));
        }
    }

    public boolean canPlayCards(List<Card> cards){
        int moneySpent = 0;
        int workersSpent = 0;
        boolean isCardInHand = true;
        for(Card card : cards){
            moneySpent += card.getMoneyCost();
            workersSpent += card.getWorkerCost();
            isCardInHand = isCardInHand && this.hand.contains(card);
        }

        return (moneySpent < this.maxMoney && workersSpent < this.maxWorkers && isCardInHand);


    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void setHand(List<Card> hand) {
        this.hand = hand;
    }

    public List<Card> getDeck() {
        return deck;
    }

    public void setDeck(List<Card> deck) {
        this.deck = deck;
    }

    public int getMaxMoney() {
        return maxMoney;
    }

    public void setMaxMoney(int maxMoney) {
        this.maxMoney = maxMoney;
    }

    public int getMaxWorkers() {
        return maxWorkers;
    }

    public void setMaxWorkers(int maxWorkers) {
        this.maxWorkers = maxWorkers;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
