/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

import jcavern.ui.*;
import java.util.Vector;
import java.awt.*;

/**
 * The Player class represents the current state of the player.
 *
 * @author	Bill Walker
 * @version	$Id$
 */
public class Player extends Combatant
{
	/** * How many gold pieces does this player have. */
	private int			mGold;

	/** * Which items the player is currently using. */
	private Vector		mInUseItems;
	
	/** * Which items the player owns, but is not currently using. */
	private Vector		mUnusedItems;

	/** * How many arrows does this player have. */
	private int			mArrows;

	/** * This player's current mission. */
	private Mission		mMission;

	/** * Whether this player is currently inside a castle. */	
	private	Castle		mCastle;
	
	/**
	 * Moves an item from the unused list to the in use list.
	 */
	public void startUsing(Treasure anItem)
	{
		mUnusedItems.removeElement(anItem);
		mInUseItems.addElement(anItem);
		
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Drop an item.
	 */
	public void drop(Treasure anItem)
	{
		mUnusedItems.removeElement(anItem);
		mInUseItems.removeElement(anItem);
		
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Retrieves an unused item by index.
	 */
	public Treasure getUnusedTreasureAt(int unusedIndex)
	{
		if ((unusedIndex < 0) || (unusedIndex >= mUnusedItems.size()))
		{
			throw new IllegalArgumentException("No such item to start using");
		}
		
		return (Treasure) mUnusedItems.elementAt(unusedIndex);
	}
	
	/**
	 * Stops using the given item.
	 */
	public void stopUsing(Treasure anItem)
	{
		mInUseItems.removeElement(anItem);
		mUnusedItems.addElement(anItem);
		
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Retrives an in use item by index.
	 */
	public Treasure getInUseTreasureAt(int inUseIndex)
	{
		if ((inUseIndex < 0) || (inUseIndex >= mInUseItems.size()))
		{
			throw new IllegalArgumentException("No such item to stop using");
		}
		
		return (Treasure) mInUseItems.elementAt(inUseIndex);
	}

	/**
	 * Retrieves the Vector of in use items.
	 */
	public Vector getInUseItems()
	{
		return mInUseItems;
	}
	
	/**
	 * Retrieves sthe Vector of items not in use.
	 */
	public Vector getUnusedItems()
	{
		return mUnusedItems;
	}
	
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

		super(name, "player", 24);
		
		mInUseItems = new Vector();
		mUnusedItems = new Vector();

		mGold = 10;
		
		// set the player up with an indestructable but uninteresting sword
		Sword aSword = new Sword("Sword", 1, 100);
		receiveItem(aSword);
		startUsing(aSword);
		
		// and give him some arrows
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
	public Player(String name, int gold, Vector inUseItems, Vector unusedItems, int arrows, int points, Mission mission)
	{
		super(name, "player", points);
		
		mGold = gold;
		mInUseItems = inUseItems;
		mUnusedItems = unusedItems;
		mArrows = arrows;
		mMission = mission;
	}
	
	/**
	 * Returns a clone of this player. This also requires cloning the player's Swords and other items.
	 */
	public Object clone()
	{
		return new Player(getName(), mGold, (Vector) mInUseItems.clone(), (Vector) mUnusedItems.clone(), mArrows, getPoints(), mMission);
	}
	
	public void setMission(Mission aMission)
	{
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
		
		if (isDead())
		{
			MissionCard.endMissionAlert("Sorry, " + getName() + ", your game is over.");
			JCavernApplet.setPlayer(this);
		}

		setChanged();
		notifyObservers();
	}
	
	public void gainPoints(World aWorld, Combatant theVictim)
	{
		super.gainPoints(aWorld, theVictim);
		
		if (mMission != null)
		{
			mMission.adjustQuota(aWorld, this, theVictim);
		}
		
		setChanged();
		notifyObservers();
	}
	
	protected boolean hasProperName()
	{
		return true;
	}

	public int getWorth()
	{
		return 0;
	}
	
	public boolean canAttack(Combatant aCombatant)
	{
		return (getSword() != null) && aCombatant.vulnerableToPlayerAttack(this);
	}
	
	public boolean canRangedAttack(Combatant aCombatant)
	{
		//System.out.println(getName() + ".canRangedAttack(" + aCombatant + ")");
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
	
	public void decrementAttackCount(World aWorld)
	{
		Sword theSword = getSword();
		
		if ((theSword != null) && (Math.random() < Sword.kWearFraction))
		{
			theSword.decrementCharges();
			
			if (theSword.isDepleted())
			{
				drop(theSword);
				aWorld.eventHappened(new WorldEvent(this, WorldEvent.USE_INVENTORY, getNounPhrase() + "'s sword shatters"));
			}
			else
			{
				aWorld.eventHappened(new WorldEvent(this, WorldEvent.INFO_MESSAGE, getNounPhrase() + "'s sword chips"));
			}
		}
	
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
	
	/**
	 * Increments this player's gold account by the given amount.
	 */
	public void receiveGold(int delta) throws JCavernInternalError
	{
		if (delta < 0)
		{
			throw new JCavernInternalError("can't receive negative gold");
		}
		
		mGold += delta;

		setChanged();
		notifyObservers();
	}
	
	/**
	 * Decrements this player's gold account by the given amount.
	 */
	public void spendGold(int delta) throws JCavernInternalError
	{
		if (delta < 0)
		{
			throw new JCavernInternalError("can't spend negative gold");
		}
		
		mGold -= delta;

		setChanged();
		notifyObservers();
	}
	
	/**
	 * Increments this player's number of arrows.
	 */
	public void receiveArrows(int delta)
	{
		mArrows += delta;

		setChanged();
		notifyObservers();
	}
	
	public Sword getSword()
	{
		for (int index = 0; index < mInUseItems.size(); index++)
		{
			Treasure aTreasure = (Treasure) mInUseItems.elementAt(index);
			
			if (aTreasure instanceof Sword)
			{
				return (Sword) aTreasure;
			}
		}
		
		return null;
	}
	
	private int getArmourPoints()
	{
		int armourPoints = 0;
		
		for (int index = 0; index < mInUseItems.size(); index++)
		{
			Treasure aTreasure = (Treasure) mInUseItems.elementAt(index);
			
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
		//System.out.println(getName() + " received " + anItem);
		
		mUnusedItems.addElement(anItem);
		
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Returns this player's current Mission.
	 */
	public Mission getMission()
	{
		return mMission;
	}
	
	/**
	 * Returns the castle currently occupied by the player, if any.
	 *
	 * @return	a Castle, or <CODE>null</CODE> if none.
	 */
	public Castle getCastle()
	{
		return mCastle;
	}
	
	/**
	 * Sets the castle currently occupied by the player, if any.
	 *
	 * @return	aCastle		a Castle to occupy, or <CODE>null</CODE> if none.
	 */
	public void setCastle(Castle aCastle)
	{
		mCastle = aCastle;
	}
	
	/**
	 * Paints the player, and any castles the Player is currently sitting in.
	 */
	public void paint(Graphics g, int plotX, int plotY, boolean highlight) throws JCavernInternalError
	{
		//System.out.println("Player.paint(g, " + plotX + ", " + plotY + ")");
		
		super.paint(g, plotX, plotY, highlight);

		if (getCastle() != null)
		{
			getCastle().paint(g, plotX, plotY, true);
		}
	}
	
	/**
	 * Handles a single turn by incrementing the move counter and notifying observers
	 *
	 * @param		aWorld					the non-null World in which the action is taking place.
	 * @exception	JCavernInternalError	the Thing could not process its turn
	 */
	public void doTurn(World aWorld) throws JCavernInternalError
	{
		super.doTurn(aWorld);
		
		setChanged();
		notifyObservers();
	}

	public void thingRemoved(World aWorld, Location aLocation) throws JCavernInternalError
	{
		if (getCastle() != null)
		{
			try
			{
				aWorld.place(aLocation, getCastle());
				setCastle(null);
			}
			catch (ThingCollisionException tce)
			{
				throw new JCavernInternalError("Can't put castle back in place of player");
			}
		}
	}
}
