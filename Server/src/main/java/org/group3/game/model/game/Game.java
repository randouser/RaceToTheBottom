package org.group3.game.model.game;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.group3.game.model.card.Card;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Game{
    private static final int DEBATE_BONUS = 2;

    private String gameName;
    private Player[] players;
    private Integer gameId;
    private List<District> districts;
    private int turnIndex; //could be 0 or 1 to indicate which player, either that or a player id
    private int districtPointer;
    private List<GameLog> turnLog;
    private String type;
    private boolean isInProgress;
    private String winnerEmail;
    private Integer winnerId;
    private Integer loserId;
    private boolean isDebate;

    public Game(){}

    public Game(Player playerOne, Player playerTwo, List<District> districts, String type) {
        this.players = new Player[2];
        this.players[0] = playerOne;
        this.players[1] = playerTwo;
        playerOne.setPlayerIndex(0);
        playerTwo.setDebateScore(1);

        this.turnIndex = 0;

        this.gameId = -1;
        this.districts = districts;

        this.districtPointer = 0;
        this.turnLog = new ArrayList<>();
        this.type = type;
        this.isInProgress = false;
        this.winnerEmail = null;

        updateGameName();
        this.isDebate = false;
    }

    public void updateGameName(){
        if(type.equals("single")){
            gameName = "Single-player game";
        }else{
            gameName = players[0].getEmail() + " vs " + players[1].getEmail();
        }
    }

    public void toggleTurn(){
        //xor with 1
        turnIndex = turnIndex ^ 0x00000001;
    }

    public void surrender(Integer userId) {
        Player curPlayer = assertCurrentPlayer(userId);
        Player otherPlayer = getNonCurrentPlayer();
        winnerEmail = otherPlayer.getEmail();
        winnerId = otherPlayer.getId();
        loserId = curPlayer.getId();
        this.setInProgress(false);
    }

    public Player getCurrentPlayer(){
        return players[turnIndex];
    }

    public Player getPendingPlayer(){
        return players[1];
    }


    public void setWinner(){
        int p1Score = 0, p2Score = 0;

        for(District district: districts){
            p1Score += district.getPlayerOneScore();
            p2Score += district.getPlayerTwoScore();
        }
        if(p1Score > p2Score){
            winnerEmail = players[0].getEmail();
            winnerId = players[0].getId();
            loserId = players[1].getId();
        }else if (p2Score < p1Score){
            winnerEmail = players[1].getEmail();
            winnerId = players[1].getId();
            loserId = players[0].getId();
        }else{ //tie?
            int randoIndex = new Random().nextInt(2); //between 0 and 1
            Player randoWinner = players[randoIndex];
            winnerEmail = randoWinner.getEmail();
            winnerId = randoWinner.getId();
            loserId = players[randoIndex ^ 1].getId();
        }
    }

    public void playCards(Integer playerId,List<Card> cards, boolean burnTurn) {
    	Player curPlayer = assertCurrentPlayer(playerId);
    	Player otherPlayer = getNonCurrentPlayer();


        curPlayer.setDebating(false);

        //check if cards can be played
         if(curPlayer.canPlayCards(cards) || burnTurn){
             //deal damage //TODO right now just focus on simple damage, worry about complicated stuff later
             int damage = 0;
             District curDistrict = districts.get(districtPointer);


             if (!burnTurn){
                 for(Card card : cards) {
                     damage += card.getMaxDamage();
                 }
                 curPlayer.removeCardsFromHand(cards);

                 //calculate damage/score for current district
                 curDistrict.increaseScoreForPlayer(curPlayer.getPlayerIndex(), damage);

                 //Add new CardsLog entry in list
                 turnLog.add(new CardsLog(cards, curPlayer, otherPlayer, districtPointer, curDistrict, damage));
             }
             else{

                 Random rand = new Random();
                 int addWorkers = 0;
                 int addMoney = 0;

                 //Give player random amount of workers and money, max 3
                 addWorkers = rand.nextInt(3) + 1;
                 addMoney = rand.nextInt(3) + 1;

                 curPlayer.setMaxWorkers(curPlayer.getMaxWorkers() + addWorkers);
                 curPlayer.setMaxMoney(curPlayer.getMaxMoney() + addMoney);

                 //Add new BurnTurnLog entry in list
                 turnLog.add(new BurnTurnLog(curPlayer));

             }



             curDistrict.setTurn(curDistrict.getTurn() + 1);

             //check if the district is out of turns
             if(curDistrict.getTurn() == 10){
                 //SET Debate Mode
                 for(Player p : players){
                    p.setDebating(true);
                 }
                 isDebate = true;
             }

             //draw cards
             curPlayer.drawHand();


         }else{
            throw new RuntimeException("This player cannot play the given cards!");
         }





    }

    public void finishDebate(Integer playerId,int debateScore){
        Player curPlayer = assertCurrentPlayer(playerId);
        Player otherPlayer = getNonCurrentPlayer();
        District curDistrict = districts.get(districtPointer);

        curPlayer.setDebating(false);
        curPlayer.setDebateScore(debateScore);


        if(otherPlayer.isDebating()){
            return;
        }

        this.isDebate = false;


        //debate logic, gives small bonus to person with lower score, we don't apply bonus if they happen to tie
        Player winningPlayer;
        if(curPlayer.getDebateScore() < otherPlayer.getDebateScore()){
            curDistrict.increaseScoreForPlayer(curPlayer.getPlayerIndex(),DEBATE_BONUS);
            winningPlayer = curPlayer;
        }else if (curPlayer.getDebateScore() > otherPlayer.getDebateScore()){
            curDistrict.increaseScoreForPlayer(otherPlayer.getPlayerIndex(),DEBATE_BONUS);
            winningPlayer = otherPlayer;
        }else{
            winningPlayer = null;
        }

        turnLog.add(new DebateLog(winningPlayer,districtPointer,debateScore,DEBATE_BONUS));

        //set winner for that district
        if(curDistrict.getPlayerOneScore() > curDistrict.getPlayerTwoScore()){
            curDistrict.setWinnerEmail(players[0].getEmail());
            curDistrict.setColor(players[0].getColor());
        }else if(curDistrict.getPlayerOneScore() < curDistrict.getPlayerTwoScore()){
            curDistrict.setWinnerEmail(players[1].getEmail());
            curDistrict.setColor(players[1].getColor());
        }else{
            curDistrict.setWinnerEmail("Tie");
        }

        //Reset players money and workers to base
        curPlayer.setMaxMoney(3);
        otherPlayer.setMaxMoney(3);
        curPlayer.setMaxWorkers(3);
        otherPlayer.setMaxWorkers(3);


        //move to next district
        ++districtPointer;

        //set final winner if out of districts
        if(districtPointer >= districts.size()){
            this.setWinner();
        }

    }

    public Player getNonCurrentPlayer(){
    	//xor with 1
        return players[turnIndex^0x00000001];
    }

    /**
     * Determines if the given id matches the current player.
     *
     * @throws  RuntimeException if this is not the matching players turn
     */
    public Player assertCurrentPlayer(Integer playerId){
        Player currentPlayer = getCurrentPlayer();

        boolean isCurPlayer = (playerId != null && playerId.equals(currentPlayer.getId()));
        if(!isCurPlayer){
            throw new RuntimeException("This player cannot take this turn!");
        }
        return currentPlayer;
    }


    public GameLog getLastTurnLog(){
        return turnLog.isEmpty() ? null : turnLog.get(turnLog.size() -1);
    }
    public List<GameLog> getLastTurnLogs(int n){
        int size = this.turnLog.size();

        if(turnLog.isEmpty()){
            return null;
        }else if(n > size){
            return turnLog;
        }else{
            return turnLog.subList(size-n,size);
        }
    }

    /**
     * This method analyses the turnLog of this game to determine
     * how many gameLogs should be sent to the client.  This helps to resolve the quirk
     * that after a debate, the second player will need to be able to see the last
     * debate result and the last cards played.
     *
     * @return the number of logs that should be sent to the client
     */
    public int getLogNumber(){
        int logSize = turnLog.size();
        if(logSize > 1 && turnLog.get(logSize-2) instanceof DebateLog){
            return 2;
        }else{
            return 1;
        }
    }



    public String getWinnerEmail() {
        return winnerEmail;
    }

    public void setWinnerEmail(String winnerEmail) {
        this.winnerEmail = winnerEmail;
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

    public List<GameLog> getTurnLog() {
        return turnLog;
    }

    public void setTurnLog(List<GameLog> turnLog) {
        this.turnLog = turnLog;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public Player[] getPlayers() {
        return players;
    }

    public int getTurnIndex() {
        return turnIndex;
    }

    public boolean isDebate() {
        return isDebate;
    }

    public void setDebate(boolean isDebate) {
        this.isDebate = isDebate;
    }

    public Integer getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(Integer winnerId) {
        this.winnerId = winnerId;
    }

    public Integer getLoserId() {
        return loserId;
    }

    public void setLoserId(Integer loserId) {
        this.loserId = loserId;
    }
}
