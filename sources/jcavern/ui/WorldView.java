/* 
	JCavernApplet.java

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
	
	private Vector				mEvents;
	
	/**
	 * Creates a new WorldView for the given World.
	 *
	 * @param	aWorld	a non-null World being viewed.
	 */
	public WorldView(World aWorld)
	{
		System.out.println("Create WorldView");
		
		mModel = aWorld;
		mEvents = new Vector();
		
		setBackground(Color.black);
		setForeground(JCavernApplet.CavernOrange);
	}
	
	/** 
	 * Retrieves a WorldEvent for a particular subject.
	 * These are events that happened to the subject (i. e., subject was attacked, was placed in the world, etc).
	 *
	 * @param	aSubject	a non-null Thing
	 * @return				a non-null WorldEvent
	 */
	private WorldEvent getEventForSubject(Thing aSubject)
	{
		Enumeration theEvents = mEvents.elements();
		
		while (theEvents.hasMoreElements())
		{
			WorldEvent anEvent = (WorldEvent) theEvents.nextElement();
			
			if (anEvent.getSubject() == aSubject)
			{
				return anEvent;
			}
		}
		
		return null;
	}
	
	/** 
	 * Retrieves a WorldEvent for a particular cause.
	 * These are events that were caused by the given Thing (i. e., Thing attacked another thing, etc).
	 *
	 * @param	aSubject	a non-null Thing
	 * @return				a non-null WorldEvent
	 */
	private WorldEvent getEventForCause(Thing aCause)
	{
		Enumeration theEvents = mEvents.elements();
		
		while (theEvents.hasMoreElements())
		{
			WorldEvent anEvent = (WorldEvent) theEvents.nextElement();
			
			if (anEvent.getCause() == aCause)
			{
				return anEvent;
			}
		}
		
		return null;
	}
	
	/** 
	 * Retrieves a WorldEvent for a particular location.
	 * These are events that happened at the location (especially the deaths of combatants).
	 *
	 * @param	aLocation	a non-null Location
	 * @return				a non-null WorldEvent
	 */
	private WorldEvent getEventForLocation(Location aLocation)
	{
		Enumeration theEvents = mEvents.elements();
		
		while (theEvents.hasMoreElements())
		{
			WorldEvent anEvent = (WorldEvent) theEvents.nextElement();
			
			if (anEvent.getLocation() != null && anEvent.getLocation().equals(aLocation))
			{
				return anEvent;
			}
		}
		
		return null;
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
				mEvents = new Vector();
				break;
				
			case WorldContentsEvent.PLACED:
			case CombatEvent.ATTACKED_MISSED:
			case CombatEvent.ATTACKED_HIT:
			case CombatEvent.ATTACKED_KILLED:
			case WorldContentsEvent.REVEALED:
			case WorldEvent.RANGED_ATTACK:
				mEvents.addElement(anEvent);
				break;
				
			case WorldEvent.TURN_STOP:
				if (mEvents.size() > 0)
				{
					repaint();
				}
				else
				{
					System.out.println("WorldView.update() received TURN_END, did not repaint");
				}
				break;
		}
	}
	
	/**
	 * Paints a board image centered around the given coordinates.
	 */
	private void paintCenteredImage(Graphics g, int plotX, int plotY, String imageName) throws JCavernInternalError
	{
		Image theImage = JCavernApplet.getBoardImage(imageName);
	
		g.drawImage(theImage,
						plotX - WorldView.kPreferredImageSize / 2, plotY - WorldView.kPreferredImageSize / 2,
						WorldView.kPreferredImageSize, WorldView.kPreferredImageSize, null);
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
							Thing		theThing = mModel.getThing(aLocation);
							WorldEvent	anEvent = getEventForSubject(theThing);
					
							theThing.paint(g, plotX, plotY, anEvent);
						}
						else
						{
							WorldEvent	anEvent = getEventForLocation(aLocation);
							
							if (anEvent != null)
							{
								if (anEvent.getEventCode() == CombatEvent.ATTACKED_KILLED)
								{
									paintCenteredImage(g, plotX, plotY, "splat");
								}
								else
								{
									g.drawString(anEvent.toString(), plotX, plotY);
								}
							}
							else
							{
								g.drawString(".", plotX, plotY);
							}
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
