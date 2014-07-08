package org.group3.game.model.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;


@Repository
public class UserDao {


    @Autowired
    JdbcTemplate jdbcTemplate;


    
    public void registerUser(String email,String password,String name, String salt, String token, String passwordHash, int wins, int losses, Timestamp tokenExpirationDate) {

        String sql = "INSERT INTO user (name, email, passwordHash,salt,token,wins,losses,tokenExpirationDate)" +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] updateArgs = {name,email,passwordHash,salt,token,0,0,tokenExpirationDate};


        this.jdbcTemplate.update(sql, updateArgs);


    }

    
    public User getUserByEmailToken(String email,String token){

        String sql = "SELECT * FROM user" +
                     " WHERE email = ? AND token = ?";
        Object[] args = {email,token};

        return jdbcTemplate.queryForObject(sql, args, new BeanPropertyRowMapper<>(User.class));

    }

    
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

    
    public User getUserById(Integer id) {
        String sql = "SELECT * FROM user" +
                " WHERE id = ?";
        Object[] args = {id};

        return jdbcTemplate.queryForObject(sql, args, new BeanPropertyRowMapper<>(User.class));
    }

    
    public void deleteUserByEmail(final String email){
        final String sql = "DELETE FROM user where email=?";
        jdbcTemplate.update(new PreparedStatementCreator() {
            
            public PreparedStatement createPreparedStatement(Connection connection)throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, email);
                return ps;
            }
        });
    }


}
