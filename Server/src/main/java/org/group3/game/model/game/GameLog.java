package org.group3.game.model.game;


import org.group3.game.model.card.Card;
import org.group3.game.model.game.Player;
import org.group3.game.model.game.District;

import java.util.List;
import java.util.ArrayList;

public abstract class GameLog {

    protected String logMessage;


    protected GameLog(){}

    protected GameLog(String logMessage) {
        this.logMessage = logMessage;

    }


    public String getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(String logMessage) {
        this.logMessage = logMessage;
    }


}
