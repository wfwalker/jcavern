
package jcavern.ui;

import java.io.*;
import jcavern.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;

/**
 * IndexCard presents the user with a set of options to manage Players and Missions.
 */
public class IndexCard extends AppletCard
{	
	/** * The current player. */
	private Player					mCurrentPlayer;

	/** * The button for undertaking a new mission */
	private Button					mNewMissionButton;
	
	/** * The button for creating a new player */
	private Button					mNewPlayerButton;
	
	/** * The button for loading a player. */
	private Button					mLoadPlayerButton;
	
	/** * The button for saving a player. */
	private Button					mSavePlayerButton;
	
	/** * The label for displaying status messages. */
	private Label					mLabel;

	/**
	 * Creates an IndexCard for the given Applet and Player.
	 * The IndexCard will enable and disable the various buttons based on the status of the current player.
	 *
	 * @param	anApplet		a non-null Applet in which the index will be displayed
	 * @param	currentPlayer	the Player currently associated with the user, or <CODE>null</CODE> if none.
	 */
	public IndexCard(JCavernApplet anApplet, Player currentPlayer)
	{
		super(anApplet);
		
		mCurrentPlayer = currentPlayer;
		
		if (mCurrentPlayer == null)
		{
			mLabel = new Label("Press 'New Player' to create a new Player");
		}
		else if (mCurrentPlayer.isDead())
		{
			mLabel = new Label("Player " + mCurrentPlayer.getName() + " is dead. Press 'New Player' to create a new Player");
		}
		else
		{
			mLabel = new Label("Welcome to the King's Castle, " + mCurrentPlayer.getName());
		}
		
		// add the load-player button
		mLoadPlayerButton = new Button("Load Player");
		mLoadPlayerButton.setForeground(Color.black);
		mLoadPlayerButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("Load Player ");
			}
		});
		mLoadPlayerButton.setEnabled(false);
		
		mSavePlayerButton = new Button("Save Player");
		mSavePlayerButton.setForeground(Color.black);
		mSavePlayerButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("Save Player ");
				
				try
				{
					ObjectOutputStream aStream = new ObjectOutputStream(System.out);
					aStream.writeObject(mCurrentPlayer);
					aStream.flush();
				}
				catch(IOException ioe)
				{
					System.out.println("can't serialize " + ioe);
				}
				System.out.println("Done Save Player");
				
			}
		});
		mSavePlayerButton.setEnabled((mCurrentPlayer != null) && (! mCurrentPlayer.isDead()));

		mNewMissionButton = new Button("New Mission");
		mNewMissionButton.setForeground(Color.black);
		mNewMissionButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("New Mission ");
				// build a mission window
				MissionCard	window = new MissionCard(mApplet, mCurrentPlayer);
				
				window.show();
			}
		});
		mNewMissionButton.setEnabled((mCurrentPlayer != null) && (! mCurrentPlayer.isDead()));
		
		// add the create-a-new-player button
		mNewPlayerButton = new Button("New Player");
		mNewPlayerButton.setForeground(Color.black);
		mNewPlayerButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				CreatePlayerCard aDialog = new CreatePlayerCard(mApplet);
				aDialog.show();
			}
		});

		show();
	}
	
	/**
	 * Displays this IndexCard in its Applet.
	 */
	public void show()
	{
		super.show();
		
		mApplet.setLayout(new GridLayout(8, 1));
		
		//mApplet.setForeground(Color.black);
		//mApplet.setBackground(Color.white);
		
		mApplet.add(mLabel);
		
		mApplet.add(createLabelledButtonPanel(mLoadPlayerButton, "Load player from database"));
		mApplet.add(createLabelledButtonPanel(mSavePlayerButton, "Save player to database"));
		mApplet.add(createLabelledButtonPanel(mNewMissionButton, "Undertake a new mission"));
		mApplet.add(createLabelledButtonPanel(mNewPlayerButton, "Create a new player"));
				
		mApplet.validate();
	}

}