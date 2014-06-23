package org.group3.game.controllers;

public class GameMessage {
    private String userEmail;
    private String userToken;
    private String gameType;
    private String emailToNotify;

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
