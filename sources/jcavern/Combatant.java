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
public interface Combatant
{
	public int computeDamage();
	
	public void sufferDamage(int theDamage);
	
	public void gainExperience(int theExperience);
	
	public int getWorth();
	
	public boolean isDead();
}
