package org.group3.game.model.game;




public class District {

    private int playerOneScore;
    private int playerTwoScore;
    private int turn;
    private String type;
    private String winnerEmail;
    private String color;

    public District(){}

    public District(String type, int playerTwoScore, int playerOneScore) {
        this.type = type;
        this.turn = 0;
        this.playerTwoScore = playerTwoScore;
        this.playerOneScore = playerOneScore;
        winnerEmail = null;
        color = "gray";
    }


    public void setPlayerScore(int playerIndex, int scoreDelta){
        if(playerIndex == 0){
            playerOneScore += scoreDelta;
        }else if(playerIndex == 1){
            playerTwoScore += scoreDelta;
        }
    }

    public void increaseScoreForPlayer(int playerIndex,int scoreDelta){
        if(playerIndex == 0){
            playerOneScore += scoreDelta;
            playerTwoScore -= scoreDelta;
        }else if(playerIndex == 1){
            playerTwoScore += scoreDelta;
            playerOneScore -= scoreDelta;
        }
    }

    public String getWinnerEmail() {
        return winnerEmail;
    }

    public void setWinnerEmail(String winnerEmail) {
        this.winnerEmail = winnerEmail;
    }

    public int getPlayerOneScore() {
        return playerOneScore;
    }

    public void setPlayerOneScore(int playerOneScore) {
        this.playerOneScore = playerOneScore;
    }

    public int getPlayerTwoScore() {
        return playerTwoScore;
    }

    public void setPlayerTwoScore(int playerTwoScore) {
        this.playerTwoScore = playerTwoScore;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
