/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

import java.applet.*;

/**
 * A Monster is a combatant that appears in the world.
 */
public class Monster extends Combatant implements Cloneable
{
	private String			mAppearance;
	private double			mWorth;
	private boolean			mInvisible;
	
	/** * Likelihood that this monster wants to move on a given turn */
	private static double	kMoveFraction = 0.7;
	
	public Monster(String name, String appearance, double points, double worth, boolean invisible)
	{
		super(name, (int) points);
	
		// System.out.println("Monster(" + name + ", " + appearance + ", " + points + ", " + worth + ", " + invisible + ")");
		
		mAppearance = appearance;
		mWorth = worth;
		mInvisible = invisible;
	}

	public void doTurn(World aWorld) throws JCavernInternalError
	{
		int	aDirection = aWorld.directionToward(this, aWorld.getPlayer());

		/*		
		{see if each monster wants to move. If so, update both the
		quadrant Q[,] array and the x[],y[] arrays.}
		if Random(10)>3 then begin
		*/
		
		if (Math.random() < kMoveFraction)
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
	
	public boolean canAttack(Combatant aCombatant)
	{
		//System.out.println(getName() + ".canAttack(" + aCombatant + ")");
		return aCombatant.vulnerableToMonsterAttack(this);
	}
	
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
	
	public int computeRangedDamageTo(Combatant opponent)
	{
		return 0;
	}
	
	public void decrementRangedAttackCount()
	{
	
	}
	
	public void gainExperience(Combatant theVictim)
	{
	}
	
	public int getWorth()
	{
		return (int) mWorth;
	}

	public Object clone()
	{
		return new Monster(getName(), mAppearance, getPoints(), mWorth, mInvisible);
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
}