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
	private String	mAppearance;
	private double	mPoints;
	private double	mWorth;
	private boolean	mInvisible;
	
	public Monster(String name, String appearance, double points, double worth, boolean invisible)
	{
		super(name);
	
		System.out.println("Monster(" + name + ", " + appearance + ", " + points + ", " + worth + ", " + invisible + ")");
		
		mAppearance = appearance;
		mPoints = points;
		mWorth = worth;
		mInvisible = invisible;
	}

	public void doTurn(World aWorld) throws NoSuchThingException
	{
		int	aDirection = aWorld.directionToward(this, aWorld.getPlayer());
		
		try
		{
			aWorld.move(this, aDirection);
		}
		catch (ThingCollisionException tce)
		{
			if (tce.getMovee() instanceof Player)
			{
				aWorld.attack(this, aDirection);
			}
		}
		catch (IllegalLocationException ile)
		{
			System.out.println("illegal location " + ile);
		}
		catch (NoSuchThingException nst)
		{
			System.out.println("no such thing " + nst);
		}
	}
		
	public int computeDamage()
	{
		/*
		dam := Q[i,j].m.points/8 + Random(3) + Q[i,j].m.worth/4
		     - My_arm_points;
		if dam < 0.0 then dam := 0.0;
		exp := exp - dam;
		Message(' The '+Q[i,j].m.name+' hits.',TRUE);
		Plot_Stats(TRUE);
		*/
		
		double	damage = 3 * Math.random() + (mPoints / 8) + (mWorth / 4);
		
		return (int) damage;
	}
	
	public int computeRangedDamage()
	{
		return 0;
	}
	
	public void decrementRangedAttackCount()
	{
	
	}
	
	public void sufferDamage(int theDamage)
	{
		mPoints -= theDamage;
	}
	
	public void gainExperience(Combatant theVictim)
	{
	}
	
	public boolean isDead()
	{
		return mPoints < 0;
	}
	
	public int getWorth()
	{
		return (int) mWorth;
	}

	public Object clone()
	{
		return new Monster(getName(), mAppearance, mPoints, mWorth, mInvisible);
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
	
	public double getPoints()
	{
		return mPoints;
	}
}