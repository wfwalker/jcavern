/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

/**
 * Armour represents an item which, when used, reduces the amount of
 * damage done by each monster's attack.
 *
 * @author	Bill Walker
 * @version	$Id$
 */
public class Armour extends Treasure
{
	/** * How many points this Armour is worth. */
	private int		mPoints;
	
	/**
	 * Creates a new Armour.
	 *
	 * @param	name	the name of this armour.
	 * @param	points	how many points the armour is worth.
	 */
	public Armour(String name, int points)
	{
		super(name);
		mPoints = points;
	}
	
	/**
	 * Creates a string-based representation of this Armour.
	 *
	 * @return	a string-based representation of this Armour.
	 */
	public String toString()
	{
		return getName() + " +" + mPoints;
	}
	
	/**
	 * Creates a clone of this Armour.
	 *
	 * @return	a clone of this Armour.
	 */
	public Object clone()
	{
		return new Armour(new String(getName()), mPoints);
	}
	
	/**
	 * Returns the number of points this armour is worth.
	 *
	 * @return		the number of points this armour is worth.
	 */
	public int getPoints()
	{
		return mPoints;
	}
}

