-- turn this on to clear out users
# DROP TABLE IF EXISTS user;

-- make the user table
CREATE TABLE IF NOT EXISTS user(
  name varchar(255) DEFAULT NULL,
  email varchar(255) DEFAULT NULL,
  passwordHash varchar(255) DEFAULT NULL,
  salt varchar(255) DEFAULT NULL,
  token varchar(255) DEFAULT NULL,
  id int(11) NOT NULL AUTO_INCREMENT,
  wins int(11) NOT NULL,
  losses int(11) NOT NULL,
  tokenExpirationDate date DEFAULT NULL,
  admin boolean DEFAULT FALSE,
  registerTime TIMESTAMP DEFAULT 0,
  lastLogin TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  sendEmailOnTurn boolean default false,
  PRIMARY KEY (id)
);