/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

import java.util.*;
import java.awt.Rectangle;

/**
 * a World is a rectangular grid of Locations, some of which contain Things.
 *
 * @author	Bill Walker
 */
public class World extends Observable
{
	/** * The usual fraction of trees in the world. */
	private static final double		kTreeFraction = 0.1;
	
	/** * The bounds of the world */
	private static final Rectangle	kBounds = new Rectangle(21, 21);
	
	/** * A dictionary mapping from locations to things */
	private Hashtable			mLocationsToThings;
	
	/** * A dictionary mapping from things to their locations */
	private Hashtable			mThingsToLocations;
	
	/**
	 * Creates a new, empty world.
	 */
	public World()
	{
		mLocationsToThings = new Hashtable();
		mThingsToLocations = new Hashtable();
	}
	
	/**
	 * Places random trees using the default fraction.
	 */
	public void placeRandomTrees() throws ThingCollisionException
	{
		placeRandomTrees(kTreeFraction);
	}
	
	/**
	 * Places random trees according to the fraction passed in.
	 */
	public void placeRandomTrees(double coverageFraction) throws ThingCollisionException
	{
		System.out.println("Place Random Trees");
		
		for (int xIndex = 0; xIndex < kBounds.width; xIndex++)
		{
			for (int yIndex = 0; yIndex < kBounds.height; yIndex++)
			{
				if (Math.random() < coverageFraction)
				{
					place(new Location(xIndex, yIndex), new Tree());
				}
			}
		}
	}
	
	/**
	 * Places appropriate opponents on the board, based on the prowess of the given player.
	 */
	public void placeWorthyOpponents(Player aPlayer, int numberOfMonsters) throws ThingCollisionException
	{
		System.out.println("START placeWorthyOpponents");
		
		for (int index = 0; index < numberOfMonsters; index++)
		{
			place(getRandomEmptyLocation(), MonsterFactory.getWorthyOpponent(aPlayer));
		}

		System.out.println("STOP placeWorthyOpponents");
	}
	
	/**
	 * Returns a random location within the bounds of this world.
	 */
	public Location getRandomLocation()
	{
		int		x = (int) (Math.random() * kBounds.width);
		int		y = (int) (Math.random() * kBounds.height);
		
		return new Location(x, y);
	}
	
	/**
	 * Returns a random, empty location within the bounds of this world.
	 */
	public Location getRandomEmptyLocation()
	{
		Location emptyLocation = getRandomLocation();
		
		while (! isEmpty(emptyLocation))
		{
			emptyLocation = getRandomLocation();
		}
		
		return emptyLocation;
	}

	/**
	 * Returns the bounds of this world.
	 */
	public Rectangle getBounds()
	{
		return kBounds;
	}
	
	/**
	 * Processes an attack by the given combatant in the given direction.
	 */
	public void attack(Thing attacker, int aDirection) throws NoSuchThingException
	{
		//System.out.println("attack(" + attacker + ", " + Location.directionToString(aDirection) + ")");
		
		if (! mThingsToLocations.containsKey(attacker))
		{
			throw new NoSuchThingException("There's no " + attacker + " to attack");
		}
		
		Location	attackerLocation = (Location) mThingsToLocations.get(attacker);
		Location	attackeeLocation = attackerLocation.getNeighbor(aDirection);
		Thing		attackee = getThing(attackeeLocation);
		
		if ((attacker instanceof Player) && (attackee instanceof Tree))
		{
			remove(attackee);
			JCavernApplet.log(attacker.getName() + " chopped down the tree");
		}
		else if ((attacker instanceof Combatant) && (attackee instanceof Combatant))
		{
			Combatant 	theAttacker = (Combatant) attacker;
			Combatant 	theAttackee = (Combatant) attackee;
			int			damage = theAttacker.computeDamage();
			
			theAttackee.sufferDamage(damage);
			
			if (theAttackee.isDead())
			{
				JCavernApplet.log(attacker.getName() + " killed the " + attackee.getName());
				theAttacker.gainExperience(theAttackee);
				remove(attackee);
			}
			else
			{
				JCavernApplet.log(attacker.getName() + " hit the " + attackee.getName() + " for " + damage);
			}
		}
	}
	
	/**
	 * Processes an attack by the given combatant in the given direction.
	 */
	public void rangedAttack(Thing attacker, int aDirection) throws NoSuchThingException, IllegalLocationException
	{
		//System.out.println("attack(" + attacker + ", " + Location.directionToString(aDirection) + ")");
		
		if (! mThingsToLocations.containsKey(attacker))
		{
			throw new NoSuchThingException("There's no " + attacker + " to attack");
		}
		
		Combatant 	theAttacker = (Combatant) attacker;
		
		theAttacker.decrementRangedAttackCount();
		
		Location	attackerLocation = (Location) mThingsToLocations.get(attacker);
		Location	attackeeLocation = attackerLocation.getNeighbor(aDirection);
		
		while (isEmpty(attackeeLocation))
		{
			if (! attackeeLocation.inBounds(kBounds))
			{
				throw new IllegalLocationException("Ranged attack hit nothing");
			}
			
			JCavernApplet.log(attackeeLocation.toString());
			attackeeLocation = attackeeLocation.getNeighbor(aDirection);
		}
		
		Thing		attackee = getThing(attackeeLocation);
		
		if ((attacker instanceof Player) && (attackee instanceof Tree))
		{
			JCavernApplet.log(attacker.getName() + " shot a tree");
		}
		else if ((attacker instanceof Combatant) && (attackee instanceof Combatant))
		{
			Combatant 	theAttackee = (Combatant) attackee;
			int			damage = theAttacker.computeRangedDamage();
			
			theAttackee.sufferDamage(damage);
			
			if (theAttackee.isDead())
			{
				JCavernApplet.log(attacker.getName() + " killed the " + attackee.getName());
				theAttacker.gainExperience(theAttackee);
				remove(attackee);
			}
			else
			{
				JCavernApplet.log(attacker.getName() + "'s arrow hit the " + attackee.getName() + " for " + damage);
			}
		}
	}
	
