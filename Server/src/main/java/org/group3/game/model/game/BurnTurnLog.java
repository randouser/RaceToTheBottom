package org.group3.game.model.game;


/**
 * Created by nperkins on 7/21/14.
 */

public class BurnTurnLog extends GameLog {
    private boolean isBurnTurnLog;
    private int workersAdded;
    private int moneyAdded;


   BurnTurnLog()
   {
	   super();
   }
    
    
    BurnTurnLog(Player curPlayer,int workersAdded, int moneyAdded){
        super();

        this.isBurnTurnLog = true;
        this.workersAdded = workersAdded;
        this.moneyAdded = moneyAdded;
        this.logMessage = curPlayer.getEmail() + " has burned a turn for resources.";


    }
    
    



    public boolean isBurnTurnLog() {
        return isBurnTurnLog;
    }

    public void setBurnTurnLog(boolean isBurnTurnLog) {
        this.isBurnTurnLog = isBurnTurnLog;
    }

    public int getWorkersAdded() {
        return workersAdded;
    }

    public void setWorkersAdded(int workersAdded) {
        this.workersAdded = workersAdded;
    }

    public int getMoneyAdded() {
        return moneyAdded;
    }

    public void setMoneyAdded(int moneyAdded) {
        this.moneyAdded = moneyAdded;
    }
}
