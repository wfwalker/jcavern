package jcavern.ui;

import jcavern.*;
import jcavern.ui.*;
import java.awt.*;
import java.util.*;
import java.applet.Applet;

public class MissionCard extends AppletCard
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
	private LogView					mLogView;
	
	/** * The representation of the player */
	private Player					mPlayer;
	
	/** * A table of messages */
	private Hashtable				mImages;

	/** * A MediaTracker to make sure the images get loaded. */
	private MediaTracker			mTracker;
	
	private static MissionCard		gWindow;
	
	public static void log(String aString)
	{
		if (gWindow != null)
		{
			gWindow.privateLog(aString);
		}
	}

	private void privateLog(String aString)
	{
		if (mLogView != null)
		{
			mLogView.addLine(aString);
		}
	}
	
	public static void endMissionAlert(String aString)
	{
		gWindow.privateEndMissionAlert(aString);
	}
	
	private void privateEndMissionAlert(String aString)
	{
		EndMissionCard anAlert = new EndMissionCard(mApplet, aString);
		anAlert.show();
	}
	
	public Player getPlayer()
	{
		return mPlayer;
	}
	
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

		mLogView = new LogView();
		mLogView.setSize(450, 200);
		
		mWorldView.setSize(300, 300);		
		mWorld.addObserver(mWorldView);
		
		mPlayerView.setSize(150, 300);
		
		mMissionView.setSize(300, 50);		
		mPlayer.getMission().addObserver(mMissionView);
	}
	
	public void show()
	{
		super.show();
		
		mApplet.setBackground(Color.black);
		mApplet.setForeground(CavernOrange);
		
		mApplet.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
			
		mApplet.add(mWorldView);
		mApplet.add(mPlayerView);
		mApplet.add(mMissionView);
		mApplet.add(mLogView);
		
		mApplet.validate();

		gWindow = this;
		
		try
		{
			mWorld.populateFor(mPlayer);
		
			mWorldView.requestFocus();
        	mWorldView.addKeyListener(new KeyboardCommandListener(mWorld, mWorldView, mPlayer, mMissionView));
		}
		catch (JCavernInternalError jcie)
		{
			System.out.println("UHOH, internal error " + jcie);
		}
	}
}