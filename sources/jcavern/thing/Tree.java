/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern.thing;

import jcavern.*;

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
	/**
	 * GraphicalTreeView is a GraphicalThingView for displaying Trees.
	 * It differs from GraphicalCombatantView only in ensuring that Trees
	 * are never highlighted, even though they are combatants.
	 */
	public class GraphicalTreeView extends Combatant.GraphicalCombatantView
	{
		/**
		 * Creates a new GraphicalTreeView with the given image name.
		 *
		 * @param	inImageName		which image to use
		 */
		public GraphicalTreeView(String inImageName)
		{
			super(inImageName);
		}
	
		/**
		 * Decides whether a particular Combatant should be highlighted,
		 * in the context of a particular event.
		 *
		 * @param	anEvent		the event that could trigger highlighting (ignored)
		 * @return				<CODE>false</CODE>, never highlight a Tree.
		 */
		public boolean shouldHighlight(WorldEvent anEvent)
		{
			return false;
		}
	}
	
	/**
	 * Creates a GraphicalThingView appropriate to this Thing.
	 *
	 * @param	inImageName		a non-null String image name
	 * @return					a non-null GraphicalThingView appropriate to this Thing.
	 */
	public GraphicalThingView createGraphicalThingView(String inImageName)
	{
		return new GraphicalTreeView(inImageName);
	}

	/**
	 * Creates a new Tree.
	 */
	public Tree()
	{
		super("Tree", "tree", 0);
	}
	
	/**
	 * Clones this tree. Honestly, though, trees are all alike.
	 *
	 * @return	a Tree
	 */
	public Object clone()
	{
		return new Tree();
	}
	

	/**
	 * Returns whether this tree can attack a combatant.
	 *
	 * @param	aCombatant			a potential attackee
	 * @return	<CODE>false</CODE>. Trees can never attack combatants.
	 */
	public boolean canAttack(Combatant aCombatant)
	{
		return false;
	}
	
	/**
	 * Returns whether this tree can mount a ranged attack on a combatant.
	 *
	 * @param	aCombatant			a potential attackee
	 * @return	<CODE>false</CODE>. Trees can never ranged arrack combatants.
	 */
	public boolean canRangedAttack(Combatant aCombatant)
	{
		//System.out.println(getName() + ".canRangedAttack(" + aCombatant + ")");
		return false;
	}
	
	/**
	 * Returns whether this tree can be attacked by a monster.
	 *
	 * @param	aMonster			a potential attacker
	 * @return	<CODE>false</CODE>. Monsters cannot attack trees.
	 */
	public boolean vulnerableToMonsterAttack(Monster aMonster)
	{
		//System.out.println("Tree.vulnerableToMonsterAttack(Monster)");
		return false;
	}
	
	/**
	 * Returns whether this tree can be attacked by a ranged monster attack.
	 *
	 * @param	aMonster			a potential attacker
	 * @return	<CODE>false</CODE>. Monsters cannot attack trees, not even with arrows.
	 */
	public boolean vulnerableToMonsterRangedAttack(Monster aMonster)
	{
		//System.out.println("Tree.vulnerableToMonsterRangedAttack(Monster)");
		return false;
	}
	
	/**
	 * Returns whether this tree can be attacked by a ranged player attack.
	 *
	 * @param	aPlayer				a potential attacker
	 * @return	<CODE>false</CODE>. Players cannot attack trees remotely.
	 */
	public boolean vulnerableToPlayerRangedAttack(Player aPlayer)
	{
		//System.out.println("Tree.vulnerableToPlayerRangedAttack(" + aPlayer + ")");
		return false;
	}
	
	/**
	 * Returns the special verb used when killing a tree.
	 *
	 * @return		"felled"
	 */
	public String getKilledVerb()
	{
		return "felled";
	}
	
	/**
	 * Gains experience from killing a combatant. Does nothing, since trees don't kill.
	 *
	 * @param	opponent		unused
	 */
	public void gainExperience(Combatant opponent) { }
	
	/**
	 * Computes damage to a combatant. Does nothing, since trees don't attack.
	 *
	 * @param	opponent		unused
	 * @return					0
	 */
	public int computeDamageTo(Combatant opponent) { return 0; }
	
	/**
	 * Computes ranged damage to a combatant. Does nothing, since trees don't attack.
	 *
	 * @param	opponent		unused
	 * @return					0
	 */
	public int computeRangedDamageTo(Combatant opponent) { return 0; }
	
	/**
	 * Returns the number of points a player might get from felling a tree.
	 * Note: environmentalists won't like this. At least some of the tree-like monsters fight back.
	 *
	 * @return	1
	 */
	public int getWorth() { return 1; }
}

