package jcavern;

import java.util.Observable;
import java.awt.Graphics;

/**
 * Parent class for players, monsters, and trees.
 *
 * @author	Bill Walker
 */
public abstract class Thing extends Observable implements Cloneable
{
	/** * The name of this thing. */
	private String		mName;

	/**
	 * Creates a new thing for the given world and name.
	 */
	public Thing(String aName)
	{
		mName = aName;
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
		g.drawString(getAppearance(), plotX, plotY);
	}
	
	/**
	 * Returns the one character string that is the appearance of this thing.
	 */
	public String getAppearance()
	{
		return "?";
	}
}

