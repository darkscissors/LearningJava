package Cory.KittyClicker;
/**
 * Clickergame is the main classfile of this game, it handles money and rendering the images and text along with keeping time.
 * 
 * @author Cory, Angie
 * @version 6/1/18
 */

//import java.io.FileInputStream; 
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;  
import javafx.scene.input.KeyCode;
//import javafx.animation.Animation;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ClickerGame extends Application {
	// private instance variables
	private final int screenWidth, screenHeight;
	private double money;

	// has the player released the space bar (anti hold protection)
	boolean hasReleased = true;

	// cat treats arraylist
	private ArrayList<CatTreat> treats;

	// private String filePath;
	// private Image image1, image2;
	private ArrayList<Image> treatImages;
	private ArrayList<Image> cats;

	// private Building kitty;

	private ArrayList<Building> buildings;

	// what score per sec has the player reached so far in booleans
	private boolean SPSReach[];

	private double SpaceCat;
	private final Image spaceCat;

	private int treatsAllowed;

	private int[] buildCounts;
	private int[] newBuildCounts;

	private SaveThing save;

	private double cookiesAdded;

	private Calendar calendar; // used for calculating time when the game is open to add treats then player
	// would have gotten if they had it open the whole time after saving and
	// quitting

	private long milis;

	private boolean inMenuState;
	private int buildingSelected;
	private boolean isShift;

	/**
	 * Fat constructor incoming No inputs but it adds all necessary buildings to the
	 * arraylist and images too, you can add more images from here for treats and
	 * they will work properly with no other setup Buildings need to be added to a
	 * key in start() and then displayed in run() Screenwidth and height can both be
	 * changed here and all(that i know of) code will scale to it not including text
	 * and images
	 * 
	 * @author Angie, Cory
	 */
	public ClickerGame() {

		save = new SaveThing();

		// initialize instance variables

		// variables for use in game and scene
		screenWidth = 720;
		screenHeight = 480;
		money = 0;

		// treats arraylist init and max treats allowed on screen exclusive
		treats = new ArrayList<CatTreat>();
		treatsAllowed = 10;
		// variable noting whether we have added a treat to the arraylist yet for each
		// threshold
		SPSReach = new boolean[treatsAllowed];

		// init building arraylist and add buildings to it
		buildings = new ArrayList<Building>();
		// add buildings to the building arraylist
		buildings.add(new Building("Kitty", .1, 5, "A kitty that goes and finds treats and brings them back to you."));
		buildings.add(new Building("Loaf Mode", 2, 80,
				"A cat who sits in 'loaf mode' and chemically coalesceing cat treats."));
		buildings.add(new Building("Power Baps", 4, 500,
				"When a cat gets very mad, they 'power bap' someone, as in they hit them really fast. \nThis cat does just that to "
						+ "random people on the street and then brings you their treats back."));
		buildings.add(new Building("Wizard Kitty", 6, 2000,
				"A cat who magically sources treats out of thin air, he makes you quite a bit of treats but asks for some up front."));
		buildings.add(new Building("Keyboard Kitty", 8, 12000,
				"A cat who plays the keyboard for passers by and asks for tips in a jar, he makes many treats because he is a good kitty."));
		buildings.add(new Building("Big ole'  kitter", 10, 100000,
				"A large cat who mauls people for their treats, he barges into stores and takes what he wants and brings you back the loot."));

		// init image arraylist
		treatImages = new ArrayList<Image>();
		// image1 = new Image(getClass().getResourceAsStream("treatimage.png"));

		// image arraylist to keep them usable

		/** treat Images **/
		treatImages.add(new Image(getClass().getResourceAsStream("treat/treatimage.png")));
		treatImages.add(new Image(getClass().getResourceAsStream("treat/FTreat.png")));
		treatImages.add(new Image(getClass().getResourceAsStream("treat/RTreat.png")));
		treatImages.add(new Image(getClass().getResourceAsStream("treat/STreat.png")));
		treatImages.add(new Image(getClass().getResourceAsStream("treat/TTreat.png")));

		// init cat images
		cats = new ArrayList<Image>();
		/** cats Images **/
		cats.add(new Image(getClass().getResourceAsStream("cats/kitty.jpg")));
		cats.add(new Image(getClass().getResourceAsStream("cats/loafcat.jpg")));
		cats.add(new Image(getClass().getResourceAsStream("cats/powerbaps.jpg")));
		cats.add(new Image(getClass().getResourceAsStream("cats/wizardkitty.jpg")));
		cats.add(new Image(getClass().getResourceAsStream("cats/keyboardkitty.jpg")));
		cats.add(new Image(getClass().getResourceAsStream("cats/lion.jpg")));

		// cat to appear when space is pushed
		spaceCat = new Image(getClass().getResourceAsStream("cats/spacecat.jpg"));

		SpaceCat = 0;

		buildCounts = new int[buildings.size()];
		newBuildCounts = new int[buildings.size()];

		// we instance the calendar as it is an abstract class and can not make it an
		// object
		calendar = Calendar.getInstance();
		// calendar.set(1970, 01, 01);
		// this variable is used for loading the save, it is the number of secconds
		// since 1970 when we open the game
		milis = calendar.getTimeInMillis() / 1000;

		buildingSelected = 0;
	}

	@Override
	/**
	 * handles the start of canvas, graphicscontext, and event listeners such as key
	 * and mouse clicks
	 */
	public void start(Stage stage) throws FileNotFoundException {
		Canvas canvas = new Canvas(screenWidth, screenHeight);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		Timeline tl = new Timeline(new KeyFrame(Duration.millis(16 + (2 / 3)), e -> run(gc))); // we use 16 + (2/3)
		// because this is the
		// amount of
		// milliseconds needed
		// between each frame to
		// create a perfect
		// 59.9999 fps,
		// essentially 60 fps
		tl.setCycleCount(Animation.INDEFINITE);
		canvas.setFocusTraversable(true);

		// handle mouse and key events

		canvas.setOnKeyPressed(e -> {
			// set hasreleased to false when the player has pushed the space bar, to prevent
			// hold down (see onkeyreleased)
			if (e.getCode() == KeyCode.SPACE) {
				if (!inMenuState) {
					if (hasReleased) {
						money++;
						SpaceCat += 100;
						hasReleased = false;
					}
				} else {
					inMenuState = false;
				}
			}

			if (e.getCode() == KeyCode.SHIFT) {
				isShift = true;
			}

			// code for buying buildings using the arraylist to hold them and the buy method
			// to make sure the purchase is valid and possible
			if (e.getCode() == KeyCode.Q) {
				if (!isShift) {
					buy(buildings.get(0));
				} else {
					buildingSelected = 0;
					inMenuState = true;
				}
			}
			if (e.getCode() == KeyCode.W) {
				if (!isShift) {
					buy(buildings.get(1));
				} else {
					buildingSelected = 1;
					inMenuState = true;
				}
			}
			if (e.getCode() == KeyCode.E) {

				if (!isShift) {
					buy(buildings.get(2));
				} else {
					buildingSelected = 2;
					inMenuState = true;
				}
			}
			if (e.getCode() == KeyCode.R) {

				if (!isShift) {
					buy(buildings.get(3));
				} else {
					buildingSelected = 3;
					inMenuState = true;
				}
			}
			if (e.getCode() == KeyCode.T) {

				if (!isShift) {
					buy(buildings.get(4));
				} else {
					buildingSelected = 4;
					inMenuState = true;
				}
			}
			if (e.getCode() == KeyCode.Y) {

				if (!isShift) {
					buy(buildings.get(5));
				} else {
					buildingSelected = 5;
					inMenuState = true;
				}
			}

			if (e.getCode() == KeyCode.ESCAPE) {
				System.out.println("Saving game...");
				save.save(buildCounts, money);
				System.out.println("Saved game.");
				System.out.println(save.getSave());
			}

			if (e.getCode() == KeyCode.F1) {
				System.out.println("Loading game save...");
				// to load, first we get how many of each building we have and store it
				newBuildCounts = save.getSave().getBuildCounts();
				// next we set our money to the amount of money we had when we saved
				money = save.getSave().getMoney();

				// then we go through our buildings and set the amount we have according to the
				// saved building count
				for (int i = 0; i < newBuildCounts.length; i++) {

					buildings.get(i).setAmount(newBuildCounts[i]);

				}

				// next we get the amount of secs since 1970 via java.util.calendar from when we
				// took the save
				long secs = ((save.getSave().getTime()) / 1000);
				// save.getSave().resetTime();

				// long secs1 = (save.getSave().getTime()) / 1000;

				// System.out.println(secs + " " + milis);
				// i use a variable here just so i can display it to the user at the bottom of
				// the screen
				// we take the amount of treats we get per seccond, and multiply it by the
				// number of secconds the game has been closed since saving
				cookiesAdded = Math.abs(getSimulateMoneyAdd() * (secs - milis));
				money += cookiesAdded;
				System.out.println("Loaded game.");
				System.out.println(save.getSave());
			}

			// cheater!
			if (e.getCode() == KeyCode.END) {

				money += 1000000;
			}
		});

		// set hasReleased to true when the player released space bar
		canvas.setOnKeyReleased(e -> {
			if (e.getCode() == KeyCode.SPACE) {

				hasReleased = true;

			}

			if (e.getCode() == KeyCode.SHIFT) {

				isShift = false;

			}

		});

		// if the player clicks and their x and y coordinates collide with a treat then
		// they get a 10% boost to their treats (money)
		canvas.setOnMouseClicked(e -> {
			double x = e.getX();
			double y = e.getY();
			if (!inMenuState) {
				for (int i = 0; i < treats.size(); i++) {
					double tX = treats.get(i).getX();
					double tY = treats.get(i).getY();
					// treat 1 x = 200 y = 300
					if (x >= tX && x <= tX + 100) {
						if (y >= tY && y <= tY + 100) {
							treats.get(i).clicked();
							money += (getSimulateMoneyAdd() * 5);
						}
					}
				}
			}
			// do something
		});

		stage.setTitle("Kitty Clicker");
		stage.setScene(new Scene(new StackPane(canvas)));
		stage.show();
		tl.play();

	}

	/**
	 * run every 1/60 seconds managing almost all of the game logic
	 * 
	 * @param gc
	 *            GraphicsContext
	 */
	private void run(GraphicsContext gc) {

		if (!inMenuState) {

			// color for background
			gc.setFill(Color.WHITE);
			gc.fillRect(0, 0, screenWidth, screenHeight);

			// space bar interaction with an image
			if (SpaceCat > 0) {
				gc.drawImage(spaceCat, 0, 0, SpaceCat, SpaceCat);
				SpaceCat -= ((SpaceCat * 0.001) + 2);
			}

			// text for treats (money)
			gc.setFill(Color.GREY);
			gc.fillText("Treats: " + roundTwoPlaces(money), 25, 25);

			// we use an integer here because I'm lazy and didn't want to make a for loop but
			// also didn't want to hand write all of these texts, it happens to work
			// perfectly with our buildings arraylist and images arraylist
			int lazy = 0;
			if (money >= buildings.get(lazy).getPrice()) {
				gc.setFill(Color.GREEN);
			} else {
				gc.setFill(Color.RED);
			}
			gc.fillText("Q:\t" + buildings.get(lazy).getName() + "(" + buildings.get(lazy).getPrice() + ")" + ":\t"
					+ buildings.get(lazy).getAmount(), 25, 50);
			gc.drawImage(cats.get(lazy), 600, 0, 100, 100);
			lazy++;
			if (money >= buildings.get(lazy).getPrice()) {
				gc.setFill(Color.GREEN);
			} else {
				gc.setFill(Color.RED);
			}
			gc.fillText("W:\t" + buildings.get(lazy).getName() + "(" + buildings.get(lazy).getPrice() + ")" + ":\t"
					+ buildings.get(lazy).getAmount(), 25, 75);
			gc.drawImage(cats.get(lazy), 600, 100, 100, 100);
			lazy++;
			if (money >= buildings.get(lazy).getPrice()) {
				gc.setFill(Color.GREEN);
			} else {
				gc.setFill(Color.RED);
			}
			gc.fillText("E:\t" + buildings.get(lazy).getName() + "(" + buildings.get(lazy).getPrice() + ")" + ":\t"
					+ buildings.get(lazy).getAmount(), 25, 100);
			gc.drawImage(cats.get(lazy), 600, 200, 100, 100);
			lazy++;
			if (money >= buildings.get(lazy).getPrice()) {
				gc.setFill(Color.GREEN);
			} else {
				gc.setFill(Color.RED);
			}
			gc.fillText("R:\t" + buildings.get(lazy).getName() + "(" + buildings.get(lazy).getPrice() + ")" + ":\t"
					+ buildings.get(lazy).getAmount(), 25, 125);
			gc.drawImage(cats.get(lazy), 600, 300, 100, 100);
			lazy++;
			if (money >= buildings.get(lazy).getPrice()) {
				gc.setFill(Color.GREEN);
			} else {
				gc.setFill(Color.RED);
			}
			gc.fillText("T:\t" + buildings.get(lazy).getName() + "(" + buildings.get(lazy).getPrice() + ")" + ":\t"
					+ buildings.get(lazy).getAmount(), 25, 150);
			gc.drawImage(cats.get(lazy), 600, 400, 100, 100);
			lazy++;
			if (money >= buildings.get(lazy).getPrice()) {
				gc.setFill(Color.GREEN);
			} else {
				gc.setFill(Color.RED);
			}
			gc.fillText("Y:\t" + buildings.get(lazy).getName() + "(" + buildings.get(lazy).getPrice() + ")" + ":\t"
					+ buildings.get(lazy).getAmount(), 25, 175);
			gc.drawImage(cats.get(lazy), 500, 400, 100, 100);

			// treat code only needs to run if we have more than 1 treat per second
			if (getSimulateMoneyAdd() >= 1) {

				// here we go through each score per second and check if we have hit that
				// threshold before with this for loop and if we have then we do nothing, else
				// we add another treat to the arraylist
				for (int i = 1; i <= getSimulateMoneyAdd(); i++) {
					if (i < SPSReach.length) {
						if (!SPSReach[i]) {
							SPSReach[i] = true;
							addTreat();
						}
					}
				}

				// gc.setFill(Color.BROWN);

				// here we go through each treat in our arraylist and move them on each frame
				// and draw them only if they are supposed to be visible
				for (int i = 0; i < treats.size(); i++) {
					treats.get(i).move(screenHeight);
					if (treats.get(i).isVisible()) {
						gc.drawImage(treatImages.get(treats.get(i).getTreatImageIndex()), treats.get(i).getX(),
								treats.get(i).getY(), 100, 100);

						// uncomment to debug treats
						// gc.fillText("treat #" + i + " " + treats.get(i).getX() + "," +
						// treats.get(i).getY() + " index: " + treats.get(i).getTreatImageIndex(),
						// treats.get(i).getX(), treats.get(i).getY());
					}
				}
				// gc.fillOval(treat1.getX(), treat1.getY(), 100, 100);
				// treatView.setX(treat1.getX());
				// treatView.setY(treat1.getY());

			}
			// here we print our treats per second
			gc.setFill(Color.GREY);
			gc.fillText(roundTwoPlaces(getSimulateMoneyAdd()) + " treats per second", 250, 25);
			// here we add our money by our treats per second / 60, because this whole
			// method runs 60 HZ (see canvas code in start method)
			money += (getSimulateMoneyAdd()) / 60;

			// sync up the buildings to build counts to potentially save
			for (int i = 0; i < buildings.size(); i++) {
				buildCounts[i] = getCounts()[i];

			}

			gc.setFill(Color.BLACK);
			gc.fillText("ESC to save \nF1 to load\n" + "While you were away, you made: " + cookiesAdded + " treats.",
					235, 440);
			gc.fillText("Hold shift and buy a building \nto see its description.", 0, 440);

		} else {

			menuStateDisplay(gc, buildingSelected);

		}

	}

	/**
	 * Displays an alternate menu to the normal game
	 * 
	 * @param gc
	 * @param buildingSelected
	 */
	public void menuStateDisplay(GraphicsContext gc, int buildingSelected) {
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, screenWidth, screenHeight);
		gc.setFill(Color.WHITE);
		gc.drawImage(cats.get(buildingSelected), 0, 0, 100, 100);
		gc.fillText(buildings.get(buildingSelected).getDescription(), 10, 115);
	}

	/**
	 * @author Angie
	 * @return Amount of money to add to the counter per second
	 */
	public double getSimulateMoneyAdd() {
		double moneyAdd = 0;
		// we go through each building and get its SPS (score per sec) and add it to a
		// return variable
		for (int i = 0; i < buildings.size(); i++) {
			moneyAdd += buildings.get(i).getSPS();
		}

		return moneyAdd;
	}

	/**
	 * used when saving the game to make sure we have the save synced with our
	 * current gamestate
	 * 
	 * @return the building counts of each building in an int[]
	 */
	public int[] getCounts() {
		int retn[] = new int[buildings.size()];
		for (int i = 0; i < buildings.size(); i++) {
			retn[i] = buildings.get(i).getAmount();

		}
		return retn;
	}

	/**
	 * @author Cory
	 * @param Building
	 *            that is valid
	 * @return true if the player has enough money to buy said building
	 */
	public boolean canBuy(Building b) {
		if (money >= b.getPrice())
			return true;
		else
			return false;
	}

	/**
	 * Buys the building specified and deducts needed money if canBuy() returns true
	 * 
	 * @author Angie
	 * @param Building
	 */
	public void buy(Building b) {
		if (canBuy(b)) {
			money -= b.getPrice();
			b.buy();
		}
	}

	/**
	 * @author Cory
	 * @param double
	 *            (ex 11.5264)
	 * @return double (ex 11.52)
	 */
	public double roundTwoPlaces(double num) {
		// num = 11.5264;
		num *= 100;
		// 1152.64
		num = (int) num;
		// 1152
		num /= 100;
		// 11.52

		return num;
	}

	/**
	 * @author Cory This method adds a treat to the treats arraylist of standard
	 *         params
	 */
	public void addTreat() {
		treats.add(new CatTreat(screenWidth, -150, 2, 50, treatImages.size()));
	}

	/**
	 * take a guess
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		Application.launch(args);
	}
}