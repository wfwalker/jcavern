package jcavern;

import java.util.Observable;
import java.awt.*;

/**
 * Parent class for players, monsters, treasure chests, castles, and trees.
 *
 * @author	Bill Walker
 * @version	$Id$
 */
public abstract class Thing extends Observable implements Cloneable
{
	/** * The name of this thing. */
	private String		mName;
	
	/** * The name of the image to use for this thing. */
	private Image		mImage;

	/** * The name of the image to use for this thing. */
	private String		mImageName;

	/** * The textual appearance of the Thing (usually two letters). */
	private String		mTextSymbol;
	
	/** * How many turns has this Thing been in the game? */
	private int			mMoveCounter;
	
	/** * Is this Thing invisible? */
	private boolean		mInvisible;
	
	/**
	 * Creates a new thing with the given name.
	 *
	 * @param	aName	a non-null String name
	 */
	public Thing(String aName)
	{
		this(aName, "none", false);
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
	 * @param	imageName	a non-null board image name
	 * @param	invisible	<CODE>true</CODE> if this Thing is invisible, <CODE>false</CODE> otherwise
	 */
	public Thing(String aName, String imageName, boolean invisible)
	{
		mName = aName;
		mTextSymbol = aName.substring(0, 2);
		mImageName = imageName;
		mImage = null;
		mMoveCounter = 0;
		mInvisible = invisible;

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
	 * Returns the Image used to depict this thing.
	 *
	 * @return								a non-null Image
	 * @exception	JCavernInternalError	could not retrieve the image from the dictionary.
	 */
	public Image getImage() throws JCavernInternalError
	{
		if (mImage == null)
		{
			mImage = JCavernApplet.getBoardImage(mImageName);
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
	 * Paints this thing by drawing its image, but only if it is not invisible.
	 *
	 * @param		g						a non-null Graphics object used for painting
	 * @param		plotX					offset within the current Graphics for painting this Thing
	 * @param		plotY					offset within the current Graphics for painting this Thing
	 * @exception	JCavernInternalError	couldn't acquire the necessary to paint this Thing
	 */
	public void paint(Graphics g, int plotX, int plotY) throws JCavernInternalError
	{
		if (! getInvisible())
		{
			if (getImage() == null)
			{
				g.drawString(getTextSymbol(), plotX, plotY);
			}
			else
			{
				Image theImage = getImage();
				//g.drawImage(theImage, plotX - theImage.getWidth(null) / 2, plotY - theImage.getHeight(null) / 2, null);
				g.drawImage(theImage,
								plotX - JCavernApplet.kPreferredImageSize / 2, plotY - JCavernApplet.kPreferredImageSize / 2,
								JCavernApplet.kPreferredImageSize, JCavernApplet.kPreferredImageSize, null);
			}
		}
		else
		{
			g.drawString(".", plotX, plotY);
		}
	}
	
	/**
	 * Returns the two character string that is the textual apperance of this thing.
	 * Not currently in used; these kinds of abbreviations were a big part of the Turbo PASCAL version
	 * of Cavern and Glen.
	 *
	 * @return	a non-null String abbreviation for this thing.
	 */
	public String getTextSymbol()
	{
		if (! getInvisible())
		{
			return mTextSymbol;
		}
		else
		{
			return " ";
		}
	}
	
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

