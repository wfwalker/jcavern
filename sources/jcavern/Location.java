/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

import java.awt.Rectangle;

/**
 * Represents a location of a Thing within a World.
 */
public class Location
{
	/** * Maximum value for location coordinates. Used for hash value computations. */
	private static final	int		gMaximumCoordinateValue = 1000;
	
	// Constants used for specifying directions
	public static final	int		NORTHWEST = 7;
	public static final	int		NORTH = 8;
	public static final	int		NORTHEAST = 9;
	
	public static final	int		WEST = 4;
	public static final	int		EAST = 6;

	public static final	int		SOUTHWEST = 1;
	public static final	int		SOUTH = 2;
	public static final	int		SOUTHEAST = 3;

	/** * X (east/west or horiziontal) coordinate. Larger values are east and/or right. */
	private int		mX;

	/** * Y (north/south or vertical) coordinate. Larger values are south and/or down. */
	private int		mY;
	
	public boolean inBounds(Rectangle bounds)
	{
		return bounds.contains(mX, mY);
	}
	
	public String toString()
	{
		return "[" + mX + ", " + mY + "]";
	}
	
	public int hashCode()
	{
		return mX * gMaximumCoordinateValue + mY;
	}
	
	public boolean equals(Object aLocation)
	{
		return (((Location) aLocation).mX == mX) && (((Location) aLocation).mY == mY);
	}
	
	public Location(int x, int y)
	{
		mX = x;
		mY = y;
	}

	public Location getNeighbor(int direction)
	{
		int	newX = mX;
		int newY = mY;
		
		switch (direction)
		{
			case 8: newY--; break;
			case 9: newX++; newY--; break;
			case 6: newX++; break;
			case 3: newX++; newY++; break;
			case 2: newY++; break;
			case 1: newX--; newY++; break;
			case 4: newX--; break;
			case 7: newX--; newY--; break;
		}
		
		return new Location(newX, newY);
	}
}
