package org.group3.game.model.user;

import org.group3.game.utils.HashUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;


@Service
public class UserServiceImpl implements UserService {
 
	@Autowired
    UserDao userDao;

 
	@Override
	public User registerUser(String email, String password, String name){
        String salt = HashUtils.generateSalt();
        String passwordHash = HashUtils.generatePasswordHash(password,salt);
        Timestamp expTime = createExpTime();
        String token = HashUtils.generateToken(email,password,expTime.toString());

        userDao.registerUser(email,password,name,salt,token,passwordHash,0,0,expTime);


        return userDao.getUserByEmailToken(email,token);
	}

    @Override
    public User getUserByEmailPassword(String email, String password) {

        User user = userDao.getUserByEmail(email);

        boolean isPassword = HashUtils.isPasswordEqualToHash(password, user.getSalt(),user.getPasswordHash());

        if(isPassword){
            return user;
        }else{
            return null;
        }

    }

    @Override
    public User getUserByEmailToken(String email, String userToken) {
        User user = userDao.getUserByEmail(email);

        if(user.getToken().equals(userToken)){
            return user;
        }else{
            return null;
        }

    }


    private static Timestamp createExpTime(){

        DateTime dt = new DateTime().plusDays(7);

        return new Timestamp(dt.getMillis());
    }
}