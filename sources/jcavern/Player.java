/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

public class Player extends Thing implements Combatant
{
	private int			mGold;
	private Sword		mSword;
	private int			mArrows;
	private int			mExperience;
	private Mission		mMission;
	
	public Player(String name)
	{
		super(name);
		
		mGold = 0;
		mSword = new Sword(1);
		mArrows = 10;
		mExperience = 10;
		mMission = new Mission();
	}
	
	public void sufferDamage(int theDamage)
	{
		mExperience -= theDamage;

		setChanged();
		notifyObservers();
	}
	
	public void gainExperience(int theExperience)
	{
		mExperience += theExperience;

		setChanged();
		notifyObservers();
	}
	
	public boolean isDead()
	{
		return mExperience < 0;
	}
	
	public int getWorth()
	{
		return 0;
	}
	
	public int computeDamage()
	{
	/*
	     if exp>1.0 then
	        dam := sword_strengh*exp/(2*ln(exp))+Random(strn+3)
	     else
	        dam := 1;
	     m.points := m.points - dam;
	     if m.points <= 0 then
	        Monster_died(sx+dx,sy+dy)
	     else
	        Message('You hit the '+m.name+'.',FALSE);
	*/
		if (mExperience > 1)
		{
			double damage =
					mSword.getStrength() * mExperience / (2 * Math.log(mExperience)) +
					(mSword.getStrength() + 3) * Math.random();
					
			return (int) damage;	
		}
		else
		{
			return 1;
		}
	}
	
	public boolean isCombatant()
	{
		return false;
	}
	
	public String getAppearance()
	{
		return "P";
	}
	
	public int getGold()
	{
		return mGold;
	}
	
	public Sword getSword()
	{
		return mSword;
	}
	
	public int getArrows()
	{
		return mArrows;
	}
	
	public int getExperience()
	{
		return mExperience;
	}

	public void collideWithTree()
	{
		mExperience--;
		
		setChanged();
		notifyObservers();
	}
}
