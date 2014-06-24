package org.group3.game.model.game;

import org.group3.game.messageWrappers.TurnMessage;
import org.group3.game.model.card.Card;
import org.group3.game.model.user.User;

import java.util.List;

public interface GameService {

    int createGame(User user, String type, String inviteeEmail);

    TurnMessage joinGame(User user, String inviteEmail, int gameId);

    TurnMessage takeTurn(User user,int gameId,List<Card> cardsPlayed);

}
