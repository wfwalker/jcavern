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
	
	public Thing(String aName, String imageName, boolean invisible)
	{
		mName = aName;
		mTextSymbol = aName.substring(0, 2);
		mImageName = imageName;
		mImage = null;
		mMoveCounter = 0;
		mInvisible = invisible;

	}

	public void setInvisible(boolean aBoolean)
	{
		mInvisible = aBoolean;
	}
	
	public boolean getInvisible()
	{
		return mInvisible;
	}

	public Image getImage() throws JCavernInternalError
	{
		if (mImage == null)
		{
			mImage = JCavernApplet.getBoardImage(mImageName);
		}
		
		System.out.println("got image for " + getName() + ", " + mImage);
		return mImage;
	}
	
	public String getImageName()
	{
		return mImageName;
	}
	
	public int getMoveCount()
	{
		return mMoveCounter;
	}
	
	public String toString()
	{
		return getClass().getName() + "." + mName;
	}
	
	/**
	 * Returns the name of this thing.
	 */
	public String getName()
	{
		return mName;
	}
	
	public void doTurn(World aWorld) throws JCavernInternalError
	{
		mMoveCounter++;
	}
	
	public abstract Object clone();
	
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
				g.drawImage(theImage, plotX - theImage.getWidth(null) / 2, plotY - theImage.getHeight(null) / 2, null);
			}
		}
		else
		{
			g.drawString(".", plotX, plotY);
		}
	}
	
	/**
	 * Returns the two character string that is the textual apperance of this thing.
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
	
	public void thingRemoved(World aWorld, Location aLocation) throws JCavernInternalError
	{
	
	}
	
	public void thingPlaced(World aWorld, Location aLocation) throws JCavernInternalError
	{
	
	}
}

