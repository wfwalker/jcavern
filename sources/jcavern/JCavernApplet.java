/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

import java.awt.*;
import java.net.*;
import java.awt.event.*;
import java.applet.*;

/**
 * JCavernApplet is the launcher for the java version of cavern and glen.
 *
 * @author	Bill Walker
 */
public class JCavernApplet extends Applet
{
	/** * A view of the game world */
	private WorldView			mWorldView;
	
	/** * A view of the player statistics */
	private PlayerView			mPlayerView;
	
	/** * A model of the game world */
	private World				mWorld;
	
	/** * The representation of the player */
	private Player				mPlayer;
	
	private static TextArea		gLogView;

	/**
	 * Creates game world, player, viewers.
	 */
	public JCavernApplet()
	{
	}
	
	public static void log(String aString)
	{
		gLogView.append(aString);
		gLogView.append("\n");
	}

	/**
	 * Installs KeyListener for keyboard commands.
	 */
	public void start()
	{
		mWorldView.requestFocus();
        mWorldView.addKeyListener(new KeyboardCommandListener(mWorld, mPlayer));
	}

	// Get applet information
	public String getAppletInfo() 
	{
		return "JCavern 0.0, Bill Walker";
	}

	// Initialize the applet
	public void init()
	{
		try
		{
			MonsterFactory.loadPrototypes(new URL(getDocumentBase(), "./monster.dat"));
	
			// Create a player and a view of the player
			mPlayer  = new Player("Bill");
			mPlayerView = new PlayerView(mPlayer);
	
			// Create a world  and a view of the world
			mWorld = new World();
			mWorld.placeRandomTrees();
			mWorld.placeWorthyOpponents(mPlayer, 10);
			mWorldView = new WorldView(mWorld);
	
			gLogView = new TextArea(5, 80);
			
			mWorldView.setSize(500, 500);		
			add(mWorldView);
			mWorld.addObserver(mWorldView);
			
			mPlayerView.setSize(500, 50);		
			add(mPlayerView);
			mPlayer.addObserver(mPlayerView);
			
			add(gLogView);
			
			// Put the player in the world
			mWorld.place(mWorld.getRandomEmptyLocation(), mPlayer);
		}
		catch(ThingCollisionException tce)
		{
			System.out.println("JCaverApplet.init " + tce);
		}
		catch(MalformedURLException mue)
		{
			System.out.println("JCaverApplet.init " + mue);
		}
	}

}
