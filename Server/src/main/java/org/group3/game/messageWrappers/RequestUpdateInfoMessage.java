package org.group3.game.messageWrappers;

/**
 * Created by nperkins on 8/8/14.
 */
public class RequestUpdateInfoMessage {
    private boolean isSendEmailOnTurn;
    private String userEmail;
    private String userToken;


    RequestUpdateInfoMessage(){}

    public boolean isSendEmailOnTurn() {
        return isSendEmailOnTurn;
    }

    public void setSendEmailOnTurn(boolean isSendEmailOnTurn) {
        this.isSendEmailOnTurn = isSendEmailOnTurn;
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
}
