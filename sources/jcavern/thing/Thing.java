package jcavern.thing;

import java.io.*;
import java.util.*;
import java.awt.*;

import jcavern.ui.*;
import jcavern.*;

/**
 * Parent class for players, monsters, treasure chests, castles, and trees.
 *
 * @author	Bill Walker
 * @version	$Id$
 */
public abstract class Thing extends Observable implements Cloneable, Serializable
{
	/** * The name of this thing. */
	private String							mName;
	
	/** * How many turns has this Thing been in the game? */
	private int								mMoveCounter;
	
	/** * Is this Thing invisible? */
	private boolean							mInvisible;
	
	/** * How to show this Thing in a graphical view */
	protected transient GraphicalThingView	mGraphicalThingView;

	/**
	 * A Graphical View of a Thing
	 */
	public class GraphicalThingView implements Observer
	{
		/** * The name of the image to use for this thing. */
		private Image			mImage;
	
		/** * The name of the image to use for this thing. */
		private String			mImageName;
	
		/** * The textual appearance of the Thing (usually two letters). */
		private String			mTextSymbol;
		
		/** * Should a highlight be painted this turn */
		private	boolean			mHighlightThisTurn;
		
		/**
		 * Creates a new GraphicalThingView for the given image name.
		 *
		 * @param	inImageName		the non-null String name of the image for this Thing.
		 */
		public GraphicalThingView(String inImageName)
		{
			mImageName = inImageName;
			mImage = null;
			mHighlightThisTurn = false;
		}
		
		/**
		 * Receives update notification that the World being viewed has changed.
		 *
		 * @param	a	the object that sent the update
		 * @param	b	information about the update.
		 */
		public void update(Observable a, Object b)
		{
			//System.out.println("Thing.GraphicalThingView(" + b + ")");
			WorldEvent anEvent = (WorldEvent) b;
			
			if (anEvent.getEventCode() == WorldEvent.TURN_START)
			{
				mHighlightThisTurn = false;
			}
			else
			{
				mHighlightThisTurn = mHighlightThisTurn || shouldHighlight(anEvent);
			}
		}
			
		
		/**
		 * Returns the Image used to depict this thing.
		 *
		 * @param		inApplet				the current applet (used to retrieve images)
		 * @return								a non-null Image
		 * @exception	JCavernInternalError	could not retrieve the image from the dictionary.
		 */
		public Image getImage(JCavernApplet inApplet) throws JCavernInternalError
		{
			if (mImage == null)
			{
				mImage = inApplet.getBoardImage(mImageName);
			}
			
			return mImage;
		}
	
		/**
		 * Returns the name of the image used to depict this Thing.
		 *
		 * @return	a non-null String containing the name of this Thing's image.
		 */
		public String getImageName()
		{
			return mImageName;
		}

		/**
		 * Returns whether the given event merits highlighting this Thing this turn.
		 *
		 * @return	<CODE>true</CODE> if the given event merits highlighting this Thing this turn, <CODE>false</CODE> otherwise
		 */
		public boolean shouldHighlight(WorldEvent anEvent)
		{
			return false;
		}
		
		/**
		 * Returns whether this Thing should be highlighted this turn.
		 *
		 * @return	<CODE>true</CODE> if this Thing should be highlighted this turn, <CODE>false</CODE> otherwise
		 */
		protected boolean getHighlightThisTurn()
		{
			return mHighlightThisTurn;
		}

		/**
		 * Paints this thing by drawing its image, but only if it is not invisible.
		 *
		 * @param		inApplet				the current applet (used to retrieve images)
		 * @param		g						a non-null Graphics object used for painting
		 * @param		plotX					offset within the current Graphics for painting this Thing
		 * @param		plotY					offset within the current Graphics for painting this Thing
		 * @exception	JCavernInternalError	couldn't acquire the necessary to paint this Thing
		 */
		public void paint(JCavernApplet inApplet, Graphics g, int plotX, int plotY) throws JCavernInternalError
		{
			if ((! getInvisible()) || (getHighlightThisTurn()))
			{
				Image theImage = getImage(inApplet);
				
				if (theImage == null)
				{
					throw new JCavernInternalError("could not get image");
				}
				else
				{
					WorldView.paintCenteredImage(g, theImage, plotX, plotY);
				}
			}
		}
	}

	/**
	 * Creates a new thing with the given name and image name.
	 *
	 * @param	aName		a non-null String name
	 * @param	imageName	a non-null board image name
	 */
	public Thing(String aName, String imageName)
	{
		this(aName, imageName, false);
	}
	