	/**
	 * Moves the given thing in the given direction.
	 */
	public void move(Thing aThing, int direction) throws ThingCollisionException, NoSuchThingException, IllegalLocationException
	{
		if (! mThingsToLocations.containsKey(aThing))
		{
			throw new NoSuchThingException("There's no " + aThing + " in this world to move");
		}
		
		Location	oldLocation = (Location) mThingsToLocations.get(aThing);
		Location	newLocation = oldLocation.getNeighbor(direction);

		if (! newLocation.inBounds(kBounds))
		{
			throw new IllegalLocationException(aThing + " moved outside the world");
		}
		
		if (isEmpty(newLocation))
		{
			remove(oldLocation);
			place(newLocation, aThing);
		}
		else
		{
			Thing currentOccupant = getThing(newLocation);
			throw new ThingCollisionException(aThing, currentOccupant, aThing + " moved into " + currentOccupant);
		}
	}
		
	/**
	 * Finds the direction between two things.
	 */
	public int directionToward(Thing aThing, Thing anotherThing) throws NoSuchThingException
	{
		if (! mThingsToLocations.containsKey(aThing))
		{
			throw new NoSuchThingException("There's no " + aThing + " in this world to move");
		}
		
		if (! mThingsToLocations.containsKey(anotherThing))
		{
			throw new NoSuchThingException("There's no " + anotherThing + " in this world to move toward");
		}
		
		Location	oldLocation1 = (Location) mThingsToLocations.get(aThing);
		Location	oldLocation2 = (Location) mThingsToLocations.get(anotherThing);
		
		
		return oldLocation1.getDirectionToward(oldLocation2);
	}
	
	/**
	 * Retrieves the thing at the given location.
	 */
	public Thing getThing(Location aLocation) throws NoSuchThingException
	{
		if (mLocationsToThings.containsKey(aLocation))
		{
			return (Thing) mLocationsToThings.get(aLocation);
		}
		else
		{
			throw new NoSuchThingException("There's nothing at " + aLocation);
		}
	}
	
	public Player getPlayer() throws NoSuchThingException
	{
		Enumeration theThings = mThingsToLocations.keys();
		
		while (theThings.hasMoreElements())
		{
			Thing aThing = (Thing) theThings.nextElement();
			
			if (aThing instanceof Player)
			{
				return (Player) aThing;
			}
		}
		
		throw new NoSuchThingException("No players found in this world");
	}
	
	public void doTurn()
	{
		Enumeration theThings = mThingsToLocations.keys();
		
		try
		{
			while (theThings.hasMoreElements())
			{
				((Thing) theThings.nextElement()).doTurn(this);
			}
		}
		catch (NoSuchThingException nse)
		{
			System.out.println("World.doTurn() internal consistency problem");
		}
	}
	
	/**
	 * Answers whether there is any thing at the given location.
	 */
	protected boolean isEmpty(Location aLocation)
	{
		if (mLocationsToThings.containsKey(aLocation))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	/**
	 * Places a Thing at the given location.
	 */
	public void place(Location aLocation, Thing aThing) throws ThingCollisionException
	{
		if (mLocationsToThings.containsKey(aLocation))
		{
			throw new ThingCollisionException(aThing, "There's already " + aThing + " at " + aLocation);
		}
		
		mLocationsToThings.put(aLocation, aThing);
		mThingsToLocations.put(aThing, aLocation);
		
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Retrieves the location of the given thing.
	 */
	public Location getLocation(Thing aThing) throws NoSuchThingException
	{
		if (! mThingsToLocations.containsKey(aThing))
		{
			throw new NoSuchThingException("There's no " + aThing + " in this world to locate");
		}
		
		return (Location) mThingsToLocations.get(aThing);
	}
	
	/**
	 * Remove the thing at the given location from the world.
	 */
	public void remove(Location locationToRemove) throws NoSuchThingException
	{
		Thing	thingToRemove = (Thing) mLocationsToThings.get(locationToRemove);
		
		if (thingToRemove == null)
		{
			throw new NoSuchThingException("There's no " + thingToRemove + " in this world to remove");
		}
		
		mLocationsToThings.remove(locationToRemove);
		mThingsToLocations.remove(thingToRemove);

		setChanged();
		notifyObservers();
	}
	
	/**
	 * Remove the given thing from the world.
	 */
	public void remove(Thing thingToRemove) throws NoSuchThingException
	{
		Location	locationToRemove = (Location) mThingsToLocations.get(thingToRemove);
		
		if (locationToRemove == null)
		{
			throw new NoSuchThingException("There's no " + thingToRemove + " in this world to remove");
		}
		
		mLocationsToThings.remove(locationToRemove);
		mThingsToLocations.remove(thingToRemove);

		setChanged();
		notifyObservers();
	}
}
