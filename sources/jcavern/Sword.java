/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

/**
 * A Sword is a Thing the player uses to attack combatants.
 *
 * @author	Bill Walker
 * @version	$Id$
 */
public class Sword extends Treasure
{
	public static final double	kWearFraction = 0.1;
	
	private int					mStrength;
	private int					mCharges;
	
	public Sword(String aName, int strength)
	{
		super(aName);
		mStrength = strength;
		
		/* charges := charges + Random(10) + 2; */
		mCharges = (int) (Math.random() * 10 + 2);
	}
	
	public void decrementCharges()
	{
		mCharges--;
	}
	
	public boolean isDepleted()
	{
		return mCharges <= 0;
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
