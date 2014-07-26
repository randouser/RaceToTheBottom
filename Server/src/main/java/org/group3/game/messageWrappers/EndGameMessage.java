package org.group3.game.messageWrappers;

/**
 * Created by nperkins on 7/25/14.
 */
public class EndGameMessage {

    private String winnerEmail;
    private String gameName;
    private int gameId;
    private String type;



    public EndGameMessage(TurnMessage turnMessage){
        this.winnerEmail = turnMessage.getWinnerEmail();
        this.gameName = turnMessage.getGameName();
        this.gameId = turnMessage.getGameId();
        this.type = "gameEnd";

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
}
