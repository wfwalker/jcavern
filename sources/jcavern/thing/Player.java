/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern.thing;

import jcavern.*;
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

//-----------------

	/**
	 * GraphicalPlayerView is a GraphicalThingView for displaying a Player.
	 * It differs from GraphicalCombatantView only in extending the paint
	 * method to include painting the Castle the player is standing in, if any.
	 */
	public class GraphicalPlayerView extends Combatant.GraphicalCombatantView
	{
		/**
		 * Creates a new GraphicalThingView
		 *
		 * @param	inImageName		which image to use
		 */
		public GraphicalPlayerView(String inImageName)
		{
			super(inImageName);
		}

		/**
		 * Paints the player, and any castles the Player is currently sitting in.
		 *
		 * @param		inApplet				the Applet (used for loading images)
		 * @param		g						the non-null Graphics object for painting
		 * @param		plotX					x coordinate of the Player relative to the View
		 * @param		plotY					y coordinate of the Player relative to the View
		 * @exception	JCavernInternalError	could not paint the player
		 */
		
		public void paint(JCavernApplet inApplet, Graphics g, int plotX, int plotY) throws JCavernInternalError
		{
			super.paint(inApplet, g, plotX, plotY);
		
			if (getCastle() != null)
			{
				getCastle().getGraphicalThingView().paint(inApplet, g, plotX, plotY);
			}
		}
		
	
	}
	
	/**
	 * Creates a GraphicalThingView appropriate to this Thing.
	 *
	 * @return	a non-nullGraphicalThingView appropriate to this Thing.
	 */
	public GraphicalThingView createGraphicalThingView(String inImageName)
	{
		return new GraphicalPlayerView(inImageName);
	}
