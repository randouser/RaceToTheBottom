package org.group3.game.model.card;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    CardDao cardDao;

    @Override
    public List<Card> getRandomDeck(int size) {

        //TODO better randomizer/type distribution 
    	return cardDao.getRandCards(size, this);


    }
    
    @Override
    public int weightedRandomNumber(int maxWeight)
    {
    	
    	int randomMult = maxWeight * (maxWeight + 1) / 2;
    	
    	Random newRandom = new Random();
    	
    	int randomInt = newRandom.nextInt(randomMult);
    	
    	int linearRandom = 0;
    	
    	for (int i = maxWeight; randomInt >= 0; i--)
    	{
    		
    		randomInt -= i;
    		
    		linearRandom++;
    		
    	}
    	
    	return linearRandom;
    	
    }
}
