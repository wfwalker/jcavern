/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

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
	
	/** * The current player. */
	private Player					mCurrentPlayer;
	
	/** * A table of messages */
	private Hashtable				mImages;

	/** * A MediaTracker to make sure the images get loaded. */
	private MediaTracker			mTracker;

	/** * The current applet */
	private static JCavernApplet	gApplet;
	
	private Button					mNewMissionButton;
	private Button					mNewPlayerButton;
	private Button					mLoadPlayerButton;
	private Button					mSavePlayerButton;
	private TextField				mLabel;
	
	public static JCavernApplet current()
	{
		return gApplet;
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
		
		if ((mCurrentPlayer != null) && (! mCurrentPlayer.isDead()))
		{
			mNewMissionButton.setEnabled(true);
			mLabel.setText("Current Player is " + mCurrentPlayer.getName() + " with " + mCurrentPlayer.getPoints() + " points");
			
		}
		else
		{
			mNewMissionButton.setEnabled(false);
			mLabel.setText("Press 'New Player' to create a new Player");
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
		
	public Image getBoardImage(String aName)
	{
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
		//setBackground(Color.black);
		//setForeground(CavernOrange);
		//setFont(new Font("Monospaced", Font.PLAIN, 12));
		
		mTracker = new MediaTracker(this);

		System.out.println("jcavern applet document base " + getDocumentBase());

		try
		{	
			// get data from server
			
			mImages.put("monster", fetchImageAndWait(new URL(getDocumentBase(), "bin/images/monster.gif")));
			mImages.put("demon", fetchImageAndWait(new URL(getDocumentBase(), "bin/images/demon.gif")));
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
			mImages.put("rajah", fetchImageAndWait(new URL(getDocumentBase(), "bin/images/rajah.gif")));
			mImages.put("wraith", fetchImageAndWait(new URL(getDocumentBase(), "bin/images/wraith.gif")));

			MonsterFactory.loadPrototypes(new URL(getDocumentBase(), "bin/monster.dat"));
			Treasure.loadPrototypes(new URL(getDocumentBase(), "bin/treasure.dat"));

			// add the text field
			mLabel = new TextField("Press 'New Player' to create a new Player", 60);
			add(mLabel);
			
			// add the load-player button
			mLoadPlayerButton = new Button("Load Player");
			mLoadPlayerButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					System.out.println("Load Player ");
				}
			});
			mLoadPlayerButton.setEnabled(false);
			add(mLoadPlayerButton);
			
			mSavePlayerButton = new Button("Save Player");
			mSavePlayerButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					System.out.println("Save Player ");
				}
			});
			mSavePlayerButton.setEnabled(false);
			add(mSavePlayerButton);
			
			mNewMissionButton = new Button("New Mission");
			mNewMissionButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					System.out.println("New Mission ");
					// build a mission window
					JCavernWindow	window = new JCavernWindow(mCurrentPlayer);
					
					window.setSize(500, 600);
					window.setTitle("JCavern Mission");
					window.setVisible(true);
				}
			});
			mNewMissionButton.setEnabled(false);
			add(mNewMissionButton);
			
			// add the create-a-new-player button
			mNewPlayerButton = new Button("New Player");
			mNewPlayerButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					System.out.println("New Player ");
					// build a mission window
					PlayerWindow	window = new PlayerWindow(null);
					
					window.setSize(200, 400);
					window.setTitle("JCavern Player");
					window.setVisible(true);
					
					mCurrentPlayer = window.getPlayer();
				}
			});
			add(mNewPlayerButton);
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
