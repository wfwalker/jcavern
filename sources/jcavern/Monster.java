/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

/**
 * A Monster is a combatant that appears in the world.
 */
public class Monster extends Thing implements Cloneable
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