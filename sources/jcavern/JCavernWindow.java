package jcavern;

import java.awt.*;
import java.util.*;

public class JCavernWindow extends Frame
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
	
	private static JCavernWindow	gWindow;
	
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
	
	public static void alert(String aString, boolean closeWindow)
	{
		gWindow.privateAlert(aString, closeWindow);
	}
	
	private void privateAlert(String aString, boolean closeWindow)
	{
		SimpleAlert anAlert = new SimpleAlert(this, aString);
		anAlert.setModal(true);
		anAlert.setVisible(true);
		
		if (closeWindow)
		{
			setVisible(false);
		}
	}
	
	public Player getPlayer()
	{
		return mPlayer;
	}
	
	public JCavernWindow(Player thePlayer)
	{
		setBackground(Color.black);
		setForeground(CavernOrange);
		setFont(new Font("Monospaced", Font.PLAIN, 12));
		
		setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
			
		mPlayer = thePlayer;
		
		mPlayerView = new PlayerView(mPlayer);
		mPlayer.setMission(MonsterFactory.createMission(mPlayer));
		mMissionView = new MissionView(mPlayer.getMission());

		// Create a world  and a view of the world
		mWorld = new World();
		mWorldView = new WorldView(mWorld);

		mLogView = new LogView();
		mLogView.setSize(450, 300);
		
		mWorldView.setSize(300, 300);		
		add(mWorldView);
		mWorld.addObserver(mWorldView);
		
		mPlayerView.setSize(150, 300);
		add(mPlayerView);
		
		mMissionView.setSize(300, 50);		
		add(mMissionView);
		mPlayer.getMission().addObserver(mMissionView);
		
		add(mLogView);

		gWindow = this;
	}
	
	public void show()
	{
		super.show();
		
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