	/**
	 * Creates a new thing with the given name, image name, and invisibility flag.
	 *
	 * @param	aName		a non-null String name
	 * @param	inImageName	a non-null board image name
	 * @param	invisible	<CODE>true</CODE> if this Thing is invisible, <CODE>false</CODE> otherwise
	 */
	public Thing(String aName, String inImageName, boolean invisible)
	{
		mName = aName;
		mMoveCounter = 0;
		mInvisible = invisible;
		mGraphicalThingView = createGraphicalThingView(inImageName);
	}


	/**
	 * Returns whether this combatant has a proper name
	 *
	 * @return	<CODE>true</CODE> if this combatant has a proper name, <CODE>false</CODE> otherwise.
	 */
	protected boolean hasProperName()
	{
		return false;
	}

	/**
	 * Returns a noun phrase for this Thing.
	 *
	 * @return		a non-null string containing a noun phrase, with articles as appropriate.
	 */
	public String getNounPhrase()
	{
		if (hasProperName())
		{
			return getName();
		}
		else
		{
			return "the " + getName();
		}
	}

	/**
	 * Sets whether this Thing is invisible.
	 *
	 * @param	aBoolean	<CODE>true</CODE> if this Thing is invisible, <CODE>false</CODE> otherwise
	 */
	public void setInvisible(boolean aBoolean)
	{
		mInvisible = aBoolean;
	}
	
	/**
	 * Returns whether this Thing is invisible.
	 *
	 * @return	<CODE>true</CODE> if this Thing is invisible, <CODE>false</CODE> otherwise
	 */
	public boolean getInvisible()
	{
		return mInvisible;
	}
	
	/**
	 * Returns the GraphicalThingView for this thing.
	 *
	 * @return	the GraphicalThingView for this thing
	 */
	public GraphicalThingView getGraphicalThingView()
	{
		return mGraphicalThingView;
	}
	
	/**
	 * Creates a GraphicalThingView appropriate to this Thing.
	 *
	 * @param	inImageName		a non-null String image name
	 * @return					a non-null GraphicalThingView appropriate to this Thing.
	 */
	protected GraphicalThingView createGraphicalThingView(String inImageName)
	{
		return new GraphicalThingView(inImageName);
	}

	/**
	 * Returns the number of moves this Thing has been alive.
	 *
	 * @return	an integer, the number of moves this Thing has been alive.
	 */
	public int getMoveCount()
	{
		return mMoveCounter;
	}
	
	/**
	 * Returns a string representation of this thing.
	 *
	 * @return	a non-null String containing the name of this thing.
	 */
	public String toString()
	{
		return getClass().getName() + "." + mName;
	}
	
	/**
	 * Returns the name of this thing.
	 *
	 * @return	a non-null String containing the name of this thing.
	 */
	public String getName()
	{
		return mName;
	}
	
	/**
	 * Handles a single turn by incrementing the move counter.
	 * Some subclasses (especially Monster) are expected to override this method.
	 *
	 * @param		aWorld					the non-null World in which the action is taking place.
	 * @exception	JCavernInternalError	the Thing could not process its turn
	 */
	public void doTurn(World aWorld) throws JCavernInternalError
	{
		mMoveCounter++;
	}
	
	/**
	 * Creates a clone of this Thing.
	 *
	 * @return	a non-null clone of this Thing.
	 */
	public abstract Object clone();
			
	/**
	 * Notifies this thing that it has been removed from the World.
	 * Subclasses interested in receiving such notifications should override this method.
	 *
	 * @param		aWorld					a non-null World from which this Thing has been removed.
	 * @param		aLocation				a non-null Location from which this Thing has been removed.
	 * @exception	JCavernInternalError	this Thing had trouble processing the notification
	 */
	public void thingRemoved(World aWorld, Location aLocation) throws JCavernInternalError
	{
	
	}
	
	/**
	 * Notifies this thing that it has been placed in the World.
	 * Subclasses interested in receiving such notifications should override this method.
	 *
	 * @param		aWorld					a non-null World in which this Thing has been placed.
	 * @param		aLocation				a non-null Location at which this Thing has been placed.
	 * @exception	JCavernInternalError	this Thing had trouble processing the notification
	 */
	public void thingPlaced(World aWorld, Location aLocation) throws JCavernInternalError
	{
	
	}
}

