/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

public class Tree extends Combatant
{
	public Tree()
	{
		super("Tree", 0);
	}
	
	public Object clone()
	{
		return new Tree();
	}
	
	public String getAppearance() { return "T"; }
	
	public void gainExperience(Combatant opponent) { }
	
	public void decrementRangedAttackCount() { }
	
	public int computeDamage(Combatant opponent) { return 0; }
	
	public int computeRangedDamage(Combatant opponent) { return 0; }
	
	public int getWorth() { return 0; }
}

