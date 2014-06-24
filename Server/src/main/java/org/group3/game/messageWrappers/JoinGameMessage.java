package org.group3.game.messageWrappers;

import java.io.Serializable;

public class JoinGameMessage implements Serializable{
    

    private Integer gameId;
    private String inviteeEmail;

    public JoinGameMessage(Integer gameId, String inviteeEmail) {

        this.gameId = gameId;
        this.inviteeEmail = inviteeEmail;
    }


    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public String getInviteeEmail() {
        return inviteeEmail;
    }

    public void setInviteeEmail(String inviteeEmail) {
        this.inviteeEmail = inviteeEmail;
    }
}
