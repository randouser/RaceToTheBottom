package org.group3.game.model.user;


import java.sql.Timestamp;

public class User{
 
	private Integer id;
	private Integer wins;
	private Integer losses;
	private String name;
	private String email;
	private String salt;
    private String passwordHash;
	private String token;
	private Timestamp tokenExpirationDate;
    boolean isAdmin;

	public User() {
	}


    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWins() {
        return wins;
    }

    public void setWins(Integer wins) {
        this.wins = wins;
    }

    public Integer getLosses() {
        return losses;
    }

    public void setLosses(Integer losses) {
        this.losses = losses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Timestamp getTokenExpirationDate() {
        return tokenExpirationDate;
    }

    public void setTokenExpirationDate(Timestamp tokenExpirationDate) {
        this.tokenExpirationDate = tokenExpirationDate;
    }


    @Override
    public boolean equals(Object obj){
        return (obj instanceof User) && ((User) obj).id.equals(this.id);
    }

    @Override
    public int hashCode(){
        return this.id.hashCode();
    }
}

