/**
 * A building is a purchasable item in clickergame used to make more money per seccond
 *
 * @author Cory, Angie
 * @version 6/1/18
 */
public class Building
{
	//instance variables
    private int amount; 
    private double scorePerSec, price;
    private String name;
    private double startPrice;
    private String description;
    
    /**
     * @author Angie
     * @param Name of building
     * @param ScorePerSec to be given to player
     * @param Price of building, currently no customizable curve
     */
    public Building(String name, double scorePerSec, double price) 
    {
    	amount = 0;
    	this.name = name;
    	this.scorePerSec = scorePerSec;
    	this.price = price;
    	this.startPrice = price;
    	description = "NO DESCRIPTION SET";
    }
    /**
     * @author Angie
     * @param Name of building
     * @param ScorePerSec to be given to player
     * @param Price of building, currently no customizable curve
     * @param description of the building (optional)
     */
    public Building(String name, double scorePerSec, double price, String description) 
    {
    	amount = 0;
    	this.name = name;
    	this.scorePerSec = scorePerSec;
    	this.price = price;
    	this.startPrice = price;
    	this.description = description;
    }
    
    /**
     * @author Angie
     * @return string of the description along with the stats of the building
     */
    public String getDescription() 
    {
		return description + getStats() + "\n\n\n\nPush space to go back to the game menu.";
    }
    
    /**
     * @author Angie
     * @return a string of the stats of the building such as price, sps, and how many the player has right now.
     */
    private String getStats() {
		return "\nTreats Per Seccond: " + scorePerSec + "\nPrice: " + price + "\nAmount Owned: " + amount + "\n";
	}
	/**
     * @author Cory
     * buys a building, increments the amount you own by one and then adjusts the price by 1 + 1%
     */
    public void buy() 
    {
    	amount++;
    	priceUp();
    }
    
    /**
     * @author Cory
     * adds the price up by its equation defined here
     * 
     */
    public void priceUp() 
    {
    	
    	price+= (price * 0.01) + (scorePerSec * 4) + 1;
    }
    
    /**
     * @author Angie
     * returns the score per second in double
     * @return double
     */
    public double getSPS() 
    {
    	return scorePerSec * amount;
    }
    
    /**
     * @author Angie
     * @return double
     */
    public int getAmount() 
    {
    	return amount;
    }
    
    /**
     * @author Angie
     * @return the name of the building
     */
    public String getName() 
    {
    	return name;
    }
    
    /**
     * @author Angie
     * @return the price rounded to two decimal places
     */
    public double getPrice() 
    {
        return roundTwoPlaces(price);
    }
    
    /**
     * Same method as in clicker game, just reused
     * @author Cory
     * @param number to be rounded
     * @return rounded number
     */
    public double roundTwoPlaces(double num) 
    {
        num *= 100;
        num = (int)num;
        num /= 100;
        return num;
    }
    
    
    /**
     * sets the amount of this building we have and adjusts the price of the building acordingly
     * @param amount of buildings to have
     */
    public void setAmount(int x) 
    {
    	price = startPrice;
    	amount = x;
    	for(int i = 0; i < x; i++) 
    	{
    		priceUp();
    	}
    	
    }
    
}