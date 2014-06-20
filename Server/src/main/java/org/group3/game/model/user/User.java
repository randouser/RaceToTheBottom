package org.group3.game.model.user;

 

public class User implements java.io.Serializable {
 
	private Integer id;
	private Integer wins;
	private Integer losses;
	private String name;
	private String email;
	private String salt;
    private String passwordHash;
	private String token;
	private String tokenExpirationDate;

	public User() {
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

    public String getTokenExpirationDate() {
        return tokenExpirationDate;
    }

    public void setTokenExpirationDate(String tokenExpirationDate) {
        this.tokenExpirationDate = tokenExpirationDate;
    }
}

