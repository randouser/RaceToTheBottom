package org.group3.game.messageWrappers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class GameMessage {


    public static final String GAME_START = "gameStart";
    public static final String GAME_LOOKUP = "gameLookup";
    public static final String GAME_REJECT = "gameReject";

    public static final String WAIT_ACCEPT = "waitForAccept";
    public static final String  WAIT_TURN = "waitForTurn";


    private String type;
    private int gameId;
    private String message;
    private boolean isInProgress;
    private boolean isUserTurn;
    private boolean needsUserConfirmation;
    private String gameType;
    private String gameName;


    public GameMessage(int gameId, String type, String message,String gameType,String gameName) {
        this.message = message;
        this.gameId = gameId;
        this.type = type;
        this.gameType = gameType;
        this.gameName = gameName;
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

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}
