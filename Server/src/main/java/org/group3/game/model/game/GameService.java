package org.group3.game.model.game;

import org.group3.game.messageWrappers.GameMessage;
import org.group3.game.messageWrappers.LobbyMessage;
import org.group3.game.messageWrappers.TurnMessage;
import org.group3.game.model.card.Card;
import org.group3.game.model.user.User;

import java.util.List;

public interface GameService {

    GameMessage createGame(User user, String type, String inviteeEmail, User invitee);

    TurnMessage joinGame(User user, String inviteEmail, int gameId);

    TurnMessage takeTurn(User user,int gameId,List<Card> cardsPlayed, boolean burnTurn,int debateScore,boolean surrender);

    LobbyMessage getActiveGamesForUser(User user);

    void deleteGameById(int gameId);
}
