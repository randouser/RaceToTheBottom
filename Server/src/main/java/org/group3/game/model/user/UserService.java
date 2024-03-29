package org.group3.game.model.user;

import org.group3.game.utils.HashUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;


@Service
public class UserService{
 
	@Autowired
    UserDao userDao;

 
	
	public User registerUser(String email, String password, String name){
        String salt = HashUtils.generateSalt();
        String passwordHash = HashUtils.generatePasswordHash(password,salt);
        Timestamp expTime = createExpTime();
        String token = HashUtils.generateToken(email,password,expTime.toString());
        DateTime dt = new DateTime();
        Timestamp registerTime = new Timestamp(dt.getMillis());
        userDao.registerUser(email,password,name,salt,token,passwordHash,0,0,expTime, registerTime);


        return userDao.getUserByEmailToken(email,token);
	}


    /**
     * This method gets the user by email and password.
     * As the user's full credentials are used, it also resets the user's token .
     *
     */
    public User getUserByEmailPassword(String email, String password) {

        User user = userDao.getUserByEmail(email);

        if(user != null && HashUtils.isPasswordEqualToHash(password, user.getSalt(),user.getPasswordHash())){

            //only update token if it's expired
            if(!assertTokenValid(user)){
                Timestamp newExpTime = createExpTime();
                DateTime dt = new DateTime();
                Timestamp lastLogin = new Timestamp(dt.getMillis());
                String newToken = HashUtils.generateToken(email,password,newExpTime.toString());

                userDao.updateUserToken(user, newToken, newExpTime, lastLogin);
                user.setToken(newToken);
                user.setTokenExpirationDate(newExpTime);
            }

            return user;
        }else{
            return null;
        }

    }


    public User getUserByEmailToken(String email, String userToken) {
        User user = userDao.getUserByEmail(email);


        if(user != null && user.getToken().equals(userToken)){
            return user;
        }else{
            return null;
        }

    }

    
    public User getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    
    public User getUserById(Integer id) {

        return userDao.getUserById(id);
    }
    
    public List<User> getTopPlayers(int howMany)
    {
    	
    	return userDao.getTopPlayer(howMany);
    	
    }
    
    public List<User> getInactivePlayers()
    {
    	
    	DateTime dt = new DateTime();
    	
    	//subtract 7 days from dt, pass as parameter to UserDao.getInactivePlayersByDate()
    	dt = dt.minusWeeks(1);

    	return userDao.getInactivePlayersByDate(dt);
    	
    }

    
    public void deleteUserByEmail(String email){
        userDao.deleteUserByEmail(email);

    }

    public void updateLastLogin(User user){
        userDao.updateLastLogin(user);
    }


    public boolean assertTokenValid(User user){
        return user != null && (new DateTime(user.getTokenExpirationDate()).isAfterNow());
    }

    private static Timestamp createExpTime(){

        DateTime dt = new DateTime().plusDays(7);

        return new Timestamp(dt.getMillis());
    }

    public void incrementWinById(Integer winnerId) {
        userDao.incrementWinById(winnerId);
    }

    public void incrementLossById(Integer loserId) {
        userDao.incrementLossById(loserId);
    }

    public void updateSendEmailOnTurn(User user, boolean sendEmailOnTurn) {
        userDao.updateSendEmailOnTurn(user,sendEmailOnTurn);
    }
}