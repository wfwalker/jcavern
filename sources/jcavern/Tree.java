/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

import java.awt.*;

/**
 * Trees inhabit the World and look nice. They can be chopped down by Players,
 * but not by Monsters or other Trees.
 *
 * @author	Bill Walker
 * @version	$Id$
 */
public class Tree extends Combatant
{
	public Tree()
	{
		super("Tree", "tree", 0);
	}
	
	public Object clone()
	{
		return new Tree();
	}
	
	public boolean canAttack(Combatant aCombatant)
	{
		return false;
	}
	
	public boolean canRangedAttack(Combatant aCombatant)
	{
		System.out.println(getName() + ".canRangedAttack(" + aCombatant + ")");
		return false;
	}
	
	public boolean vulnerableToMonsterAttack(Monster aMonster)
	{
		//System.out.println("Tree.vulnerableToMonsterAttack(Monster)");
		return false;
	}
	
	public boolean vulnerableToMonsterRangedAttack(Monster aMonster)
	{
		//System.out.println("Tree.vulnerableToMonsterRangedAttack(Monster)");
		return false;
	}
	
	public boolean vulnerableToPlayerRangedAttack(Player aPlayer)
	{
		System.out.println("Tree.vulnerableToPlayerRangedAttack(" + aPlayer + ")");
		return false;
	}
	
	protected String getKilledVerb()
	{
		return "felled";
	}
	
	public String getAppearance() { return "T"; }
	
	public void gainExperience(Combatant opponent) { }
	
	public void decrementRangedAttackCount() { }
	
	public int computeDamageTo(Combatant opponent) { return 0; }
	
	public int computeRangedDamageTo(Combatant opponent) { return 0; }
	
	public int getWorth() { return 1; }
	
	public void paint(Graphics g, int plotX, int plotY)
	{
		Image theImage = JCavernApplet.current().getBoardImage("tree");
		
		g.drawImage(theImage, plotX - theImage.getWidth(null) / 2, plotY - theImage.getHeight(null) / 2, null);
	}

}

