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
	/** * The probability of losing a charge on each sword stroke. */
	public static final double	kWearFraction = 0.1;
	
	/** * The strength of the sword. */
	private int					mStrength;
	
	/** * The number of charges remaining on the sword. */
	private int					mCharges;
	
	/**
	 * Creates a new sword with the given characteristics.
	 *
	 * @param	aName		a non-null String name of the sword.
	 * @param	strength	the strength of the sword.
	 */
	public Sword(String aName, int strength)
	{
		/* charges := charges + Random(10) + 2; */
		this(aName, strength, (int) (Math.random() * 10 + 2));
	}
	
	/**
	 * Creates a new sword with the given characteristics.
	 *
	 * @param	aName		a non-null String name of the sword.
	 * @param	strength	the strength of the sword.
	 * @param	charges		the number of charges on the sword.
	 */
	public Sword(String aName, int strength, int charges)
	{
		super(aName);
		mStrength = strength;
		mCharges = charges;
	}
	
	/**
	 * Decrements one charge from this sword.
	 */
	public void decrementCharges()
	{
		mCharges--;
	}
	
	/**
	 * Returns whether this sword's charges have been completely depleted.
	 *
	 * @return	<CODE>true</CODE> if the sword was depleted, <CODE>false</CODE> otherwise
	 */
	public boolean isDepleted()
	{
		return mCharges <= 0;
	}
	
	/**
	 * Implements the cloning interface.
	 *
	 * @return	a clone of this Sword.
	 */
	public Object clone()
	{
		return new Sword(new String(getName()), mStrength);
	}
	
	/**
	 * Returns the strength of this sword.
	 *
	 * @return	the strength of this sword
	 */
	public int getStrength()
	{
		return mStrength;
	}
	
	/**
	 * Returns a String-based representation of this Sword.
	 *
	 * @return	a String-based representation of this Sword.
	 */
	public String toString()
	{
		return getName() + " +" + (mStrength - 1);
	}
}	
