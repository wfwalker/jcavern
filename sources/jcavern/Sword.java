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
	
	public Sword(int strength)
	{
		mStrength = strength;
	}
	
	public int getStrength()
	{
		return mStrength;
	}
	
	public String toString()
	{
		return "Sword +" + (mStrength - 1);
	}
}	
