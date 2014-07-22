package org.group3.game.model.game;

/**
 * Created by nperkins on 7/21/14.
 */
public class BurnTurnLog extends GameLog {
    private boolean isBurnTurnLog;


    BurnTurnLog(Player curPlayer){
        super();

        this.isBurnTurnLog = true;
        this.logMessage = curPlayer.getEmail() + " has burned a turn for resources.";


    }



    public boolean isBurnTurnLog() {
        return isBurnTurnLog;
    }

    public void setBurnTurnLog(boolean isBurnTurnLog) {
        this.isBurnTurnLog = isBurnTurnLog;
    }
}
