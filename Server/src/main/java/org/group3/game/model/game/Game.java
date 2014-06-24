package org.group3.game.model.game;


import org.group3.game.model.card.Card;
import org.group3.game.model.user.User;

import java.util.List;

public class Game {


    private Player[] players;
    private Integer gameId;
    private List<District> districts;
    private int turnIndex; //could be 0 or 1 to indicate which player, either that or a player id
    private int districtPointer;
    private List<GameLog> log;
    private String type;
    private boolean isInProgress;


    public Game(Integer gameId, Player playerOne, Player playerTwo, List<District> districts, String type) {
        this.players = new Player[2];
        this.players[0] = playerOne;
        this.players[1] = playerTwo;
        this.turnIndex = 0;

        this.gameId = gameId;
        this.districts = districts;

        this.districtPointer = 0;
        this.log = log;
        this.type = type;
        this.isInProgress = false;
    }

    public void toggleTurn(){
        //we could do a binary flip, but might as well keep it more flexible
        turnIndex = (turnIndex == 0) ? 1 : 0;
    }


    public Player getCurrentPlayer(){
        return players[turnIndex];
    }

    public Player getPendingPlayer(){
        return players[1];
    }


    public boolean playCards(String playerEmail,List<Card> cards) {
        int otherplayerIndex = turnIndex == 0? 1 : 0;
        Player curPlayer = players[turnIndex];
        Player otherPlayer = players[otherplayerIndex];

        //check if right turn
        if(curPlayer.getEmail().equals(playerEmail)){

            //check if cards can be played
             if(curPlayer.canPlayCards(cards)){
                 //deal damage //TODO right now just focus on simple damage, worry about complicated stuff later
                 int damage = 0;
                 for(Card card : cards){
                     damage += card.getDamage();
                 }
                 districts.get(districtPointer).setPlayerScore(turnIndex, damage);
                 districts.get(districtPointer).setPlayerScore(otherplayerIndex, (-1 * damage));

                 //draw cards
                 curPlayer.drawHand(); //TODO we don't check if you run out of your deck yet

                 return true;

             }else{
                return false;
             }


        }else{
            return false;
        }


    }

    public boolean isInProgress() {
        return isInProgress;
    }

    public void setInProgress(boolean isInProgress) {
        this.isInProgress = isInProgress;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public List<District> getDistricts() {
        return districts;
    }

    public void setDistricts(List<District> districts) {
        this.districts = districts;
    }


    public int getDistrictPointer() {
        return districtPointer;
    }

    public void setDistrictPointer(int districtPointer) {
        this.districtPointer = districtPointer;
    }

    public List<GameLog> getLog() {
        return log;
    }

    public void setLog(List<GameLog> log) {
        this.log = log;
    }


}