/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

public class Player extends Combatant
{
	private int			mGold;
	private Sword		mSword;
	private int			mArrows;
	private int			mExperience;
	private Mission		mMission;
	
	public Player(String name)
	{
		/*
		exp := 24.0; arrows := 20; gold := 10; which_swd := 0;
		*/

		super(name);
		
		mGold = 10;
		mSword = new Sword(1);
		mArrows = 20;
		mExperience = 24;
	}
	
	public void setMission(Mission aMission)
	{
		JCavernApplet.log(getName()+ "'s mission is " + aMission);
		mMission = aMission;
	}
	
	public void sufferDamage(int theDamage)
	{
		mExperience -= theDamage;

		setChanged();
		notifyObservers();
	}
	
	public void gainExperience(Combatant theVictim)
	{
		mExperience += theVictim.getWorth();
		
		mMission.adjustQuota(theVictim);

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
	
	public void decrementRangedAttackCount()
	{
		mArrows--;
		setChanged();
		notifyObservers();	
	}
	
	public int computeRangedDamage()
	{
	/*
		Message(' Arrow hit the '+Q[xx,yy].m.name,FALSE);
		dam := 4 + int(exp / 10);
		Q[xx,yy].m.points := Q[xx,yy].m.points - dam;
		if Q[xx,yy].m.points<0 then Monster_died(xx,yy);
	*/

		double damage = 4 + mExperience / 10.0;
		
		return (int) damage;
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
