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
public class CardDaoImpl implements CardDao, CardConstants {

    @Autowired
    JdbcTemplate jdbcTemplate;
    
    CardConstants cardConstants;


    @Override
    public List<Card> getCardsOfType(String type) {
        String sql = "SELECT * FROM card where type = ?";
        Object[] args = {type};

        return jdbcTemplate.query(sql,args,new BeanPropertyRowMapper<>(Card.class));


    }

  
    
    
    @Override
    public List<Card> getRandCards(int size, CardServiceImpl instance) {
        String sql = "SELECT * FROM card ORDER BY weight;";
        
        List<Card> returnCards = new ArrayList<>(size);
        
        List<Card> allCards = new ArrayList<>(UNIQUE_CARDS);
        
        Object[] args = {};
        
        allCards = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper<>(Card.class));
        
        int counter = 1;
        
        int randomWeight = 0;
        
        while (counter <= size)
        {
        	
        	randomWeight = instance.weightedRandomNumber(MAX_WEIGHT);
        
        	returnCards.add(allCards.get(randomWeight - 1));
        	
        	counter++;
        	
        }

        return returnCards;

    }
    
    


}
