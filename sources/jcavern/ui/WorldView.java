/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern.ui;

import jcavern.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * WorldView displays a view of a world centered around a player's location.
 *
 * @author	Bill Walker
 * @version	$Id$
 */
public class WorldView extends Canvas implements Observer
{
	/** * Preferred size of images */
	public static final int		kPreferredImageSize = 32;
	
	/** * Number of pixels between board pictures. */
	private static final int	kSpacing = 5 * kPreferredImageSize / 4;
	
	/** * The World being viewed by this View. */
	private World				mModel;
	
	private Hashtable			mEvents;
	
	/**
	 * Creates a new WorldView for the given World.
	 *
	 * @param	aWorld	a non-null World being viewed.
	 */
	public WorldView(World aWorld)
	{
		System.out.println("Create WorldView");
		
		mModel = aWorld;
		mEvents = new Hashtable();
		
		setBackground(Color.black);
		setForeground(JCavernApplet.CavernOrange);
	}
	
	/**
	 * Receives update notification that the World being viewed has changed.
	 */
	public void update(Observable a, Object b)
	{
		// System.out.println("WorldView.update(" + a + ", " + b + ")");
		
		WorldEvent anEvent = (WorldEvent) b;
		
		switch (anEvent.getEventCode())
		{
			case WorldEvent.TURN_START:
				mEvents = new Hashtable();
				break;
				
			case WorldEvent.ATTACKED_HIT:
			case WorldEvent.ATTACKED_KILLED:
			case WorldEvent.REVEALED:
				mEvents.put(anEvent.getSubject(), anEvent);
				break;
				
			case WorldEvent.TURN_STOP:
				repaint();
				break;
		}
	}
	
	/**
	 * Paints a view of the world, centered around the player's current location.
	 */
	public void paint(Graphics g)
	{
		//System.out.println("--- WorldView.paint()");
		
		Player		thePlayer = mModel.getPlayer();

		g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);

		if (thePlayer == null)
		{
			return;
		}
		
		try
		{
			Location	theLocation = mModel.enforceMinimumInset(mModel.getLocation(thePlayer), 3);
		
			for (int yIndex = -3; yIndex <= 3; yIndex++)
			{
				for (int xIndex = -3; xIndex <= 3; xIndex++)
				{
					Location	aLocation = new Location(xIndex + theLocation.getX(), yIndex + theLocation.getY());
					
					if (mModel.inBounds(aLocation))
					{
						int		plotX = (kSpacing / 2) + kSpacing * (xIndex + 3);
						int		plotY = (kSpacing / 2) + kSpacing * (yIndex + 3);

						if (! mModel.isEmpty(aLocation))
						{
							Thing	theThing = mModel.getThing(aLocation);
							boolean	highlight = mEvents.containsKey(theThing);
					
							/*if (mEvents.containsKey(theThing))
							{
								WorldEvent	anEvent = (WorldEvent) mEvents.get(theThing);
								
								if (! anEvent.getSubject().getInvisible())
								{
									g.drawOval(plotX - 3 * kSpacing / 4, plotY - 3 * kSpacing / 4, 3 * kSpacing / 2, 3 * kSpacing / 2);
								}
							}
							else
							{
								// System.out.println("no events for " + theThing + ", just painting");
							}*/
							
							theThing.paint(g, plotX, plotY, highlight);
						}
						else
						{
							g.drawString(".", plotX, plotY);
						}
					} // end if mModel.isEmpty
				} // end for xIndex
			} // end for yIndex			
		}
		catch (JCavernInternalError jcie)
		{
			System.out.println("Can't paint world view, " + jcie);
		}
		catch (EmptyLocationException ele)
		{
			System.out.println("Can't paint world view, internal error " + ele);
		}
		catch (IllegalLocationException ile)
		{
			System.out.println("Illegal location exception, " + ile);
		}
	}
}
