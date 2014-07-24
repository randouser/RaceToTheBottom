package org.group3.game.model.game;

import org.codehaus.jackson.map.ObjectMapper;
import org.group3.game.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;

@Repository
public class GameDao{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Saves a game to the database.
     * This is what creates the ID for the game, a new game
     * should not have one until it is saved, and you must add it to an object manually.
     * This is because we rely on the auto-generated key of the gamestore table for
     * game IDs.  This prevents games from ever having overlapping IDs.
     *
     * @return the unique id of the saved game
     */
    public int save(final Game newGame){
        //remove old value if it exists
        KeyHolder holder = new GeneratedKeyHolder();
        final String sql = "INSERT INTO gamestore (playerOneId,playerTwoId,serializedGame,lastPlayed) VALUES (?,?,?,?)";
        jdbcTemplate.update(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection connection)
                    throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                Integer playerOneId = newGame.getPlayers()[0].getId();
                Integer playerTwoId = newGame.getPlayers()[1].getId();
                ps.setInt(1, playerOneId);
                if(playerTwoId == null){
                    ps.setNull(2, java.sql.Types.INTEGER);
                }else{
                    ps.setInt(2, playerTwoId);
                }
                ps.setString(3,stringifyGame(newGame));
                DateTime dt = new DateTime();
                Timestamp lastPlayed = new Timestamp(dt.getMillis());
                ps.setTimestamp(4, lastPlayed);
                return ps;
            }
        }, holder);

        return holder.getKey().intValue();

    }

    public void update(final Game existingGame){
        final String sql = "UPDATE gamestore SET playerOneId=?,playerTwoId=?,serializedGame=?,lastPlayed=? WHERE id=?";

        Player[] players = existingGame.getPlayers();
        DateTime dt = new DateTime();
        Timestamp lastPlayed = new Timestamp(dt.getMillis());
        Object[] updateArgs = {players[0].getId(),players[1].getId(),stringifyGame(existingGame),lastPlayed,existingGame.getGameId()};

        this.jdbcTemplate.update(sql, updateArgs);

    }

    public void delete(final Game existingGame){
        final String sql = "DELETE FROM gamestore WHERE id=?";

        Object[] updateArgs = {existingGame.getGameId()};

        this.jdbcTemplate.update(sql, updateArgs);
    }


    public void delete(final int gameId){
        final String sql = "DELETE FROM gamestore where id=?";
        jdbcTemplate.update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection connection)throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setInt(1, gameId);
                return ps;
            }
        });
    }


    public Game getGameById(final int id){
        String sql = "SELECT serializedGame FROM gamestore" +
                " WHERE id = ?";
        Object[] args = {id};

        return jdbcTemplate.query(sql,args,new ResultSetExtractor<Game>(){
            @Override
            public Game extractData(ResultSet rs) throws SQLException,DataAccessException {
                rs.next();
                Game retGame = parseGameJSON(rs.getString("serializedGame"));
                retGame.setGameId(id);
                return retGame;
            }
        });
    }



    public List<Game> getSavedGamesForUser(final User user){
        String sql = "SELECT * FROM gamestore where playerOneId=? OR playerTwoId=?";
        Object[] args = {user.getId(),user.getId()};

        return jdbcTemplate.query(sql,args,new ResultSetExtractor<List<Game>>(){
            @Override
            public List<Game> extractData(ResultSet rs) throws SQLException,DataAccessException {
                List<Game> retList = new ArrayList<>();
                while(rs.next()){
                    int id = rs.getInt("id");
                    Game game = parseGameJSON(rs.getString("serializedGame"));
                    game.setGameId(id);
                    retList.add(game);
                }

                return retList;
            }
        });

    }
    
    public List<Integer> getInactiveGameIdsByDate(DateTime curDateTime){
    	
    	Timestamp ts = new Timestamp(curDateTime.getMillis());
    	
    	String sql = "SELECT id FROM gamestore WHERE lastPlayed <= ?";
    	Object[] args = {ts};
    	
    	return jdbcTemplate.query(sql, args, new ResultSetExtractor<List<Integer>>(){
    		@Override
    		public List<Integer> extractData(ResultSet rs) throws SQLException,DataAccessException {
    			
    			List<Integer> retList = new ArrayList<>();
    			while(rs.next())
    			{
    				
    				int id = rs.getInt("id");
    				retList.add(id);
    				
    			}
    			
    			return retList;
    			
    		}
    	});
    	
    }




    private String stringifyGame(Game gameObj){
        try {
            return new ObjectMapper().writer().writeValueAsString(gameObj);
        } catch (IOException e) {
            throw new RuntimeException("Error in turning Game to JSON" + e.getMessage());
        }
    }

    private Game parseGameJSON(String gameJSON){
        try {
            return new ObjectMapper().readValue(gameJSON, Game.class);
        } catch (IOException e) {
            throw new RuntimeException("Error in reading gameJSON" + e.getMessage());
        }

    }



    //Code for possibly using byte serialization, may go back to this, but it has some issues.

//    public byte[] serializeGame(Game game) {
//        ByteArrayOutputStream b = new ByteArrayOutputStream();
//
//        ObjectOutputStream o = null;
//        try {
//            o = new ObjectOutputStream(b);
//            o.writeObject(game);
//            return b.toByteArray();
//        } catch (IOException e) {
//            throw new Error(e.getMessage());
//        }
//
//    }
//
//    public Game deSerializeGame(byte[] bytes) {
//        ByteArrayInputStream arrayStream = new ByteArrayInputStream(bytes);
//        ObjectInputStream objStream = null;
//        try {
//            objStream = new ObjectInputStream(arrayStream);
//            return (Game)objStream.readObject();
//        } catch (Exception e) {
//            throw new Error(e.getMessage());
//        }
//
//
//    }


}
