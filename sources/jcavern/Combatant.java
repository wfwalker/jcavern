/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

/**
 * Represents a situation in which an asked-for thing was not found.
 */
public abstract class Combatant extends Thing
{
	public abstract int computeDamage();
	
	public abstract int computeRangedDamage();
	
	public abstract void decrementRangedAttackCount();
	
	public abstract void sufferDamage(int theDamage);
	
	public abstract void gainExperience(Combatant theVictim);
	
	public abstract int getWorth();
	
	public abstract boolean isDead();
	
	public Combatant(String name)
	{
		super(name);
	}
}
