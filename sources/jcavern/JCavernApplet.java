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
	public static final Color		CavernOrange = new Color(0xFF, 0x66, 0x00);
	
	public static final String		MonsterImageNames[] = { "snail", "blob", "hoplite", "guy", "monster", "trex", "demon", "wraith", "rajah", "eyeball", "ugly", "darklord", "chavin", "wahoo", "gobbler", "scary", "jackolantern", "snake" };
	public static final String		OtherImageNames[] = { "player", "chest", "tree", "tree2", "castle", "splat" };

	/** * The current player. */
	private Player					mCurrentPlayer;
	
	/** * A table of messages */
	private Hashtable				mImages;

	/** * A MediaTracker to make sure the images get loaded. */
	private MediaTracker			mTracker;

	/** * The current applet */
	private static JCavernApplet	gApplet;

	private static JCavernApplet current()
	{
		return gApplet;
	}
	
	public static void displayHomeCard()
	{
		try
		{
			if (current() != null)
			{
				current().privateDisplayHomeCard();
			}
		}
		catch (JCavernInternalError jcie)
		{
			System.out.println("can't display home card " + jcie);
		}
	}
	
	private void privateDisplayHomeCard() throws JCavernInternalError
	{
		IndexCard anIndex = new IndexCard(current(), mCurrentPlayer);
		anIndex.show();
	}
	
	public static void setPlayer(Player aPlayer)
	{
		if (current() != null)
		{
			current().privateSetPlayer(aPlayer);
		}
	}
	
	private void privateSetPlayer(Player aPlayer)
	{
		mCurrentPlayer = aPlayer;
					
		System.out.println("current player " + mCurrentPlayer + " is dead " + mCurrentPlayer.isDead());
	}

	/**
	 * Creates game world, player, viewers.
	 */
	public JCavernApplet()
	{
		mImages = new Hashtable();
		gApplet = this;
		mTracker = new MediaTracker(this);
	}
	
	public static Image getBoardImage(String aName) throws JCavernInternalError
	{
		if (current() != null)
		{
			return current().privateGetBoardImage(aName);
		}
		else
		{
			return null;
		}
	}
	
	private Image privateGetBoardImage(String aName) throws JCavernInternalError
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

	// Get applet information
	public String getAppletInfo() 
	{
		return "JCavern 0.0, Bill Walker";
	}

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
