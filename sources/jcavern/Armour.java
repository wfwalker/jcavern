/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

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

