/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

import java.awt.Rectangle;
import java.util.Vector;

/**
 * Represents a location of a Thing within a World.
 *
 * @author	Bill Walker
 * @version	$Id$
 */
public class Location
{
	/** * Maximum value for location coordinates. Used for hash value computations. */
	private static final	int		gMaximumCoordinateValue = 1000;
	
	// Constants used for specifying directions
	
	/** * Constant indicating northwest (up and left) */
	public static final	int		NORTHWEST = 7;
	
	/** * Constant indicating north (up) */
	public static final	int		NORTH = 8;
	
	/** * Constant indicating northeast (up and right) */
	public static final	int		NORTHEAST = 9;
	
	/** * Constant indicating west (left) */
	public static final	int		WEST = 4;
	
	/** * Constant indicating east (right) */
	public static final	int		EAST = 6;

	/** * Constant indicating southwest (down and left) */
	public static final	int		SOUTHWEST = 1;
	
	/** * Constant indicating south (down) */
	public static final	int		SOUTH = 2;
	
	/** * Constant indiciating southeast (down and right) */
	public static final	int		SOUTHEAST = 3;

	/** * X (east/west or horiziontal) coordinate. Larger values are east and/or right. */
	private int		mX;

	/** * Y (north/south or vertical) coordinate. Larger values are south and/or down. */
	private int		mY;
	
	/**
	 * Returns a String description of the given direction.
	 *
	 * @param	aDirection		one of the direction constants.
	 * @return					a non-null String description.
	 */
	public static String directionToString(int aDirection)
	{
		switch (aDirection)
		{
			case NORTH: return "north";
			case NORTHEAST: return "northeast";
			case EAST: return "east";
			case SOUTHEAST: return "southeast";
			case SOUTH: return "south";
			case SOUTHWEST: return "southwest";
			case WEST: return "west";
			case NORTHWEST: return "northwest";
		}
		
		throw new IllegalArgumentException("no such direction");
	}
	
	/**
	 * Returns whether a Location is within the given rectangle.
	 *
	 * @param	bounds		a rectangle
	 * @return				<CODE>true</CODE> if the Location is inside the rectangle, <CODE>false</CODE> otherwise
	 */
	public boolean inBounds(Rectangle bounds)
	{
		return bounds.contains(mX, mY);
	}
	
	/**
	 * Returns a Location no closer to the bounds than the supplied inset.
	 *
	 * @param	bounds		a rectangle
	 * @param	inset		an integer inset from the edge of the rectangle
	 * @return				a Location that is at least <I>inset</I> cells from the edge of the rectangle
	 */
	public Location enforceMinimumInset(Rectangle bounds, int inset)
	{
		// make sure newX,Y are no larger than the largest acceptable value
		int newX = Math.min(mX, (bounds.x + bounds.width - 1) - inset);
		int newY = Math.min(mY, (bounds.y + bounds.height - 1) - inset);
		
		// make sure newX, Y are no smaller than the smallest acceptable value
		newX = Math.max(newX, bounds.x + inset);
		newY = Math.max(newY, bounds.y + inset);
		
		//System.out.println(this + "enforceMinimumInset(" + bounds + ", " + inset + ") = " + newX + ", " + newY);
		return new Location(newX, newY);
	}
	
	/**
	 * Returns the x coordinate of this location.
	 *
	 * @return		the x coordinate of this location.
	 */
	public int getX()
	{
		return mX;
	}
	
	/**
	 * Returns the y coordinate of this location.
	 *
	 * @return		the y coordinate of this location.
	 */
	public int getY()
	{
		return mY;
	}
	
	/**
	 * Computes the number of moves between Locations.
	 * We use a modified Manhattan distance metric, with diagonal
	 * moves allowed.
	 *
	 * @param	aLocation	the non-null Location to which the distance will be computed.
	 * @return				the number of spaces between this location and the given location
	 */ 
	public int distanceTo(Location aLocation)
	{
		return Math.max(Math.abs(aLocation.getX() - mX), Math.abs(aLocation.getY() - mY));
	}
	
	/**
	 * Returns a string-based representation of this Location.
	 *
	 * @return		a String including the x and y coordinates.
	 */
	public String toString()
	{
		return "[" + mX + ", " + mY + "]";
	}
	
	/**
	 * Returns the hash code for this Location. Used for efficient equality tests amongst Locations.
	 *
	 * @return		the hash code for this Location
	 */
	public int hashCode()
	{
		return mX * gMaximumCoordinateValue + mY;
	}
	
	/**
	 * Returns whether this Location is the same as another Location.
	 *
	 * @param	aLocation		a non-null Location to compare this location against.
	 * @return					<CODE>true</CODE> if the two Locations have the same coordinates, <CODE>false</CODE> otherwise
	 */
	public boolean equals(Object aLocation)
	{
		return (((Location) aLocation).mX == mX) && (((Location) aLocation).mY == mY);
	}
	
	/**
	 * Creates a new Location.
	 *
	 * @param	x		the x coordinate
	 * @param	y		the y coordinate
	 */
	public Location(int x, int y)
	{
		mX = x;
		mY = y;
	}

	/**
	 * Returns a neighboring Location in the given direction.
	 *
	 * @param	direction	one of the direction codes
	 * @return				a Location adjacent to this Location
	 */
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
			default: throw new IllegalArgumentException("No such direction");
		}
		
		return new Location(newX, newY);
	}
	
	/**
	 * Returns a Vector of all the neighbors of this Location.
	 *
	 * @return		a non-null Vector of Locations neighboring this Location.
	 */
	public Vector getNeighbors()
	{
		Vector neighbors = new Vector(8);
		
		neighbors.addElement(getNeighbor(NORTH));
		neighbors.addElement(getNeighbor(NORTHEAST));
		neighbors.addElement(getNeighbor(EAST));
		neighbors.addElement(getNeighbor(SOUTHEAST));
		neighbors.addElement(getNeighbor(SOUTH));
		neighbors.addElement(getNeighbor(SOUTHWEST));
		neighbors.addElement(getNeighbor(WEST));
		neighbors.addElement(getNeighbor(NORTHWEST));

		return neighbors;
	}

	/**
	 * Returns a direction toward the given location.
	 *
	 * @param		anotherLocation		a Location to move toward
	 * @return							the direction code that moves toward that Location
	 */
	public int getDirectionToward(Location anotherLocation)
	{
		double	deltaX = anotherLocation.getX() - mX;
		double	deltaY = anotherLocation.getY() - mY;
		double	degrees = (360.0 / (2 * Math.PI)) * Math.atan(deltaY / deltaX);
		
		if (Math.abs(degrees) > 67.5) // either north or south
		{
			return deltaY > 0 ? SOUTH : NORTH;
		}
		else if (Math.abs(degrees) > 22.5) // one of the four diagonals
		{
			if (deltaY < 0)
			{
				return deltaX > 0 ? NORTHEAST : NORTHWEST;
			}
			else
			{
				return deltaX > 0 ? SOUTHEAST : SOUTHWEST;
			}
		}
		else // either east or west
		{
			return deltaX > 0 ? EAST : WEST;
		}
	}

	/**
	 * Returns a Location toward the given location.
	 *
	 * @param		anotherLocation		a Location to move toward
	 * @return							a Location toward the given Location
	 */
	public Location getNeighborToward(Location anotherLocation)
	{
		return getNeighbor(getDirectionToward(anotherLocation));
	}
}
