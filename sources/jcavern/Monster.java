/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

import java.applet.*;
import java.awt.*;
import java.net.URL;

/**
 * A Monster is a combatant that appears in the world.
 *
 * @author	Bill Walker
 * @version	$Id$
 */
public class Monster extends Combatant implements Cloneable
{
	/** * The textual appearance of the Monster (usually two letters). */
	private String			mAppearance;
	
	/** * How many points the monster is worth when a Player kills it. */
	private double			mWorth;
	
	/** * Is this monster invisible. */
	private boolean			mInvisible;
	
	/** * Likelihood that this monster wants to move on a given turn */
	//private static double	kMoveFraction = 0.7;
	private static int		kMoveRadius = 4;
	
	/**
	 * Creates a new monster with the given parameters.
	 */
	public Monster(String name, String imageName, String appearance, double points, double worth, boolean invisible)
	{
		super(name, imageName, (int) points);
	
		// System.out.println("Monster(" + name + ", " + appearance + ", " + points + ", " + worth + ", " + invisible + ")");
		
		mAppearance = appearance;
		mWorth = worth;
		mInvisible = invisible;
	}

	/**
	 * Performs one turn by moving toward or attacking an adjacent Player.
	 */
	public void doTurn(World aWorld) throws JCavernInternalError
	{
		Player	aPlayer = aWorld.getPlayer();
		
		if ((aPlayer == null) || (! aPlayer.vulnerableToMonsterAttack(this)))
		{
			return;
		}
		
		int		aDirection = aWorld.getDirectionToward(this, aPlayer);
		int		aDistance = aWorld.getDistanceBetween(aPlayer, this);
		
		//System.out.println(getName() + " at " + aWorld.getLocation(this) + " " +
		//						aDistance + " away, " + Location.directionToString(aDirection));

		/*		
		{see if each monster wants to move. If so, update both the
		quadrant Q[,] array and the x[],y[] arrays.}
		if Random(10)>3 then begin
		*/
		
		// New scheme -- if the monster is within a certain distance of the player, it moves.
		
		if (aDistance < kMoveRadius)
		{
			try
			{
				aWorld.move(this, aDirection);
			}
			catch (ThingCollisionException tce)
			{
				try
				{
					attack(aWorld, tce.getMovee());
				}
				catch (NonCombatantException nce)
				{
					System.out.println(getName() + " tried to attack non combatant " + tce.getMovee());
				}
				catch (JCavernInternalError nst)
				{
					System.out.println(getName() + " tried to attack, encountered internal error " + nst);
					throw nst;
				}
			}
			catch (IllegalLocationException ile)
			{
				System.out.println(getName() + " tried to move off edge of world " + ile);
			}
		}
	}
	
	/**
	 * Returns whether this monster can attack the given combatant.
	 */
	public boolean canAttack(Combatant aCombatant)
	{
		//System.out.println(getName() + ".canAttack(" + aCombatant + ")");
		return aCombatant.vulnerableToMonsterAttack(this);
	}
	
	/**
	 * Returns whether this monster can make a ranged attack the given combatant.
	 */	
	public boolean canRangedAttack(Combatant aCombatant)
	{
		System.out.println(getName() + ".canRangedAttack(" + aCombatant + ")");
		return aCombatant.vulnerableToMonsterRangedAttack(this);
	}
	
	/**
	 * Returns whether Monsters are vulnerable to attack from other Monsters.
	 * By default, this is always false.
	 */
	public boolean vulnerableToMonsterAttack(Monster aMonster)
	{
		//System.out.println("Monster.vulnerableToMonsterAttack(Monster)");
		return false;
	}
		
	/**
	 * Returns how much damage this monster does to this opponent.
	 */
	public int computeDamageTo(Combatant opponent)
	{
		/*
		dam := Q[i,j].m.points/8 + Random(3) + Q[i,j].m.worth/4
		     - My_arm_points;
		if dam < 0.0 then dam := 0.0;
		exp := exp - dam;
		Message(' The '+Q[i,j].m.name+' hits.',TRUE);
		Plot_Stats(TRUE);
		*/
		
		return (int) (3 * Math.random() + (getPoints() / 8) + (getWorth() / 4));
	}
	
	/**
	 * Returns how much damage this monster does to this opponent during a ranged attack.
	 */
	public int computeRangedDamageTo(Combatant opponent)
	{
		return 0;
	}
	
	/**
	 * Decrements how many ranged attacks this monster can perform.
	 * This simulates using up a supply of arrows.
	 */
	public void decrementRangedAttackCount()
	{
	
	}

	protected void reportResultAgainst(Combatant opponent, int outcome, int damage)
	{
		if (opponent instanceof Player)
		{
			super.reportResultAgainst(opponent, outcome, damage);
		}
	}

	/**
	 * Gains experience points when this monster kills a combatant.
	 * Not currently in use.
	 */
	public void gainExperience(Combatant theVictim)
	{
	}
	
	/**
	 * Returns how many points this Monster is worth when vanquished.
	 */
	public int getWorth()
	{
		return (int) mWorth;
	}

	/**
	 * Returns a copy of this monster.
	 * How cool is cloning a monster!
	 */
	public Object clone()
	{
		return new Monster(getName(), getImageName(), mAppearance, getPoints(), mWorth, mInvisible);
	}
	
	public String getAppearance()
	{
		if (! mInvisible)
		{
			return mAppearance;
		}
		else
		{
			return ".";
		}
	}

	public void paint(Graphics g, int plotX, int plotY)
	{
		if (! mInvisible)
		{
			super.paint(g, plotX, plotY);
		}
		else
		{
			g.drawString(".", plotX, plotY);
		}
	}

}