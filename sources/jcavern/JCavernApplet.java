/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

import jcavern.ui.*;
import jcavern.thing.*;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.Hashtable;
import java.awt.event.*;
import java.applet.*;

/**
 * JCavernApplet is the launcher for the java version of cavern and glen.
 *
 * @author	Bill Walker
 * @version	$Id$
 */
public class JCavernApplet extends Applet
{
	/** * The official PLATO orange color, more or less. */
	public static final Color		CavernOrange = new Color(0xFF, 0x66, 0x00);
	
	/** * Names of image files containing pictures of monsters. */
	public static final String		MonsterImageNames[] = {
		"crab", "snail", "jubjub", "blob", "hoplite", "guy", "monster", "trex",
		"demon", "wraith", "rajah", "grump", "larry", "eyeball", "ugly", "darklord", "chavin",
		"wahoo", "gobbler", "scary", "jackolantern", "hippo", "alien", "forkman", "snake" };
	
	/** * Names of image files containing pictures of non-monsters. */
	public static final String		OtherImageNames[] = {
		"player", "chest", "tree", "tree2", "castle", "splat" };

	/** * A table of messages */
	private Hashtable				mImages;

	/** * A MediaTracker to make sure the images get loaded. */
	private MediaTracker			mTracker;

	/**
	 * Displays the home card.
	 *
	 * @param		inPlayer				the Player currently in use
	 * @exception	JCavernInternalError	trouble displaying the card
	 */
	public void displayHomeCard(Player inPlayer) throws JCavernInternalError
	{
		IndexCard anIndex = new IndexCard(this, inPlayer);
		anIndex.show();
	}

	/**
	 * Creates game world, player, viewers.
	 */
	public JCavernApplet()
	{
		mImages = new Hashtable();
		//gApplet = this;
		mTracker = new MediaTracker(this);
	}
		
	/**
	 * Retrieves a board image.
	 *
	 * @param		aName					the name of the image to retrieve
	 * @return								a non-null Image
	 * @exception	JCavernInternalError	the image could not be retrieved
	 */
	public Image getBoardImage(String aName) throws JCavernInternalError
	{
		if (! mImages.containsKey(aName))
		{
			throw new JCavernInternalError("no such image " + aName + " in dictionary " + mImages);
		}
		
		return (Image) mImages.get(aName);
	}
	
	/**
	 * Installs KeyListener for keyboard commands.
	 */
	public void start()
	{
	}

	/**
	 * Returns information about the applet.
	 *
	 * @return	a non-null String
	 */
	public String getAppletInfo() 
	{
		return "JCavern 0.0, Bill Walker";
	}

	/**
	 * Loads data files from the web server.
	 * These include monster prototypes, treasure prototypes, monster images, and other images.
	 *
	 * @exception	JCavernInternalError	could not load data
	 */
	private void loadDataFromServer() throws JCavernInternalError
	{
		try
		{
			MonsterFactory.loadPrototypes(new URL(getDocumentBase(), "bin/monster.dat"));
			Treasure.loadPrototypes(new URL(getDocumentBase(), "bin/treasure.dat"));
		}
		catch(MalformedURLException mue)
		{
			System.out.println("JCaverApplet.init " + mue);
			throw new JCavernInternalError("Can't load monster and treasure data");
		}
		
		loadArrayOfImages(MonsterImageNames);
		loadArrayOfImages(OtherImageNames);
	}
	
	/**
	 * Loads an array of images.
	 *
	 * @param		imageNames				an array of non-null Strings containing the names of image files.
	 * @exception	JCavernInternalError	could not load data
	 */
	private void loadArrayOfImages(String[] imageNames) throws JCavernInternalError
	{		
		for (int index = 0; index < imageNames.length; index++)
		{
			try
			{
				URL imageURL = new URL(getDocumentBase(), "bin/images/" + imageNames[index] + ".gif");
	
				Image image = getImage(imageURL);
				
				mTracker.addImage(image, 1);
				mTracker.waitForID(1);
				
				if (mTracker.isErrorID(index))
				{
					throw new JCavernInternalError("Can't load image " + mTracker.getErrorsID(index));
				}
				
				//System.out.println("name " + imageNames[index] + " image = " + image);

				mImages.put(new String(imageNames[index]), image);
			}
			catch(InterruptedException ie)
			{
				System.out.println("JCaverApplet.init interrupted exception " + ie);
				throw new JCavernInternalError("Can't load monster images");
			}
			catch(MalformedURLException mue)
			{
				System.out.println("JCaverApplet.init " + mue);
				throw new JCavernInternalError("Can't load monster images");
			}
		}
	}
	
	/**
	 * Initialize the applet
	 */
	public void init()
	{
		try
		{
			loadDataFromServer();
		
			IndexCard anIndex = new IndexCard(this, null);
			anIndex.show();
		}
		catch (JCavernInternalError jcie)
		{
			System.out.println("Can't initialize applet!");
		}
	}
}
