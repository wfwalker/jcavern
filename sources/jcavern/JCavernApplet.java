/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

import java.awt.*;
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
	
	/** * A view of the game world */
	private WorldView				mWorldView;
	
	/** * A view of the player statistics */
	private PlayerView				mPlayerView;
	
	/** * A view of the player statistics */
	private MissionView				mMissionView;
	
	/** * A model of the game world */
	private World					mWorld;
	
	/** * A model of the game world */
	private TextArea				mLogView;
	
	/** * The representation of the player */
	private Player					mPlayer;
	
	/** * A table of messages */
	private Hashtable				mImages;

	/** * A MediaTracker to make sure the images get loaded. */
	private MediaTracker			mTracker;

	private static JCavernApplet	gApplet;
	
	public static JCavernApplet current()
	{
		return gApplet;
	}

	public static void log(String aString)
	{
		if (gApplet != null)
		{
			gApplet.privateLog(aString);
		}
	}


	/**
	 * Creates game world, player, viewers.
	 */
	public JCavernApplet()
	{
		mImages = new Hashtable();
		gApplet = this;
	}
	
	private void privateLog(String aString)
	{
		if (mLogView != null)
		{
			mLogView.append(aString);
			mLogView.append("\n");
		}
	}
	
	public Image getBoardImage(String aName)
	{
		return (Image) mImages.get(aName);
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

	Image fetchImageAndWait(URL imageURL) throws InterruptedException
	{
		Image image = getImage(imageURL);
		
		mTracker.addImage(image, 1);
		mTracker.waitForID(1);
		return image;
	}


	// Initialize the applet
	public void init()
	{
		setBackground(Color.black);
		setForeground(Color.orange);
		setFont(new Font("Monospaced", Font.PLAIN, 12));
		
		mTracker = new MediaTracker(this);

		System.out.println("jcavern applet document base " + getDocumentBase());

		try
		{	
			// get data from server
			
			mImages.put("monster", fetchImageAndWait(new URL(getDocumentBase(), "bin/images/monster.gif")));
			mImages.put("player", fetchImageAndWait(new URL(getDocumentBase(), "bin/images/player.gif")));
			mImages.put("tree", fetchImageAndWait(new URL(getDocumentBase(), "bin/images/tree.gif")));
			mImages.put("tree2", fetchImageAndWait(new URL(getDocumentBase(), "bin/images/tree2.gif")));
			mImages.put("eyeball", fetchImageAndWait(new URL(getDocumentBase(), "bin/images/eyeball.gif")));
			mImages.put("chest", fetchImageAndWait(new URL(getDocumentBase(), "bin/images/chest.gif")));
			mImages.put("chavin", fetchImageAndWait(new URL(getDocumentBase(), "bin/images/chavin.gif")));
			mImages.put("darklord", fetchImageAndWait(new URL(getDocumentBase(), "bin/images/darklord.gif")));
			mImages.put("gobbler", fetchImageAndWait(new URL(getDocumentBase(), "bin/images/gobbler.gif")));
			mImages.put("jackolantern", fetchImageAndWait(new URL(getDocumentBase(), "bin/images/jackolantern.gif")));
			mImages.put("wahoo", fetchImageAndWait(new URL(getDocumentBase(), "bin/images/wahoo.gif")));
			mImages.put("snake", fetchImageAndWait(new URL(getDocumentBase(), "bin/images/snake.gif")));

			MonsterFactory.loadPrototypes(new URL(getDocumentBase(), "bin/monster.dat"));
			Treasure.loadPrototypes(new URL(getDocumentBase(), "bin/treasure.dat"));
	
			// Create a player and a view of the player
			mPlayer  = new Player("Bill");
			mPlayerView = new PlayerView(mPlayer);
			mPlayer.setMission(MonsterFactory.createMission(mPlayer));
			mMissionView = new MissionView(mPlayer.getMission());
	
			// Create a world  and a view of the world
			mWorld = new World();
			mWorldView = new WorldView(mWorld);
	
			mLogView = new TextArea("Welome to JCavern\n", 5, 60, TextArea.SCROLLBARS_NONE);
			mLogView.setEditable(false);
			mLogView.setBackground(Color.black);
			mLogView.setForeground(CavernOrange);
			
			mWorldView.setSize(300, 300);		
			add(mWorldView);
			mWorld.addObserver(mWorldView);
			
			//mPlayerView.setSize(500, 50);		
			add(mPlayerView);
			mPlayer.addObserver(mPlayerView);
			
			mMissionView.setSize(500, 50);		
			add(mMissionView);
			mPlayer.getMission().addObserver(mMissionView);
			
			add(mLogView);

			mWorld.populateFor(mPlayer);
		}
		catch(JCavernInternalError jcie)
		{
			System.out.println("JCaverApplet.init internal error " + jcie);
		}
		catch(InterruptedException ie)
		{
			System.out.println("JCaverApplet.init interrupted exception " + ie);
		}
		catch(MalformedURLException mue)
		{
			System.out.println("JCaverApplet.init " + mue);
		}
	}

}
