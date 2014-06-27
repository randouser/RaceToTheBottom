package org.group3.game.messageWrappers;


import java.util.List;

public class LobbyMessage {
    public List<TurnMessage> inProgressGames;
    public List<JoinGameMessage> invites;

    public LobbyMessage(List<TurnMessage> inProgressGames,List<JoinGameMessage> invites) {
        this.inProgressGames = inProgressGames;
        this.invites = invites;
    }



    public List<TurnMessage> getInProgressGames() {
        return inProgressGames;
    }

    public void setInProgressGames(List<TurnMessage> inProgressGames) {
        this.inProgressGames = inProgressGames;
    }

    public List<JoinGameMessage> getInvites() {
        return invites;
    }

    public void setInvites(List<JoinGameMessage> invites) {
        this.invites = invites;
    }
}
