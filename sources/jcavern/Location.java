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
	
	public int getX()
	{
		return mX;
	}
	
	public int getY()
	{
		return mY;
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
			case NORTH: newY--; break;
			case NORTHEAST: newX++; newY--; break;
			case EAST: newX++; break;
			case SOUTHEAST: newX++; newY++; break;
			case SOUTH: newY++; break;
			case SOUTHWEST: newX--; newY++; break;
			case WEST: newX--; break;
			case NORTHWEST: newX--; newY--; break;
		}
		
		return new Location(newX, newY);
	}

	public Location getNeighborToward(Location anotherLocation)
	{
		double	deltaX = anotherLocation.getX() - mX;
		double	deltaY = anotherLocation.getY() - mY;
		double	degrees = (360.0 / (2 * Math.PI)) * Math.atan(deltaY / deltaX);
		
		if (Math.abs(degrees) > 67.5) // either north or south
		{
			return getNeighbor(deltaY > 0 ? SOUTH : NORTH);
		}
		else if (Math.abs(degrees) > 22.5) // one of the four diagonals
		{
			if (deltaY < 0)
			{
				return getNeighbor(deltaX > 0 ? NORTHEAST : NORTHWEST);
			}
			else
			{
				return getNeighbor(deltaX > 0 ? SOUTHEAST : SOUTHWEST);
			}
		}
		else // either east or west
		{
			return getNeighbor(deltaX > 0 ? EAST : WEST);
		}
	}
}
