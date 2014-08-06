package org.group3.game.messageWrappers;


import java.util.List;
import org.group3.game.model.user.User;

public class LobbyMessage {
    public List<TurnMessage> inProgressGames;
    public List<JoinGameMessage> invites;
    public List<User> leaderBoard;
    
    public LobbyMessage()
    {
    	
    }

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



	public List<User> getLeaderBoard() {
		return leaderBoard;
	}



	public void setLeaderBoard(List<User> leaderBoard) {
		this.leaderBoard = leaderBoard;
	}
}
