/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

public class Treasure
{
	private String	mName;
	private boolean	mPlayerHas;
	private boolean	mPlayerUse;
}

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
}	

public class Armour extends Treasure
{
	private int		mPoints;
}

public class Other extends Treasure
{
	private int		mPower;
}