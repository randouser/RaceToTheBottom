package org.group3.game.model.user;


import org.group3.game.utils.HashUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;


@Repository
public class UserDaoImpl implements UserDao {


    @Autowired
    JdbcTemplate jdbcTemplate;


    @Override
    public void registerUser(String email,String password,String name, String salt, String token, String passwordHash, int wins, int losses, Timestamp tokenExpirationDate) {

        String sql = "INSERT INTO user (name, email, passwordHash,salt,token,wins,losses,tokenExpirationDate)" +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] updateArgs = {name,email,passwordHash,salt,token,0,0,tokenExpirationDate};


        this.jdbcTemplate.update(sql, updateArgs);


    }

    @Override
    public User getUserByEmailToken(String email,String token){

        String sql = "SELECT * FROM user" +
                     " WHERE email = ? AND token = ?";
        Object[] args = {email,token};

        return jdbcTemplate.queryForObject(sql, args, new BeanPropertyRowMapper<>(User.class));

    }

    @Override
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM user" +
                     " WHERE email = ?";
        Object[] args = {email};

        try{
            return jdbcTemplate.queryForObject(sql, args, new BeanPropertyRowMapper<>(User.class));
        }catch(EmptyResultDataAccessException e){
            //if the user doesn't exist, return null instead of throwing exception
            return null;
        }
    }

    @Override
    public User getUserById(Integer id) {
        String sql = "SELECT * FROM user" +
                " WHERE id = ?";
        Object[] args = {id};

        return jdbcTemplate.queryForObject(sql, args, new BeanPropertyRowMapper<>(User.class));
    }


}
