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
	private int		mPoints;
	
	public Armour(String name, int points)
	{
		super(name);
		mPoints = points;
	}
	
	public String toString()
	{
		return getName() + " +" + mPoints;
	}
	
	public Object clone()
	{
		return new Armour(new String(getName()), mPoints);
	}
	
	public int getPoints()
	{
		return mPoints;
	}
}

