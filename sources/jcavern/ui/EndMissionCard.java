package jcavern.ui;

import jcavern.*;
import jcavern.ui.*;
import jcavern.thing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.applet.Applet;

/**
 * Displays an AppletCard informing the player that the mission ended.
 */
public class EndMissionCard extends AppletCard implements ActionListener
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
	private Panel					mEndMessage;
	
	/** * The representation of the mission end event */
	private WorldEvent					mEvent;
	
    /**
     * Creates a new EndMissionCard for the given Applet and Player.
     *
     * @param	inApplet	a non-null Applet in which to display the message
     * @param	inEvent		a non-null Event that triggered the end of the mission
     * @param	inWorld		a non-null World in which the mission ended
     */
	public EndMissionCard(JCavernApplet inApplet, WorldEvent inEvent, World inWorld)
	{
		super(inApplet);

		System.out.println("new end mission card " + inApplet + " " + inEvent + " " + inWorld);
		System.out.println("location " + inEvent.getLocation());
			
		mEvent = inEvent;
		mWorld = inWorld;
		Player aPlayer = (Player) inEvent.getSubject();
		
		mPlayerView = new PlayerView(inApplet, aPlayer);
		mPlayerView.setSize(150, 300);

		mMissionView = new MissionView(inApplet, aPlayer.getMission());
		mMissionView.setSize(150, 200);		

		// Create a world  and a view of the world
		mWorldView = new WorldView(inApplet, mWorld);
		mWorldView.setSize(300, 300);		

		// create a button pointing back to the index card
		mEndMessage = new Panel();
		mEndMessage.setLayout(new FlowLayout(FlowLayout.LEFT));
		mEndMessage.add(new ImageCanvas(10, 200));
		Button endButton = new JCavernButton("OK");
		endButton.addActionListener(this);
		mEndMessage.add(endButton);
		mEndMessage.add(new Label(mEvent.getMessage()), BorderLayout.CENTER);
		mEndMessage.setSize(300, 200);
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
		leftPanel.add(mEndMessage, BorderLayout.SOUTH);
		
		Panel rightPanel = new Panel();
		rightPanel.setLayout(new BorderLayout());
		rightPanel.setSize(150, 500);
		rightPanel.add(mPlayerView, BorderLayout.NORTH);
		rightPanel.add(mMissionView, BorderLayout.SOUTH);
		
		mApplet.add(leftPanel);
		mApplet.add(rightPanel);
		
		mApplet.validate();
	}

	/**
	 * Responds to ActionEvents from the OK Button by returning to the IndexCard.
	 *
	 * @param	event	a non-null ActionEvent.
	 */
    public void actionPerformed(ActionEvent event)
    {
    	try
    	{
			Object source = event.getSource();
			mApplet.displayHomeCard((Player) mEvent.getSubject());
		}
		catch (JCavernInternalError jcie)
		{
			System.out.println("Can't display home card " + jcie);
		}
    }
}
