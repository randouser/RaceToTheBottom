-- make the game storage table
CREATE TABLE IF NOT EXISTS gamestore(
  id int AUTO_INCREMENT NOT NULL,
  playerOneId int, -- we use playerIds as a way to identify a game with a user, should match a user id, but may be null
  playerTwoId int,
  serializedGame MEDIUMTEXT NOT NULL,
  lastPlayed TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (playerOneId) REFERENCES user(id) -- this works because starting player is always a user
);