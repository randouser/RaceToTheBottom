-- cleanup tabless
DROP TABLE IF EXISTS card;

-- make the card table
CREATE TABLE IF NOT EXISTS card (
  id int(10) NOT NULL AUTO_INCREMENT,
  type varchar(255) DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  bodyText varchar(255) DEFAULT NULL,
  subtext varchar(255) DEFAULT NULL,
  moneyCost int(10) NOT NULL,
  workerCost int(10) NOT NULL,
  minDamage int(10) NOT NULL,
  maxDamage int(10) NOT NULL,
  multiplier int(10) NOT NULL,
  weight int(10) NOT NULL,
  PRIMARY KEY (id)
);


-- make the cards

INSERT INTO card (type, name, bodyText, subtext,moneyCost,workerCost,minDamage,maxDamage,multiplier,weight)
VALUES ('positive', 'RALLY', '+10','Add 10 from your supporters',7,4,10,10,1,10);

INSERT INTO card (type, name, bodyText, subtext,moneyCost,workerCost,minDamage,maxDamage,multiplier,weight)
VALUES ('positive', 'TV ADS', '+4 to +6','',5,3,4,6,1,8);

INSERT INTO card (type, name, bodyText, subtext,moneyCost,workerCost,minDamage,maxDamage,multiplier,weight)
VALUES ('positive', 'RADIO ADS', '+5','A solid increase',4,2,5,5,1,5);

INSERT INTO card (type, name, bodyText, subtext,moneyCost,workerCost,minDamage,maxDamage,multiplier,weight)
VALUES ('positive', 'INTERNET ADS', '+1 to +3','RON PAUL 2012',2,1,1,3,1,1);

INSERT INTO card (type, name, bodyText, subtext,moneyCost,workerCost,minDamage,maxDamage,multiplier,weight)
VALUES ('positive', 'DOOR-to-DOOR CAMPAIGNING', '+4','The basis of democracy',3,3,4,4,1,4);


INSERT INTO card (type, name, bodyText, subtext,moneyCost,workerCost,minDamage,maxDamage,multiplier,weight)
VALUES ('negative', 'NEGATIVE TV ADS', '-10','Swift-boat them',5,3,10,10,1,7);

INSERT INTO card (type, name, bodyText, subtext,moneyCost,workerCost,minDamage,maxDamage,multiplier,weight)
VALUES ('negative', 'NEGATIVE RADIO ADS', '-4','Burn their ears',3,2,4,4,1,3);

INSERT INTO card (type, name, bodyText, subtext,moneyCost,workerCost,minDamage,maxDamage,multiplier,weight)
VALUES ('negative', 'NEGATIVE ONLINE ADS', '-2 to -5','opponentsux.net',2,2,2,5,1,2);

INSERT INTO card (type, name, bodyText, subtext,moneyCost,workerCost,minDamage,maxDamage,multiplier,weight)
VALUES ('negative', 'PLANT A SPY', '-3 to -10','Deals more damage in later rounds',5,2,3,10,1,6);

INSERT INTO card (type, name, bodyText, subtext,moneyCost,workerCost,minDamage,maxDamage,multiplier,weight)
VALUES ('negative', 'DIG UP OLD DIRT', '-2x Turn','Deals twice the turn in damage',5,3,1,1,2,9);


