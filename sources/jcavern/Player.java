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
	private Mission		mMission;
	
	public Player(String name)
	{
		/*
		exp := 24.0; arrows := 20; gold := 10; which_swd := 0;
		*/

		super(name, 24);
		
		mGold = 10;
		mSword = new Sword(1);
		mArrows = 20;
	}
	
	public Player(String name, int gold, Sword sword, int arrows, int points, Mission mission)
	{
		super(name, points);
		
		mGold = gold;
		mSword = sword;
		mArrows = arrows;
		mMission = mission;
	}
	
	public Object clone()
	{
		return new Player(getName(), mGold, mSword, mArrows, getPoints(), mMission);
	}
	
	public void setMission(Mission aMission)
	{
		JCavernApplet.log(getName()+ "'s mission is " + aMission);
		mMission = aMission;
	}
	
	public void sufferDamage(int theDamage)
	{
		super.sufferDamage(theDamage);

		setChanged();
		notifyObservers();
	}
	
	public void gainExperience(Combatant theVictim)
	{
		super.gainExperience(theVictim);
		
		if (mMission != null)
		{
			mMission.adjustQuota(theVictim);
		}
		
		setChanged();
		notifyObservers();
	}
	
	public int getWorth()
	{
		return 0;
	}
	
	public int computeDamage(Combatant opponent)
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
		if (getPoints() > 1)
		{
			double damage =
					mSword.getStrength() * getPoints() / (2 * Math.log(getPoints())) +
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
	
	public int computeRangedDamage(Combatant opponent)
	{
	/*
		Message(' Arrow hit the '+Q[xx,yy].m.name,FALSE);
		dam := 4 + int(exp / 10);
		Q[xx,yy].m.points := Q[xx,yy].m.points - dam;
		if Q[xx,yy].m.points<0 then Monster_died(xx,yy);
	*/

		if (opponent instanceof Tree)
		{
			return 0;
		}
		else
		{
			return (int) (4 + getPoints() / 10.0);
		}
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
	
	public Mission getMission()
	{
		return mMission;
	}
}
