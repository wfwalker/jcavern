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
import java.applet.Applet;
import java.awt.event.*;
import java.util.*;

/**
 * WorldView displays a view of a world centered around a player's location.
 *
 * @author	Bill Walker
 * @version	$Id$
 */
public class WorldView extends JCavernView
{
	/** * Preferred size of images */
	public static final int			kPreferredImageSize = 32;
	
	/** * Number of pixels between board pictures. */
	private static final int		kSpacing = 5 * kPreferredImageSize / 4;
	
	/** * The World being viewed by this View. */
	private World					mModel;
	
	/** * The list of events received from the model for this turn */
	private Vector					mEvents;
	
	/** * The location of the most interesting thing in the World, usually the player. */
	private	Location				mLocationOfInterest;
	
	/** * A Thread to animate the world view. */
	private WorldViewUpdateThread	mThread;
	
	/** * An animation thread class. */
	private class WorldViewUpdateThread extends Thread
	{
		/** * Should the thread continue running. */
		private boolean		mKeepRunning = true;
		
		/** * Asks the thread to stop running. */
		public void pleaseStop()
		{
			mKeepRunning = false;
		}
		
		/** * The main animation routine. */
		public void run()
		{
			while (mKeepRunning)
			{
				try
				{
					sleep(5000);
					System.out.println("WorldView animation thread");
				}
				catch (InterruptedException ie)
				{
					//System.out.println("Interrupted " + ie);
				}
			}
		}
	}
	
	/**
	 * Creates a new WorldView for the given World.
	 *
	 * @param	inApplet				a non-null Applet used to retrieve images
	 * @param	inWorld					a non-null World being viewed.
	 * @param	inLocationOfInterest	a Location of interest, or <CODE>null</CODE> if none.
	 */
	public WorldView(JCavernApplet inApplet, World inWorld, Location inLocationOfInterest)
	{
		super(inApplet);
		
		if (inWorld == null)
		{
			throw new IllegalArgumentException("can't create WorldView with null World!");
		}
		
		mModel = inWorld;
		mEvents = new Vector();
		
		if (inLocationOfInterest != null)
		{
			mLocationOfInterest = mModel.enforceMinimumInset(inLocationOfInterest, 3);
		}
		
		setBackground(Color.black);
		setForeground(JCavernApplet.CavernOrange);
		
		mThread = new WorldViewUpdateThread();
		mThread.start();
	}
	