//-----------------

	/**
	 * Moves an item from the unused list to the in use list.
	 *
	 * @param	anItem		a non-null Treasure to start using.
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
	 *
	 * @param	anItem		a non-null Treasure to drop
	 */
	public void drop(Treasure anItem)
	{
		mUnusedItems.removeElement(anItem);
		mInUseItems.removeElement(anItem);
		
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Retrieves an unused Treasure by index.
	 *
	 * @param	unusedIndex		<B>ONE-based</B> index of an unused Treasure
	 * @return					a non-null Treasure
	 */
	public Treasure getUnusedTreasureAt(int unusedIndex)
	{
		if ((unusedIndex < 1) || (unusedIndex > mUnusedItems.size()))
		{
			throw new IllegalArgumentException("No such item to start using");
		}
		
		return (Treasure) mUnusedItems.elementAt(unusedIndex - 1);
	}
	
	/**
	 * Stops using the given item.
	 *
	 * @param	anItem		a non-null Treasure to stop using
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
	 *
	 * @param	inUseIndex		<B>ONE-based</B> index of an in-use Treasure
	 * @return					a non-null Treasure
	 */
	public Treasure getInUseTreasureAt(int inUseIndex)
	{
		if ((inUseIndex < 1) || (inUseIndex > mInUseItems.size()))
		{
			throw new IllegalArgumentException("No such item to stop using");
		}
		
		return (Treasure) mInUseItems.elementAt(inUseIndex - 1);
	}

	/**
	 * Retrieves the Vector of in use items.
	 *
	 * @return	a Vector of Treasures currently in use
	 */
	public Vector getInUseItems()
	{
		return mInUseItems;
	}
	
	/**
	 * Retrieves sthe Vector of items not in use.
	 *
	 * @return	a Vector of Treasures not currently in use
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
		
		// set the player up with a compass
		MagicItem acompass = new MagicItem("Compass", 5);
		receiveItem(acompass);
		
		// and give him some arrows
		mArrows = 20;
	}
	
	/**
	 * Creates a new player with the given statistics.
	 *
	 * @param	name		a non-null String containing the name of the player.
	 * @param	gold		how many gold pieces the player has
	 * @param	inUseItems	the Vector of items currently in use
	 * @param	unusedItems	the Vector of items not currently in use
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
	 *
	 * @return		a non-null Player
	 */
	public Object clone()
	{
		return new Player(getName(), mGold, (Vector) mInUseItems.clone(),
				(Vector) mUnusedItems.clone(), mArrows, getPoints(), mMission);
	}
	
	/**
	 * Sets the current mission for this player.
	 *
	 * @param	aMission	a non-null Mission.
	 */
	public void setMission(Mission aMission)
	{
		mMission = aMission;
	}
	
	/**
	 * Causes the player to suffer combat damage.
	 * Damage points are mitigated by any armour the player is wearing.
	 *
	 * @param		inWorld					a non-null World in which the combat takes place.
	 * @param		inDamage				how much damage was done (not taking armour points into account)
	 * @return								how much damage was actually done, taking armour into account
	 * @exception	JCavernInternalError	a mission end event could not be created.
	 */
	public int sufferDamage(World inWorld, int inDamage) throws JCavernInternalError
	{
		int adjustedDamage = inDamage - getArmourPoints();
		
		if (adjustedDamage < 0)
		{
			adjustedDamage = 0;
		}
		
		super.sufferDamage(inWorld, adjustedDamage);
		
		if (isDead())
		{
			inWorld.eventHappened(WorldEvent.missionEnd(inWorld.getLocation(this), this, "Sorry, you died!"));
		}

		setChanged();
		notifyObservers();
		
		return adjustedDamage;
	}
	
	/**
	 * Causes the player to receive points as credit for vanquishing an opponent.
	 *
	 * @param	aWorld		a non-null World in which the combat occurred.
	 * @param	theVictim	the now-vanquished opponent.
	 */
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
	
	/**
	 * Returns true if this Combatant has a proper name.
	 *
	 * @return	<CODE>true</CODE>, all Players have proper names.
	 */
	protected boolean hasProperName()
	{
		return true;
	}
	
	/**
	 * Returns a noun phrase for this Thing
	 *
	 * @return	<CODE>"you"</CODE>
	 */
	public String getNounPhrase()
	{
		return "you";
	}

	/**
	 * Returns how many points this Combatant is worth when vanquished.
	 *
	 * @return	0, not used since Monsters don't stick around after players die.
	 */
	public int getWorth()
	{
		return 0;
	}

	/**
	 * Returns whether this Player can attack a given Combatant.
	 * Depends on whether this player has a sword, and whether the opponent is vulnerable to
	 * player attack.
	 *
	 * @param	aCombatant		a non-null Combatant.
	 * @return	<CODE>true</CODE> if this Player can attack the given combatant, <CODE>false</CODE> otherwise
	 */
	public boolean canAttack(Combatant aCombatant)
	{
		return (getSword() != null) && aCombatant.vulnerableToPlayerAttack(this);
	}
	
	/**
	 * Returns whether this Combatant can make a ranged attack on a given Combatant.
	 * Depends on whether this player has arrows, and whether the opponent is vulnerable to
	 * ranged player attack.
	 *
	 * @param	aCombatant		a non-null Combatant.
	 * @return	<CODE>true</CODE> if this Player can attack the given combatant, <CODE>false</CODE> otherwise
	 */
	public boolean canRangedAttack(Combatant aCombatant)
	{
		//System.out.println(getName() + ".canRangedAttack(" + aCombatant + ")");
		return (mArrows > 0) && aCombatant.vulnerableToPlayerRangedAttack(this);
	}
	
	/**
	 * Returns whether this combatant is vulnerable to attack from a given Monster.
	 *
	 * @param		aMonster		a non-null Monster
	 * @return						<CODE>false</CODE> if the Player is in a castle, CODE>true</CODE> otherwise
	 */
	public boolean vulnerableToMonsterAttack(Monster aMonster)
	{
		return getCastle() == null;
	}
	
	/**
	 * Computes the damage this combatant does to a given opponent in a sword attack.
	 * Depends on the current health of the player and the prowess of the sword currently in use.
	 *
	 * @param	opponent	a non-null Combatant on whom the receiver will inflict damage
	 * @return				damage points
	 */
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
	
	/**
	 * Decrements the number of ranged attacks this Combatant can perform.
	 * This simulates using up a supply of arrows.
	 */
	public void decrementRangedAttackCount()
	{
		mArrows--;
		setChanged();
		notifyObservers();	
	}
	
	/**
	 * Decrements the number of attacks this Combatant can perform.
	 * This simulates wearing out a sword.
	 *
	 * @param	aWorld	the world in which the attack took place.
	 */
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
	
	/**
	 * Computes the damage this combatant does to a given opponent in a ranged attack.
	 *
	 * @param	opponent	a non-null Combatant on whom the receiver will inflict damage
	 * @return				how much damage this player would do in a ranged attack
	 */
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

	/**
	 * Returns the Player's current bank balance
	 *
	 * @return	how much gold the player has.
	 */
	public int getGold()
	{
		return mGold;
	}
	
	/**
	 * Increments this player's gold account by the given amount.
	 *
	 * @param		delta					how much gold the Player received
	 * @exception	JCavernInternalError	delta was negative
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
	 *
	 * @param		delta					how much gold the Player spent
	 * @exception	JCavernInternalError	delta was negative
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
	 *
	 * @param	delta	how many arrows the Player received
	 */
	public void receiveArrows(int delta)
	{
		mArrows += delta;

		setChanged();
		notifyObservers();
	}
	
	/**
	 * Returns the player's current sword, or null if none.
	 *
	 * @return the player's current sword, or <CODE>null</CODE> if none.
	 */
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
	
	/**
	 * Returns the player's current armour points.
	 *
	 * @return the player's current armour points, or 0 if none.
	 */
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
	
	/**
	 * Returns how many arrows the player has.
	 *
	 * @return	how many arrows the player has.
	 */
	public int getArrows()
	{
		return mArrows;
	}
	
	/**
	 * Adds a Treasure to the Player's list of not-in-use items.
	 *
	 * @param	anItem		a non-null Treasure
	 */
	public void receiveItem(Treasure anItem)
	{
		//System.out.println(getName() + " received " + anItem);
		
		mUnusedItems.addElement(anItem);
		
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Returns this player's current Mission.
	 *
	 * @return		this player's current Mission.
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
	 * @param	aCastle		a Castle to occupy, or <CODE>null</CODE> if none.
	 */
	public void setCastle(Castle aCastle)
	{
		mCastle = aCastle;
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

	/**
	 * Informs the player that it was removed from the world.
	 * If the player is in a Castle, put the Castle back where the player was.
	 *
	 * @param		aWorld					the world in which the action takes place
	 * @param		aLocation				the Location from which the Player was removed
	 * @exception	JCavernInternalError	trouble putting the Castle back
	 */
	public void thingRemoved(World aWorld, Location aLocation) throws JCavernInternalError
	{
		if (getCastle() != null)
		{
			try
			{
				aWorld.place(aLocation, getCastle());
				setCastle(null);
				aWorld.eventHappened(new WorldEvent(this, WorldEvent.INFO_MESSAGE, getNounPhrase() + " departed the magic castle"));
			}
			catch (ThingCollisionException tce)
			{
				throw new JCavernInternalError("Can't put castle back in place of player");
			}
		}
	}
}
