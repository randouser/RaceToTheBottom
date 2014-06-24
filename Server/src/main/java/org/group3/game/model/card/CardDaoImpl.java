package org.group3.game.model.card;

import org.group3.game.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;


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
        String sql = "SELECT * FROM card ORDER BY RAND() LIMIT ?";
        Object[] args = {size};

        return jdbcTemplate.query(sql,args,new BeanPropertyRowMapper<>(Card.class));

    }


}
