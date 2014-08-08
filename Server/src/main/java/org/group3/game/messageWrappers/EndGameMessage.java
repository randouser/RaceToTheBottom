package org.group3.game.messageWrappers;

/**
 * Created by nperkins on 7/25/14.
 */
public class EndGameMessage {

    private String winnerEmail;
    private String gameName;
    private int gameId;
    private String type;
    private int winsForUser;
    private int lossesForUser;



    public EndGameMessage(TurnMessage turnMessage,int winsForUser, int lossesForUser){
        this.winnerEmail = turnMessage.getWinnerEmail();
        this.gameName = turnMessage.getGameName();
        this.gameId = turnMessage.getGameId();
        this.type = "gameEnd";

        this.winsForUser = winsForUser;
        this.lossesForUser = lossesForUser;

    }

    public String getWinnerEmail() {
        return winnerEmail;
    }

    public void setWinnerEmail(String winnerEmail) {
        this.winnerEmail = winnerEmail;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getWinsForUser() {
        return winsForUser;
    }

    public void setWinsForUser(int winsForUser) {
        this.winsForUser = winsForUser;
    }

    public int getLossesForUser() {
        return lossesForUser;
    }

    public void setLossesForUser(int lossesForUser) {
        this.lossesForUser = lossesForUser;
    }
}
