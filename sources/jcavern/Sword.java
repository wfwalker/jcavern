/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

public class Sword extends Treasure
{
	private int		mStrength;
	
	public Sword(String aName, int strength)
	{
		super(aName);
		mStrength = strength;
	}
	
	public Object clone()
	{
		return new Sword(new String(getName()), mStrength);
	}
	
	public int getStrength()
	{
		return mStrength;
	}
	
	public String toString()
	{
		return getName() + " +" + (mStrength - 1);
	}
}	
