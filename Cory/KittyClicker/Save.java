import java.util.Calendar;
import java.io.Serializable;
/**
 * This object is used to save stats about the player, it is completely serializable and loads properly, it stores the current time when ever save() is called
 * 
 * @author Cory 
 * @version 6/1/18
 */
public class Save implements Serializable
{
	private static final long serialVersionUID = 1L;
	private int[] buildCounts;
    private double money;
    private Calendar calendar;
    private long milis;
    public Save(int[] buildCounts, double money)
    {
        calendar = Calendar.getInstance();
        //calendar.set(1970, 01, 01);
        milis = calendar.getTimeInMillis();
        this.buildCounts = buildCounts;
        this.money = money;
    }
    
    public void resetTime() 
    {
        milis = calendar.getTimeInMillis();
    }
    
    public double getMoney() 
    {
        return money;
    }
    
    public int[] getBuildCounts() 
    {
        return buildCounts;
    }
    
    public long getTime() 
    {
        return milis;
    }
    
    @Override
    public String toString() 
    {
    	String retn = "Time: " + milis + "\nMoney: " + money + "\nBuildings: \n";
    	for(int i = 0; i < buildCounts.length; i++) 
    	{
    		retn += i + ": " + buildCounts[i] + "\n";
    	}
		return retn;
    }
    
}
