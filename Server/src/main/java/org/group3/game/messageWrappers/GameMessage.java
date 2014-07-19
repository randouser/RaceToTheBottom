package org.group3.game.messageWrappers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


public class GameMessage {

    @JsonIgnoreProperties
    public static final String GAME_START = "gameStart";

    @JsonIgnoreProperties
    public static final String GAME_LOOKUP = "gameLookup";


    private String type;
    private int gameId;
    private String message;
    private boolean isInProgress;
    private boolean isUserTurn;
    private boolean needsUserConfirmation;


    public GameMessage(int gameId, String type, String message) {
        this.message = message;
        this.gameId = gameId;
        this.type = type;
    }
    public GameMessage(int gameId, String type, String message, boolean isPending, boolean isUserTurn,boolean needsUserConfirmation) {
        this.message = message;
        this.gameId = gameId;
        this.type = type;
        this.isInProgress = isPending;
        this.isUserTurn = isUserTurn;
        this.needsUserConfirmation = needsUserConfirmation;
    }

    public static String getGameStart() {
        return GAME_START;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isInProgress() {
        return isInProgress;
    }

    public void setInProgress(boolean isPending) {
        this.isInProgress = isPending;
    }

    public boolean isUserTurn() {
        return isUserTurn;
    }

    public void setUserTurn(boolean isUserTurn) {
        this.isUserTurn = isUserTurn;
    }

    public boolean isNeedsUserConfirmation() {
        return needsUserConfirmation;
    }

    public void setNeedsUserConfirmation(boolean needsUserConfirmation) {
        this.needsUserConfirmation = needsUserConfirmation;
    }
    
}
