package org.group3.game.utils;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class HashUtils {

    private static final String ENCRYPTION_FUNC = "SHA-256";


    public static String generateSalt(){
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

    public static String generatePasswordHash(String password, String salt) {

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(ENCRYPTION_FUNC);
        } catch (NoSuchAlgorithmException e) {
            throw new Error("Security utils - bad security algorithm");
        }

        //SALT, THEN PASSWORD!
        md.update(salt.getBytes());
        md.update(password.getBytes());

        return makeHexString(md.digest());
    }

    public static String generateToken(String email,String password,String expirationDate){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(ENCRYPTION_FUNC);
        } catch (NoSuchAlgorithmException e) {
            throw new Error("Security utils - bad security algorithm");
        }

        md.update(email.getBytes());
        md.update(password.getBytes());
        md.update(expirationDate.getBytes());

        return makeHexString(md.digest());
    }


    public static String makeHexString(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        for (byte bt : bytes) {
            sb.append(Integer.toString((bt & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();

    }

    public static boolean isPasswordEqualToHash(String password, String salt, String passwordHash){
        return generatePasswordHash(password,salt).equals(passwordHash);
    }



}
