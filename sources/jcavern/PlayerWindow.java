package jcavern;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class PlayerWindow extends Frame
{
	public static final Color		CavernOrange = new Color(0xFF, 0x66, 0x00);
	
	/** * A view of the player statistics */
	private PlayerView				mPlayerView;
	
	/** * The representation of the player */
	private Player					mPlayer;

	public Player getPlayer()
	{
		return mPlayer;
	}
	
	public PlayerWindow(Player thePlayer)
	{
		setBackground(Color.black);
		setForeground(CavernOrange);
		setFont(new Font("Monospaced", Font.PLAIN, 12));
		
		setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
		
		if (thePlayer == null)
		{
			// Create a player and a view of the player
			SimpleDialog nameDialog = new SimpleDialog(this, "What is your name, player?");
			nameDialog.setModal(true);
			nameDialog.setVisible(true);
			String playerName = nameDialog.getText();
			mPlayer  = new Player(playerName);
		}
		else
		{
			mPlayer = thePlayer;
		}
		
		JCavernApplet.setPlayer(mPlayer);
		
		mPlayerView = new PlayerView(mPlayer);
		mPlayerView.setSize(150, 300);
		add(mPlayerView);
		
		Button closeButton = new Button("OK");
		
		closeButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("woo woo woo woo");
				setVisible(false);
			}
		});
		
		add(closeButton);
	}
}