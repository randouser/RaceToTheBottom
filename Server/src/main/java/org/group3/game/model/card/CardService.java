package org.group3.game.model.card;

import java.util.List;

/**
 * Created by alleninteractions on 6/23/14. 
 */
public interface CardService {

    List<Card> getRandomDeck(int size);
    int weightedRandomNumber(int maxWeight);
    
}
