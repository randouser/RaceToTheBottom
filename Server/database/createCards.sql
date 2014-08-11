-- cleanup tabless
DROP TABLE IF EXISTS card;

-- make the card table
CREATE TABLE IF NOT EXISTS card (
  id int(10) NOT NULL AUTO_INCREMENT,
  type varchar(255) DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  tag varchar(255) DEFAULT NULL,
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

INSERT INTO card (type, name, tag, bodyText, subtext,moneyCost,workerCost,minDamage,maxDamage,multiplier,weight)
VALUES ('positive', 'RALLY','rally', '+10','Add 10 from your supporters',7,4,10,10,1,10);

INSERT INTO card (type, name, tag, bodyText, subtext,moneyCost,workerCost,minDamage,maxDamage,multiplier,weight)
VALUES ('positive', 'TV ADS','tv_ads', '+4 to +6','',5,3,4,6,1,8);

INSERT INTO card (type, name, tag, bodyText, subtext,moneyCost,workerCost,minDamage,maxDamage,multiplier,weight)
VALUES ('positive', 'RADIO ADS','radio_ads', '+5','A solid increase',4,2,5,5,1,5);

INSERT INTO card (type, name, tag, bodyText, subtext,moneyCost,workerCost,minDamage,maxDamage,multiplier,weight)
VALUES ('positive', 'INTERNET ADS','internet_ads', '+1 to +3','RON PAUL 2012',2,1,1,3,1,1);

INSERT INTO card (type, name, tag, bodyText, subtext,moneyCost,workerCost,minDamage,maxDamage,multiplier,weight)
VALUES ('positive', 'DOOR-to-DOOR CAMPAIGNING','door_to_door', '+4','The basis of democracy',3,3,4,4,1,4);


INSERT INTO card (type, name, tag, bodyText, subtext,moneyCost,workerCost,minDamage,maxDamage,multiplier,weight)
VALUES ('negative', 'NEGATIVE TV ADS','tv_ads_neg', '-10','Swift-boat them',5,3,10,10,1,7);

INSERT INTO card (type, name, tag, bodyText, subtext,moneyCost,workerCost,minDamage,maxDamage,multiplier,weight)
VALUES ('negative', 'NEGATIVE RADIO ADS','radio_ads_neg', '-4','Burn their ears',3,2,4,4,1,3);

INSERT INTO card (type, name, tag, bodyText, subtext,moneyCost,workerCost,minDamage,maxDamage,multiplier,weight)
VALUES ('negative', 'NEGATIVE ONLINE ADS','internet_ads_neg', '-2 to -5','opponentsux.net',2,2,2,5,1,2);

INSERT INTO card (type, name, tag, bodyText, subtext,moneyCost,workerCost,minDamage,maxDamage,multiplier,weight)
VALUES ('negative', 'PLANT A SPY','plant_spy', '-3 to -10','Deals more damage in later rounds',5,2,3,10,2,6);

INSERT INTO card (type, name, tag, bodyText, subtext,moneyCost,workerCost,minDamage,maxDamage,multiplier,weight)
VALUES ('negative', 'DIG UP OLD DIRT','dig_up_dirt', '-2x Turn','Deals twice the turn in damage',5,3,0,0,2,9);


