/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

import java.util.Vector;
import java.awt.*;

/**
 * The Player class represents the current state of the player.
 */
public class Player extends Combatant
{
	/** * How many gold pieces does this player have. */
	private int			mGold;

	/** * What kind of sword does this player have. */
	private Sword		mSword;
	
	/** * What kind of armour this player is using. */
	private Armour		mArmour;
	
	private Vector		mItems;

	/** * How many arrows does this player have. */
	private int			mArrows;

	/** * This player's current mission. */
	private Mission		mMission;

	/** * Whether this player is currently inside a castle. */	
	private	Castle		mCastle;
	
	/**
	 * Creates a beginning player with the default initial statistics.
	 *
	 * @param	name	a non-null String containing the name of the new player.
	 */
	public Player(String name)
	{
		/*
		exp := 24.0; arrows := 20; gold := 10; which_swd := 0;
		*/

		super(name, 24);
		
		mItems = new Vector();

		mGold = 10;
		receiveItem(new Sword("Sword", 1));
		receiveItem(new Armour("Armour", 5));
		mArrows = 20;
	}
	
	/**
	 * Creates a new player with the given statistics.
	 *
	 * @param	name		a non-null String containing the name of the player.
	 * @param	gold		how many gold pieces the player has
	 * @param	sword		what kind of sword does this player have
	 * @param	arrows		how many arrows does this player have
	 * @param	points		how many points does this player have
	 * @param	mission		a non-null Mission this player must complete
	 */
	public Player(String name, int gold, Sword sword, Armour armour, int arrows, int points, Mission mission)
	{
		super(name, points);
		
		mGold = gold;
		mSword = sword;
		mArmour = armour;
		mArrows = arrows;
		mMission = mission;
	}
	
	/**
	 * Returns a clone of this player. This also requires cloning the player's Swords and other items.
	 */
	public Object clone()
	{
		return new Player(getName(), mGold, (Sword) mSword.clone(), (Armour) mArmour.clone(), mArrows, getPoints(), mMission);
	}
	
	public void setMission(Mission aMission)
	{
		JCavernApplet.log(getName()+ "'s mission is " + aMission);
		mMission = aMission;
	}
	
	public void sufferDamage(int theDamage)
	{
		int adjustedDamage = theDamage - getArmourPoints();
		
		if (adjustedDamage < 0)
		{
			adjustedDamage = 0;
		}
		
		super.sufferDamage(adjustedDamage);

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
	
	public boolean canAttack(Combatant aCombatant)
	{
		return aCombatant.vulnerableToPlayerAttack(this);
	}
	
	public boolean canRangedAttack(Combatant aCombatant)
	{
		System.out.println(getName() + ".canRangedAttack(" + aCombatant + ")");
		return (mArrows > 0) && aCombatant.vulnerableToPlayerRangedAttack(this);
	}
	
	public boolean vulnerableToMonsterAttack(Monster aMonster)
	{
		return getCastle() == null;
	}
	
	public int computeDamageTo(Combatant opponent)
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
			Sword aSword = getSword();
			
			double damage =
					aSword.getStrength() * getPoints() / (2 * Math.log(getPoints())) +
					(aSword.getStrength() + 3) * Math.random();
					
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
	
	public int computeRangedDamageTo(Combatant opponent)
	{
	/*
		Message(' Arrow hit the '+Q[xx,yy].m.name,FALSE);
		dam := 4 + int(exp / 10);
		Q[xx,yy].m.points := Q[xx,yy].m.points - dam;
		if Q[xx,yy].m.points<0 then Monster_died(xx,yy);
	*/

		return (int) (4 + getPoints() / 10.0);
	}

	public String getAppearance()
	{
		return "P";
	}	
	
	public int getGold()
	{
		return mGold;
	}
	
	public void incrementGold(int delta)
	{
		mGold += delta;

		setChanged();
		notifyObservers();
	}
	
	public Sword getSword()
	{
		for (int index = 0; index < mItems.size(); index++)
		{
			Treasure aTreasure = (Treasure) mItems.elementAt(index);
			
			if (aTreasure instanceof Sword)
			{
				return (Sword) aTreasure;
			}
		}
		
		return null;
	}
	
	public Vector getItems()
	{
		return mItems;
	}
	
	private int getArmourPoints()
	{
		int armourPoints = 0;
		
		for (int index = 0; index < mItems.size(); index++)
		{
			Treasure aTreasure = (Treasure) mItems.elementAt(index);
			
			if (aTreasure instanceof Armour)
			{
				armourPoints += ((Armour) aTreasure).getPoints();
			}
		}
		
		return armourPoints;
	}
	
	public int getArrows()
	{
		return mArrows;
	}
	
	public void receiveItem(Treasure anItem)
	{
		System.out.println(getName() + " received " + anItem);
		
		mItems.addElement(anItem);
		
		setChanged();
		notifyObservers();
	}
	
	public Mission getMission()
	{
		return mMission;
	}
	
	public Castle getCastle()
	{
		return mCastle;
	}
	
	public void setCastle(Castle aCastle)
	{
		mCastle = aCastle;
	}
	
	public void paint(Graphics g, int plotX, int plotY)
	{
		Image theImage = JCavernApplet.current().getBoardImage("player");
		
		g.drawImage(theImage, plotX - theImage.getWidth(null) / 2, plotY - theImage.getHeight(null) / 2, null);

		if (getCastle() != null)
		{
			getCastle().paint(g, plotX, plotY);
		}		
	}
}
