package org.group3.game.messageWrappers;

public class StartGameMessage {
    private String userEmail;
    private String userToken;
    private String gameType;
    private String emailToNotify;
    private Integer gameId;

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getEmailToNotify() {
        return emailToNotify;
    }

    public void setEmailToNotify(String emailToNotify) {
        this.emailToNotify = emailToNotify;
    }
}
