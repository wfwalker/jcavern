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
	
	/** * A view of the player statistics */
	private MissionView			mMissionView;
	
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
		if (gLogView != null)
		{
			gLogView.append(aString);
			gLogView.append("\n");
		}
	}

	/**
	 * Installs KeyListener for keyboard commands.
	 */
	public void start()
	{
		mWorldView.requestFocus();
        mWorldView.addKeyListener(new KeyboardCommandListener(mWorld, mWorldView, mPlayer, mMissionView));
	}

	// Get applet information
	public String getAppletInfo() 
	{
		return "JCavern 0.0, Bill Walker";
	}

	// Initialize the applet
	public void init()
	{
		setBackground(Color.black);
		setForeground(Color.orange);
		setFont(new Font("Monospaced", Font.PLAIN, 12));

		System.out.println("jcavern applet document base " + getDocumentBase());

		try
		{
			MonsterFactory.loadPrototypes(new URL(getDocumentBase(), "./monster.dat"));
			Treasure.loadPrototypes(new URL(getDocumentBase(), "./treasure.dat"));
	
			// Create a player and a view of the player
			mPlayer  = new Player("Bill");
			mPlayerView = new PlayerView(mPlayer);
			mPlayer.setMission(MonsterFactory.createMission(mPlayer));
			mMissionView = new MissionView(mPlayer.getMission());
	
			// Create a world  and a view of the world
			mWorld = new World();
			mWorld.populateFor(mPlayer);
			mWorldView = new WorldView(mWorld);
	
			gLogView = new TextArea("Welome to JCavern", 5, 60, TextArea.SCROLLBARS_NONE);
			gLogView.setEditable(false);
			gLogView.setBackground(Color.black);
			gLogView.setForeground(Color.orange);
			
			mWorldView.setSize(500, 220);		
			add(mWorldView);
			mWorld.addObserver(mWorldView);
			
			mPlayerView.setSize(500, 50);		
			add(mPlayerView);
			mPlayer.addObserver(mPlayerView);
			
			mMissionView.setSize(500, 50);		
			add(mMissionView);
			mPlayer.getMission().addObserver(mMissionView);
			
			add(gLogView);
			
		}
		catch(JCavernInternalError jcie)
		{
			System.out.println("JCaverApplet.init internal error " + jcie);
		}
		catch(MalformedURLException mue)
		{
			System.out.println("JCaverApplet.init " + mue);
		}
	}

}
