/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * PlayerView displays the player's current statistics.
 *
 * @author	Bill Walker
 * @version	$Id$
 */
public class PlayerView extends TextArea implements Observer
{
	private Player	mModel;
	
	public PlayerView(Player aPlayer)
	{
		super("", 15, 20, TextArea.SCROLLBARS_NONE);

		mModel = aPlayer;
		
		setBackground(Color.black);
		setForeground(JCavernApplet.CavernOrange);
	}
	
	public void update(Observable a, Object b)
	{
		StringBuffer aBuffer = new StringBuffer();
		
		aBuffer.append("  Gold: " + mModel.getGold());
		aBuffer.append("\n  Arrows: " + mModel.getArrows());
		aBuffer.append("\n  Experience: " + mModel.getPoints());
		aBuffer.append("\n\n  In Use:");
			
		Vector items = mModel.getInUseItems();
		
		for (int index = 0; index < items.size(); index++)
		{
			aBuffer.append("\n" + index + " ");
			aBuffer.append(((Treasure) items.elementAt(index)).toString());
		}
		
		aBuffer.append("\n\n  Unused:");
			
		Vector items2 = mModel.getUnusedItems();
		
		for (int index = 0; index < items2.size(); index++)
		{
			aBuffer.append("\n" + index + " ");
			aBuffer.append(((Treasure) items2.elementAt(index)).toString());
		}
		
		setText(aBuffer.toString());
		repaint();
	}
}