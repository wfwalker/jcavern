
package jcavern.ui;

import jcavern.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class IndexCard extends AppletCard
{	
	/** * The current player. */
	private Player					mCurrentPlayer;

	private Button					mNewMissionButton;
	private Button					mNewPlayerButton;
	private Button					mLoadPlayerButton;
	private Button					mSavePlayerButton;
	private Label					mLabel;

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
			}
		});
		mSavePlayerButton.setEnabled(false);

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