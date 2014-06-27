-- cleanup tabless
DROP TABLE IF EXISTS card;

-- make the card table
CREATE TABLE IF NOT EXISTS card (
  id int(10) NOT NULL AUTO_INCREMENT,
  type varchar(255) DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  bodyText varchar(255) DEFAULT NULL,
  moneyCost int(10) NOT NULL,
  workerCost int(10) NOT NULL,
  minDamage int(10) NOT NULL,
  maxDamage int(10) NOT NULL,
  multiplier int(10) NOT NULL,
  weight int(10) NOT NULL,
  PRIMARY KEY (id)
);


-- make the cards

INSERT INTO card (type, name, bodyText,moneyCost,workerCost,minDamage,maxDamage,multiplier,weight)
VALUES ('positive', 'Rally', '10 - money:7,workers:4',7,4,10,10,1,10);

INSERT INTO card (type, name, bodyText,moneyCost,workerCost,minDamage,maxDamage,multiplier,weight)
VALUES ('positive', 'TV Advertisements', '4,6 - money:5,workers:3',5,3,4,6,1,8);

INSERT INTO card (type, name, bodyText,moneyCost,workerCost,minDamage,maxDamage,multiplier,weight)
VALUES ('positive', 'Radio Advertisements', '5 - money:4,workers:2',4,2,5,5,1,5);

INSERT INTO card (type, name, bodyText,moneyCost,workerCost,minDamage,maxDamage,multiplier,weight)
VALUES ('positive', 'Internet Advertisements', '1,3 - money:2,workers:1',2,1,1,3,1,1);

INSERT INTO card (type, name, bodyText,moneyCost,workerCost,minDamage,maxDamage,multiplier,weight)
VALUES ('positive', 'Door-to-door campaigning', '4 - money:3,workers:3',3,3,4,4,1,4);


INSERT INTO card (type, name, bodyText,moneyCost,workerCost,minDamage,maxDamage,multiplier,weight)
VALUES ('negative', 'Negative TV ads', '10 - money:5,workers:3',5,3,10,10,1,7);

INSERT INTO card (type, name, bodyText,moneyCost,workerCost,minDamage,maxDamage,multiplier,weight)
VALUES ('negative', 'Negative Radio ads', '4 - money:3,workers:2',3,2,4,4,1,3);

INSERT INTO card (type, name, bodyText,moneyCost,workerCost,minDamage,maxDamage,multiplier,weight)
VALUES ('negative', 'Negative online ads', '2,5 - money:2,workers:2',2,2,2,5,1,2);

INSERT INTO card (type, name, bodyText,moneyCost,workerCost,minDamage,maxDamage,multiplier,weight)
VALUES ('negative', 'Plant a spy in enemy’s campaign team', '3,10 - money:5,workers:2',5,2,3,10,1,6);

INSERT INTO card (type, name, bodyText,moneyCost,workerCost,minDamage,maxDamage,multiplier,weight)
VALUES ('negative', 'Dig up old dirt', '2x Round - money:5,workers:3',5,3,1,1,2,9);


