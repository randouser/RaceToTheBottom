package org.group3.game.model.card;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    CardDao cardDao;

    @Override
    public List<Card> getRandomDeck(int size) {

        //TODO better randomizer/type distribution
        return cardDao.getRandCards(size);

    }
}
