package org.group3.game.model.game;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.group3.game.model.card.Card;
import org.group3.game.model.user.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Player {

    @JsonIgnoreProperties
    private static final int MAX_HAND = 5;
    @JsonIgnoreProperties
    public static final String RED = "red";
    @JsonIgnoreProperties
    public static final String BLUE = "blue";

    //last actions are useful for preTurn calculations, for some actions we don't want to display an animation
    @JsonIgnoreProperties
    public static final String PLAY_CARDS = "playCards";
    @JsonIgnoreProperties
    public static final String DO_DEBATE = "doDebate";
    @JsonIgnoreProperties
    public static final String BURN_TURN = "burnTurn";


    private Integer id;
    private List<Card> hand;
    private List<Card> deck;
    private List<Card> discardPile;
    private int maxMoney;
    private int maxWorkers;
    private String email;
    private int playerIndex;
    private boolean isDebating;
    private int debateScore;
    private String color;
    private List<Card> lastCardsDrawn;
    private String lastAction;

    public Player(){}

    public Player(Integer id,List<Card> deck, int maxMoney, int maxWorkers, String email, int playerIndex,String color) {
        this.id = id;
        this.deck = deck;
        this.maxMoney = maxMoney;
        this.maxWorkers = maxWorkers;
        this.email = email;
        this.playerIndex = playerIndex;
        this.discardPile = new ArrayList<>();
        this.color = color;
        this.lastCardsDrawn = new ArrayList<>();
        this.lastAction = null;

        this.hand = new ArrayList<>();
        drawHand();
    }


    public void drawHand(){
        clearLastCardsDrawn();
        while(hand.size() < MAX_HAND){
            //if deck is out of cards, reshuffle discards
            if(deck.isEmpty()){
                deck.addAll(discardPile);
                Collections.shuffle(deck);
                discardPile.clear();
            }
            //draw next card
            Card drawnCard = deck.remove(0);
            hand.add(drawnCard);
            lastCardsDrawn.add(drawnCard);
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

        return (moneySpent <= this.maxMoney && workersSpent <= this.maxWorkers && isCardInHand);


    }
    public void removeCardsFromHand(List<Card> cardsToRemove){
        for(Card card : cardsToRemove){
            int cardIndex = hand.indexOf(card);
            discardPile.add(hand.remove(cardIndex));
        }
    }

    public void clearLastCardsDrawn(){
        this.lastCardsDrawn.clear();
    }

    public boolean isDebating() {
        return isDebating;
    }

    public void setDebating(boolean isDebating) {
        this.isDebating = isDebating;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
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

    public int getDebateScore() {
        return debateScore;
    }

    public void setDebateScore(int debateScore) {
        this.debateScore = debateScore;
    }

    public List<Card> getDiscardPile() {
        return discardPile;
    }

    public void setDiscardPile(List<Card> discardPile) {
        this.discardPile = discardPile;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<Card> getLastCardsDrawn() {
        return lastCardsDrawn;
    }

    public void setLastCardsDrawn(List<Card> lastCardsDrawn) {
        this.lastCardsDrawn = lastCardsDrawn;
    }

    public String getLastAction() {
        return lastAction;
    }

    public void setLastAction(String lastAction) {
        this.lastAction = lastAction;
    }
}
