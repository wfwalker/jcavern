package jcavern;

import java.util.Observable;
import java.awt.*;

/**
 * Parent class for players, monsters, and trees.
 *
 * @author	Bill Walker
 * @version	$Id$
 */
public abstract class Thing extends Observable implements Cloneable
{
	/** * The name of this thing. */
	private String		mName;
	
	/** * The name of the image to use for this thing. */
	private String		mImageName;

	/**
	 * Creates a new thing for the given name.
	 */
	public Thing(String aName)
	{
		mName = aName;
	}
	
	/**
	 * Creates a new thing for the given name.
	 */
	public Thing(String aName, String imageName)
	{
		mName = aName;
		mImageName = imageName;
	}
	
	public String getImageName()
	{
		return mImageName;
	}
	
	public String toString()
	{
		return getClass().getName() + "." + mName;
	}
	
	/**
	 * Returns the name of this thing.
	 */
	protected String getName()
	{
		return mName;
	}
	
	public void doTurn(World aWorld) throws JCavernInternalError
	{
	
	}
	
	public abstract Object clone();
	
	public void paint(Graphics g, int plotX, int plotY)
	{
		if (mImageName == null)
		{
			g.drawString(getAppearance(), plotX, plotY);
		}
		else
		{
			Image theImage = JCavernApplet.current().getBoardImage(mImageName);
		
			g.drawImage(theImage, plotX - theImage.getWidth(null) / 2, plotY - theImage.getHeight(null) / 2, null);
		}
	}
	
	/**
	 * Returns the one character string that is the appearance of this thing.
	 */
	public String getAppearance()
	{
		return "?";
	}
}

