package org.group3.game.model.card;




public class Card {

    private Integer id;
    private String type;
    private String name;
    private String bodyText;
    private String subtext;
    private int moneyCost;
    private int workerCost;
    private int minDamage;
    private int maxDamage;
    private int multiplier;
    private int weight;

    public String getSubtext() {
        return subtext;
    }

    public void setSubtext(String subtext) {
        this.subtext = subtext;
    }

    public int getMinDamage() {
        return minDamage;
    }

    public void setMinDamage(int minDamage) {
        this.minDamage = minDamage;
    }

    public int getMaxDamage() {
        return maxDamage;
    }

    public void setMaxDamage(int maxDamage) {
        this.maxDamage = maxDamage;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
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
    
    public void setWeight(int weight)
    {
    	
    	this.weight = weight;
    	
    }
    
    public int getWeight()
    {
    	
    	return weight;
    	
    }

    @Override 
    public boolean equals(Object obj){
        return (obj instanceof Card) && (this.id.equals(((Card)obj).id));
    }

    @Override
    public int hashCode(){
        return this.id;
    }
}
