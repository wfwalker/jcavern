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
	public static final Color		CavernOrange = new Color(0xFF, 0x99, 0x00);
	
	/** * A view of the game world */
	private WorldView				mWorldView;
	
	/** * A view of the player statistics */
	private PlayerView				mPlayerView;
	
	/** * A view of the player statistics */
	private MissionView				mMissionView;
	
	/** * A model of the game world */
	private World					mWorld;
	
	/** * The representation of the player */
	private Player					mPlayer;
	
	/** * A text view list of messages. */
	private static TextArea			gLogView;
	
	/** * A table of messages */
	private static Hashtable		gImages;

	private static JCavernApplet	gApplet;

	/**
	 * Creates game world, player, viewers.
	 */
	public JCavernApplet()
	{
		gImages = new Hashtable();
		gApplet = this;
	}
	
	public static void log(String aString)
	{
		if (gLogView != null)
		{
			gLogView.append(aString);
			gLogView.append("\n");
		}
	}
	
	public static JCavernApplet current()
	{
		return gApplet;
	}

	public Image getBoardImage(String aName)
	{
		return (Image) gImages.get(aName);
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
			// get data from server
			
			gImages.put("monster", getImage(new URL(getDocumentBase(), "bin/images/monster.gif")));
			gImages.put("player", getImage(new URL(getDocumentBase(), "bin/images/player.gif")));
			gImages.put("tree", getImage(new URL(getDocumentBase(), "bin/images/tree.gif")));
			gImages.put("tree2", getImage(new URL(getDocumentBase(), "bin/images/tree2.gif")));
			gImages.put("eyeball", getImage(new URL(getDocumentBase(), "bin/images/eyeball.gif")));
			gImages.put("chest", getImage(new URL(getDocumentBase(), "bin/images/chest.gif")));
			gImages.put("chavin", getImage(new URL(getDocumentBase(), "bin/images/chavin.gif")));
			gImages.put("darklord", getImage(new URL(getDocumentBase(), "bin/images/darklord.gif")));
			gImages.put("gobbler", getImage(new URL(getDocumentBase(), "bin/images/gobbler.gif")));
			gImages.put("jackolantern", getImage(new URL(getDocumentBase(), "bin/images/jackolantern.gif")));
			gImages.put("wahoo", getImage(new URL(getDocumentBase(), "bin/images/wahoo.gif")));

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
	
			gLogView = new TextArea("Welome to JCavern\n", 5, 60, TextArea.SCROLLBARS_NONE);
			gLogView.setEditable(false);
			gLogView.setBackground(Color.black);
			gLogView.setForeground(CavernOrange);
			
			mWorldView.setSize(300, 300);		
			add(mWorldView);
			mWorld.addObserver(mWorldView);
			
			//mPlayerView.setSize(500, 50);		
			add(mPlayerView);
			mPlayer.addObserver(mPlayerView);
			
			mMissionView.setSize(500, 50);		
			add(mMissionView);
			mPlayer.getMission().addObserver(mMissionView);
			
			add(gLogView);

			mWorld.populateFor(mPlayer);
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
