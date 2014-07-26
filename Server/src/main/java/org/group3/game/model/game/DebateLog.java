package org.group3.game.model.game;


/**
 * Created by nperkins on 7/20/14.
 */

public class DebateLog extends GameLog{

    private boolean isDebateLog;
    private int score;
    private Integer winningPlayerId;
    private int curDistrictIndex;
    private int bonus;

    public DebateLog(){
    	
    	super();
    }

    public DebateLog(Player winningPlayer,int districtPointer, int score,int bonus){
        super();

        if(winningPlayer == null){
            this.logMessage = "The debate was a tie!";
            this.winningPlayerId = null;

        }else{
            this.logMessage = "Player: " + winningPlayer.getEmail() + " has won the debate with the score " + score;
            this.winningPlayerId = winningPlayer.getId();
        }


        this.score = score;
        this.isDebateLog = true; //for the javascript to know what class this is
        this.curDistrictIndex = districtPointer;
        this.bonus = bonus;

    }

    public int getCurDistrictIndex() {
        return curDistrictIndex;
    }

    public void setCurDistrictIndex(int curDistrictIndex) {
        this.curDistrictIndex = curDistrictIndex;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public Integer getWinningPlayerId() {
        return winningPlayerId;
    }

    public void setWinningPlayerId(Integer winningPlayerId) {
        this.winningPlayerId = winningPlayerId;
    }

    public boolean isDebateLog() {
        return isDebateLog;
    }

    public void setDebateLog(boolean isDebate) {
        this.isDebateLog = isDebate;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


}
