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
	private Image		mImage;

	/** * The name of the image to use for this thing. */
	private String		mImageName;

	/** * The textual appearance of the Thing (usually two letters). */
	private String		mTextSymbol;
	
	/**
	 * Creates a new thing for the given name.
	 */
	public Thing(String aName)
	{
		mName = aName;
		mTextSymbol = aName.substring(0, 2);
		mImageName = "none";
		mImage = null;
	}
	
	/**
	 * Creates a new thing for the given name.
	 */
	public Thing(String aName, String imageName)
	{
		mName = aName;
		mImageName = imageName;
		mImage = null;
	}
	
	public Image getImage()
	{
		if ((mImage == null) && (JCavernApplet.current() != null))
		{
			mImage = JCavernApplet.current().getBoardImage(mImageName);
		}
		
		return mImage;
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
	
	/**
	 * Returns the two character string that is the textual apperance of this thing.
	 */
	public String getTextSymbol()
	{
		return mTextSymbol;
	}
}

