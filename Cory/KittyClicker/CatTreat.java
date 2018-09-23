/**
 * CatTreat is a class object to handle the moving treats and their logic along with visibility
 *
 * @author Angie, Cory
 * @version 6/1/18
 */
public class CatTreat
{
	//instance variables
    private double x;
    private int y, r;
    private int screenWidth;
    private double treatImageIndexMax, treatImageIndex;
    private double vY;

    private boolean visible;

    /**
     *
     * @author Angie, Cory
     * @param screenWidth
     * @param y height to spawn it for starting (use number larger than screen height to start at top)
     * @param vY speed to move down, this is randomized with the equation being 3 +-(vY)
     * @param r radius to make the sphere of the treat
     * @param imageIndexMax the size of the image arraylist
     */
    public CatTreat(int screenWidth, int y, double vY, int r,int imageIndexMax)
    {
        this.screenWidth = screenWidth;
        this.x = Math.random() * screenWidth; // when we spawn the treat we place it within the screen boundaries
        this.y = y;
        this.vY = vY;
        this.r = r;
        this.treatImageIndexMax = imageIndexMax;
        visible = true;
    }

    /**
     * Moves the cat treat by vY (velocity) downward and then checks if it needs to be reset an, also randomizes next spawn location and resets visibility
     * @author Cory
     *
     * @param screenHeight
     */
    public void move(int screenHeight)
    {
        y += vY;
        //if the treat is off screen + about an (1/8) of it then move it back up and make it visible and then place it randomly
        if (y > screenHeight + (screenHeight / 8))
        {
        	this.x = Math.random() * screenWidth;
        	if (x > screenWidth - 100) x = screenWidth - 100; // don't allow the image to get cut off by screen boundaries
        	y = -100;
        	this.treatImageIndex =(Math.random() * this.treatImageIndexMax);//randomize the current image index
        	this.vY = Math.random() * vY + 3; // we also slightly randomize the velocity down to make the treats gradually get more and more out of sync with eachother if they were gotten at similar times
        	visible = true;
        }
    }

    /**
     * @author Angie
     * @return the current image index
     */
    public int getTreatImageIndex()
    {
        return (int)treatImageIndex;
    }

    /**
     * @author Angie
     * @return current X position
     */
    public int getX()
    {
        return (int) x;
    }

    /**
     * @author Angie
     * @return current Y position
     */
    public int getY()
    {
        return y;
    }

    /**
     * @author Angie
     * @return current velocity downward
     */
    public int getvY()
    {
        return (int) vY;
    }

    /**
     * @author Cory
     * @return returns the radius of the circle used to draw the treat
     * @deprecated
     */
    public int getR()
    {
        return r;
    }

    /**
     * @author Angie
     * @return current visibility
     */
    public boolean isVisible()
    {

    	return visible;
    }

    /**Called when the treat gets clicked
     * @author Angie
     */
    public void clicked()
    {

    	visible = false;
    }

}
