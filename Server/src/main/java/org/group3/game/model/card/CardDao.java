package org.group3.game.model.card;



import java.util.List;

public interface CardDao {

	
    List<Card> getCardsOfType(String type);
    List<Card> getRandCards(int size, CardServiceImpl instance);
    
}
