package jcavern.ui;

import jcavern.*;
import jcavern.ui.*;
import java.awt.*;
import java.util.*;
import java.applet.Applet;

/**
 * Displays an AppletCard where all the action of performing a Mission takes place,
 * including a view of the Player's statistics and equipment, a view of the World and
 * its inhabitants, a view of the status of the mission, and a view of the log messages.
 */
public class MissionCard extends AppletCard
{
	/** * The official PLATO orange color, as used in jcavern. */
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
	private LogView					mLogView;
	
	/** * The representation of the player */
	private Player					mPlayer;
	
	/** * A table of messages */
	private Hashtable				mImages;

	/** * A MediaTracker to make sure the images get loaded. */
	private MediaTracker			mTracker;
	
	/** * The current global instance of a MissionCard. */
	private static MissionCard		gWindow;
		
	/**
	 * Displays an EndMissionCard on the current global instance of MissionCard.
	 *
	 * @param	aString		a non-null String containing an end-of-mission message
	 */
	public static void endMissionAlert(String aString)
	{
		gWindow.privateEndMissionAlert(aString);
	}
	
	/**
	 * Displays an EndMissionCard on this MissionCard's Applet.
	 *
	 * @param	aString		a non-null String containing an end-of-mission message
	 */
	private void privateEndMissionAlert(String aString)
	{
		EndMissionCard anAlert = new EndMissionCard(mApplet, aString);
		anAlert.show();
	}

    /**
     * Creates a new MissionCard for the given Applet and Player.
     *
     * @param	anApplet	a non-null Applet in which to display the message
     * @param	thePlayer	a non-null Player who will do a Mission
     */
	public MissionCard(JCavernApplet anApplet, Player thePlayer)
	{
		super(anApplet);
		
		mPlayer = thePlayer;
		
		mPlayerView = new PlayerView(mPlayer);
		mPlayer.setMission(MonsterFactory.createMission(mPlayer));
		mMissionView = new MissionView(mPlayer.getMission());

		// Create a world  and a view of the world
		mWorld = new World();
		mWorldView = new WorldView(mWorld);

		mLogView = new LogView(mPlayer);
		mLogView.setSize(300, 200);
		
		mWorldView.setSize(300, 300);		
		mWorld.addObserver(mWorldView);
		mWorld.addObserver(mLogView);
		
		mPlayerView.setSize(150, 300);
		
		mMissionView.setSize(150, 200);		
		mPlayer.getMission().addObserver(mMissionView);
	}
	
	/**
	 * Displays this AppletCard in its Applet.
	 */
	public void show()
	{
		super.show();
		
		mApplet.setBackground(Color.black);
		mApplet.setForeground(CavernOrange);
		
		mApplet.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
		
		
		Panel leftPanel = new Panel();
		leftPanel.setLayout(new BorderLayout());
		leftPanel.setSize(300, 500);
		leftPanel.add(mWorldView, BorderLayout.NORTH);
		leftPanel.add(mLogView, BorderLayout.SOUTH);
		
		Panel rightPanel = new Panel();
		rightPanel.setLayout(new BorderLayout());
		rightPanel.setSize(150, 500);
		rightPanel.add(mPlayerView, BorderLayout.NORTH);
		rightPanel.add(mMissionView, BorderLayout.SOUTH);
		
		//mApplet.add(mWorldView);
		//mApplet.add(mPlayerView);
		//mApplet.add(mLogView);
		//mApplet.add(mMissionView);
		mApplet.add(leftPanel);
		mApplet.add(rightPanel);
		
		mApplet.validate();

		gWindow = this;
		
		try
		{
			mWorld.populateFor(mPlayer);
		
			mWorldView.requestFocus();
        	mWorldView.addKeyListener(new KeyboardCommandListener(mWorld, mWorldView, mPlayer, mMissionView));
			mWorld.eventHappened(new WorldEvent(mPlayer, WorldEvent.TURN_STOP));
		}
		catch (JCavernInternalError jcie)
		{
			System.out.println("UHOH, internal error " + jcie);
		}
	}
}