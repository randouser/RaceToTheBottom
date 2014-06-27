package org.group3.game.model.card;

import org.group3.game.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;


@Repository
public class CardDaoImpl implements CardDao {

    @Autowired
    JdbcTemplate jdbcTemplate;


    @Override
    public List<Card> getCardsOfType(String type) {
        String sql = "SELECT * FROM card where type = ?";
        Object[] args = {type};

        return jdbcTemplate.query(sql,args,new BeanPropertyRowMapper<>(Card.class));


    }

    @Override
    public List<Card> getRandCards(int size) {
        String sql = "SELECT * FROM card WHERE weight = ?";

        List<Card> newCard = new ArrayList<>(1);
        
        List<Card> returnCards = new ArrayList<>(size);
        
        int counter = 1;
        
        int randomWeight = 0;
        
        while (counter <= size)
        {
        	
        	randomWeight = linearCardGenerator(MAXWEIGHT);
       
        	Object[] args = { randomWeight };
        	
        	//Would like a way to get a single Card instead of a List of them from query
        	newCard = jdbcTemplate.query(sql,args,new BeanPropertyRowMapper<>(Card.class));
        
        	returnCards.addAll(newCard);
        	
        	counter++;
        	
        }

        return returnCards;

    }


    @Override
    public int linearCardGenerator(int maxWeight)
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