	/**
	 * Stops the animation thread from running.
	 */
	public void stopThread()
	{
		mThread.pleaseStop();
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
	 *
	 * @param	a	the object that sent the update
	 * @param	b	information about the update.
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
			case WorldContentsEvent.MOVED:
			case WorldContentsEvent.REMOVED:
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
	 *
	 * @param		inApplet				a non-null Applet used to retrieve images
	 * @param		g						a non-null Graphics object with which to paint
	 * @param		plotX					x coordinate relative to the corner of the world view
	 * @param		plotY					y coordinate relative to the corner of the world view
	 * @param		imageName				name of the image
	 * @exception	JCavernInternalError	could not paint
	 */
	public static void paintCenteredImage(JCavernApplet inApplet, Graphics g,
					int plotX, int plotY, String imageName) throws JCavernInternalError
	{
		Image theImage = inApplet.getBoardImage(imageName);
	
		paintCenteredImage(g, theImage, plotX, plotY);
	}
	
	/**
	 * Paints a board image centered around the given coordinates.
	 *
	 * @param		g						a non-null Graphics object with which to paint
	 * @param		inImage					a non-null Image
	 * @param		plotX					x coordinate relative to the corner of the world view
	 * @param		plotY					y coordinate relative to the corner of the world view
	 * @exception	JCavernInternalError	could not paint
	 */
	public static void paintCenteredImage(Graphics g, Image inImage, 
					int plotX, int plotY) throws JCavernInternalError
	{
		g.drawImage(inImage,
						plotX - WorldView.kPreferredImageSize / 2,
						plotY - WorldView.kPreferredImageSize / 2,
						WorldView.kPreferredImageSize, WorldView.kPreferredImageSize, null);
	}
	
	/**
	 * Paints some text centered around the given coordinates.
	 *
	 * @param		g						a non-null Graphics object with which to paint
	 * @param		plotX					x coordinate relative to the corner of the world view
	 * @param		plotY					y coordinate relative to the corner of the world view
	 * @param		inLavel					the non-null String to be painted
	 * @exception	JCavernInternalError	could not paint
	 */
	public static void paintCenteredText(Graphics g, int plotX, int plotY, String inLabel) throws JCavernInternalError
	{
		int textWidth = g.getFontMetrics().stringWidth(inLabel);
		int	textHeight = g.getFontMetrics().getHeight();
		
		System.out.println("textWidth " + textWidth + " textHeight " + textHeight);
	
		g.drawString(inLabel, plotX - textWidth / 2, plotY - textHeight / 2);
	}

	/**
	 * Paints a representation of a given Location at the given screen coordinates.
	 * If the given Location is out of bounds, do nothing.
	 * If the given Location is empty, see if there's an event for that location; if so, paint the event.
	 * If the given Location contains a Thing, invoke that Thing's paint method
	 *
	 * @param		g						a non-null Graphics object 
	 * @param		inLocation				a non-null Location to paint 
	 * @param		plotX					horizontal screen coordinate relative to corner of this WorldView
	 * @param		plotY					vertical screen coordinate relative to corner of this WorldView
	 * @exception	JCavernInternalError	problem retrieving contents of this Location
	 */
	public void paintLocation(Graphics g, Location inLocation, int plotX, int plotY) throws JCavernInternalError
	{
		if (mModel.inBounds(inLocation))
		{
			if (! mModel.isEmpty(inLocation))
			{
				Thing		theThing;
				
				try
				{
					theThing = mModel.getThing(inLocation);
				}
				catch(EmptyLocationException ele)
				{
					throw new JCavernInternalError("model says not empty, but throws EmptyLocationException");
					
				}
				catch(IllegalLocationException ele)
				{
					throw new JCavernInternalError("model says not in bounds, but throws IllegalLocationException");	
				}
				
				theThing.getGraphicalThingView().paint(getApplet(), g, plotX, plotY);
			}
			else
			{
				WorldEvent	anEvent = getEventForLocation(inLocation);
				paintEvent(g, anEvent, plotX, plotY);
				
			}
		} // end if mModel.isEmpty
	}
	
	/**
	 * Paints a representation of a given Event at the given screen coordinates.
	 *
	 * @param		g						a non-null Graphics object 
	 * @param		anEvent					a non-null WorldEvent to paint 
	 * @param		plotX					horizontal screen coordinate relative to corner of this WorldView
	 * @param		plotY					vertical screen coordinate relative to corner of this WorldView
	 * @exception	JCavernInternalError	problem painting a board image
	 */
	public void paintEvent(Graphics g, WorldEvent anEvent, int plotX, int plotY) throws JCavernInternalError
	{
		if (anEvent != null)
		{
			switch (anEvent.getEventCode())
			{
				case CombatEvent.ATTACKED_KILLED:
						paintCenteredImage(getApplet(), g, plotX, plotY, "splat");
						break;
				case WorldContentsEvent.REMOVED:
						paintCenteredText(g, plotX, plotY, "poof");
						break;
				case CombatEvent.RANGED_ATTACK:
						paintCenteredText(g, plotX, plotY, "*");
						break;
				default:
						//paintCenteredImage(getApplet(), g, plotX, plotY, "empty");
						break;
			}
		}
		//else
		//{
		//	paintCenteredImage(getApplet(), g, plotX, plotY, "empty");
		//}
	}
	
	/**
	 * Converts from World coordinates to WorldView coordinates.
	 * Used in WorldView paint routines.
	 * @see		jcavern.World#getBounds()
	 *
	 * @param	ordinal		a World coordinate
	 * @return				a WorldView coordinate.
	 */
	private int scaled(double ordinal)
	{
		return (int) (kSpacing / 2 + (ordinal * kSpacing));
	}
	
	/**
	 * Draw a grid of dots on the World view to
	 * help convey the idea of motion.
	 *
	 * @param		g						a non-null Graphics object 
	 * @param		theLocation				the location to focus on. 
	 */
	private void paintBorder(Graphics g, Location theLocation)
	{
		int		topBorder = scaled(-0.5);
		int		bottomBorder = scaled(6.5);
		
		g.setColor(JCavernApplet.CavernOrangeDim);
		
		// draw axes at the perimeter of the display
		
		if (theLocation.getY() < 4) g.drawLine(topBorder, topBorder, bottomBorder, topBorder);
		if (theLocation.getY() > (mModel.getBounds().height - 5)) g.drawLine(topBorder, bottomBorder, bottomBorder, bottomBorder);
		if (theLocation.getX() < 4) g.drawLine(topBorder, topBorder, topBorder, bottomBorder);
		if (theLocation.getX() > (mModel.getBounds().width - 5)) g.drawLine(bottomBorder, topBorder, bottomBorder, bottomBorder);

		// draw tick marks on those axes. Major ticks every third unit.
		
		for (int yIndex = -3; yIndex <= 3; yIndex++)
		{
			int		worldY = yIndex + theLocation.getY();
			int		plotY = scaled(yIndex + 3);
			boolean	isYEdge =
						Math.abs(worldY - mModel.getBounds().height) <= 3 ||
						worldY < 3;

			for (int xIndex = -3; xIndex <= 3; xIndex++)
			{
				int		worldX = xIndex + theLocation.getX();
				int		plotX = scaled(xIndex + 3);
				boolean	isXEdge =
							Math.abs(worldX - mModel.getBounds().width) <= 3 ||
							worldX < 3;

				if ((worldX % 3 == 0) && (worldY % 3 == 0))
				{
					g.fillRect(plotX - 3, plotY - 3, 6, 6);
				}
			}
		}
	}
	
	/**
	 * Paints a view of the world, centered around the player's current location.
	 *
	 * @param	g		a non-null Graphics object with which to paint
	 */
	public void paint(Graphics g)
	{
		Player		thePlayer;
		
		try
		{
			thePlayer = mModel.getPlayer();
			mLocationOfInterest = mModel.enforceMinimumInset(mModel.getLocation(thePlayer), 3);
		}
		catch (JCavernInternalError jcie)
		{
			System.out.println("Can't find player, using " + mLocationOfInterest);
		}
		
		paintBorder(g, mLocationOfInterest);
		
		try
		{
			for (int yIndex = -3; yIndex <= 3; yIndex++)
			{
				int		worldY = yIndex + mLocationOfInterest.getY();
				int		plotY = scaled(yIndex + 3);

				for (int xIndex = -3; xIndex <= 3; xIndex++)
				{
					int			worldX = xIndex + mLocationOfInterest.getX();
					int			plotX = scaled(xIndex + 3);
					Location	aLocation = new Location(worldX, worldY);					
					
					paintLocation(g, aLocation, plotX, plotY);
				}
			}
		}
		catch (JCavernInternalError jcie)
		{
			System.out.println("Can't paint world view, " + jcie);
		}
	}
}
