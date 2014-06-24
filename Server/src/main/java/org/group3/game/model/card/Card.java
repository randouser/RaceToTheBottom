package org.group3.game.model.card;


import java.io.Serializable;

public class Card implements Serializable{

    private Integer id;
    private String type;
    private String name;
    private String bodyText;
    private int workerCost;
    private int moneyCost;
    private int damage;

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBodyText() {
        return bodyText;
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

    public int getWorkerCost() {
        return workerCost;
    }

    public void setWorkerCost(int workerCost) {
        this.workerCost = workerCost;
    }

    public int getMoneyCost() {
        return moneyCost;
    }

    public void setMoneyCost(int moneyCost) {
        this.moneyCost = moneyCost;
    }
}
