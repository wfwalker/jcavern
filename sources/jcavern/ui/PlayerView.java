/* 
	PlayerView.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern.ui;

import jcavern.*;
import jcavern.thing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * PlayerView displays the player's current statistics.
 *
 * @author	Bill Walker
 * @version	$Id$
 */
public class PlayerView extends JCavernView
{
	/** * The Player being shown */
	private Player	mModel;
	
	/**
	 * Creates a new player view for the given Applet and Player.
	 *
	 * @param	inApplet	a non-null Applet
	 * @param	aPlayer		a non-null Player
	 */
	public PlayerView(JCavernApplet inApplet, Player aPlayer)
	{
		super(inApplet);
		
		mModel = aPlayer;
		
		setBackground(Color.black);
		setForeground(JCavernApplet.CavernOrange);
		
		aPlayer.addObserver(this);
	}

	/**
	 * Notifies this View that its model has changed.
	 *
	 * @param	a	who sent the event (ignored)
	 * @param	b	information about the change (ignored)
	 */
	public void update(Observable a, Object b)
	{
		repaint();
	}
	
	/**
	 * Paints the PlayerView.
	 *
	 * @param	g	the non-null Graphics with which paiting is performed.
	 */
	public void paint(Graphics g)
	{
		setBackground(Color.black);
		setForeground(JCavernApplet.CavernOrange);

		int y = g.getFontMetrics().getHeight();
		int lineHeight = g.getFontMetrics().getHeight();

		g.drawString("Name: " + mModel.getName(), 1, y); y += lineHeight;
		
		g.drawString("Points: " + mModel.getPoints() + "/" + mModel.getMaximumPoints(), 1, y); y += lineHeight;
		g.drawString("Gold: " + mModel.getGold(), 1, y); y += lineHeight;
		g.drawString("Arrows: " + mModel.getArrows(), 1, y); y += lineHeight;
		g.drawString("Moves: " + mModel.getMoveCount(), 1, y); y += lineHeight; y += lineHeight;
		
		g.drawString("In Use:", 1, y); y += lineHeight; 
			
		Vector items = mModel.getInUseItems();
				
		for (int index = 0; index < items.size(); index++)
		{
			g.drawString(index + " " + ((Treasure) items.elementAt(index)).toString(), 1, y); y += lineHeight;
		}
		
		y += lineHeight;
		g.drawString("Unused:", 1, y); y += lineHeight;
			
		Vector items2 = mModel.getUnusedItems();
		
		for (int index = 0; index < items2.size(); index++)
		{
			g.drawString(index + " " + ((Treasure) items2.elementAt(index)).toString(), 1, y); y += lineHeight;
		}
	}
}
