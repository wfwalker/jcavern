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
public class Monster extends Thing implements Cloneable, Combatant
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
		return 1;
	}
	
	public void sufferDamage(int theDamage)
	{
		mPoints -= theDamage;
	}
	
	public void gainExperience(int theExperience)
	{
		mPoints += theExperience;
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
		return mAppearance;
	}
	
	public double getPoints()
	{
		return mPoints;
	}
}