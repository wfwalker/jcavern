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
					if (tce.getMovee() instanceof Player)
					{
						attack(aWorld, tce.getMovee());
					}
				}
				catch (NonCombatantException nce)
				{
					System.out.println("non combatant " + nce);
				}
				catch (JCavernInternalError nst)
				{
					System.out.println("no such thing " + nst);
				}
			}
			catch (IllegalLocationException ile)
			{
				System.out.println("illegal location " + ile);
			}
		}
	}
		
	public int computeDamage(Combatant opponent)
	{
		/*
		dam := Q[i,j].m.points/8 + Random(3) + Q[i,j].m.worth/4
		     - My_arm_points;
		if dam < 0.0 then dam := 0.0;
		exp := exp - dam;
		Message(' The '+Q[i,j].m.name+' hits.',TRUE);
		Plot_Stats(TRUE);
		*/
		
		if (opponent instanceof Tree)
		{
			return 0;
		}
		else
		{
			return (int) (3 * Math.random() + (getPoints() / 8) + (getWorth() / 4));
		}
	}
	
	public int computeRangedDamage(Combatant opponent)
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