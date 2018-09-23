package Cory.KittyClicker;

/**
 * @author Cory
 * @version 6/1/18
 */
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
/**
 * The save thing class is to be used with the save class also to save the game, currently it can save the buildings you have along with the amount of money you have
 * It saves it to a file called "save.ser" in the root dirrectory where the game is
 */
public class SaveThing {
	
	/**
	 * Takes no inputs and creates the savething object for use later
	 */
    public SaveThing() 
    {
    	
	}
    
    /**
     * Saves the build counts to save.ser in root
     * @param buildCounts array
     */
    public void save(int[] buildCounts, double money) 
    {
    	try {
    	FileOutputStream fos = new FileOutputStream("save.ser"); 
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(new Save(buildCounts, money));
		oos.close();


	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
    }
    
    /**
     * returns the save after reading it and casting it to an object save
     * @return
     */
    public Save getSave() 
    {
    	Save save;
    	FileInputStream fis = null;
        ObjectInputStream in = null;
        try {
            fis = new FileInputStream("save.ser");
            in = new ObjectInputStream(fis);
            //bCs = (int[]) in.readObject();
            save =((Save)in.readObject());
            in.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            save = null;
        }
        
        return save;
    }

	
}