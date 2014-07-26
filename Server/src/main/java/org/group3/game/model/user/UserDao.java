package org.group3.game.model.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Repository;

import org.joda.time.DateTime;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;


@Repository
public class UserDao {


    @Autowired
    JdbcTemplate jdbcTemplate;


    
    public void registerUser(String email,String password,String name, String salt, String token, String passwordHash, int wins, int losses, Timestamp tokenExpirationDate, Timestamp registerTime) {

        String sql = "INSERT INTO user (name, email, passwordHash,salt,token,wins,losses,tokenExpirationDate,registerTime,lastLogin) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] updateArgs = {name,email,passwordHash,salt,token,0,0,tokenExpirationDate,registerTime,registerTime};


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


    public void updateUserToken(User user,String token, Timestamp timestamp, Timestamp lastLogin){
        String sql = "UPDATE user SET token=?,tokenExpirationDate=?,lastLogin=? where id = ?";

        Object[] updateArgs = {token,timestamp,lastLogin,user.getId()};

        this.jdbcTemplate.update(sql, updateArgs);
    }


    public User getUserByToken(String token){
        String sql = "SELECT * FROM user" +
                " WHERE token = ?";
        Object[] args = {token};

        try{
            return jdbcTemplate.queryForObject(sql, args, new BeanPropertyRowMapper<>(User.class));
        }catch(EmptyResultDataAccessException e){
            //if the user doesn't exist, return null instead of throwing exception
            return null;
        }
    }
    
    public List<User> getTopPlayer(int howMany)
    {
    	
    	String sql = "SELECT * FROM user ORDER BY wins LIMIT ?";
    	
    	Object[] args = {howMany};
    	
    	List<User> leaderBoardUsers = new ArrayList<User>(howMany);
    	

    	leaderBoardUsers = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper<>(User.class));
    	
    	return leaderBoardUsers;

    	
    }
    
    public List<User> getInactivePlayersByDate(DateTime curDateTime)
    {
    	
    	Timestamp ts = new Timestamp(curDateTime.getMillis());
    	
    	String sql = "SELECT * FROM user WHERE lastLogin <= ?";
    	
    	Object[] args = {ts};
    	
    	List<User> inactivePlayers = new ArrayList<User>();
    	
    	inactivePlayers = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper<>(User.class));
    	
    	return inactivePlayers;
    	
    }

    public void updateLastLogin(User user){
        Object[] args = {user.getId()};
        String sql = "UPDATE user SET lastLogin=CURRENT_TIMESTAMP WHERE id=?";

        this.jdbcTemplate.update(sql, args);
    }


    public void incrementWinById(Integer winnerId) {
        Object[] args = {winnerId};
        String sql = "UPDATE user SET wins=(wins + 1) WHERE id=?";

        this.jdbcTemplate.update(sql, args);
    }

    public void incrementLossById(Integer loserId) {
        Object[] args = {loserId};
        String sql = "UPDATE user SET losses=(losses + 1) WHERE id=?";

        this.jdbcTemplate.update(sql, args);

    }
}
