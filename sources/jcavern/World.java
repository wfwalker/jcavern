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
		for (int index = 0; index < numberOfMonsters; index++)
		{
			place(getRandomEmptyLocation(), MonsterFactory.getWorthyOpponent(aPlayer));
		}
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
	public String attack(Thing attacker, int aDirection) throws NoSuchThingException
	{
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
			return attacker.getName() + " chopped down the tree";
		}
		else
		{
			return attacker.getName() + " attacks " + attackee.getName();
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
		
		remove(oldLocation);
		
		try
		{
			place(newLocation, aThing);
		}
		catch(ThingCollisionException e)
		{
			place(oldLocation, aThing);
			throw e;
		}
	}
	
	/**
	 * Moves the thing at the given location in the given direction.
	 */
	public void move(Location oldLocation, int direction) throws ThingCollisionException, NoSuchThingException, IllegalLocationException
	{
		Thing		theThing = (Thing) mLocationsToThings.get(oldLocation);
		Location	newLocation = oldLocation.getNeighbor(direction);

		if (! newLocation.inBounds(kBounds))
		{
			throw new IllegalLocationException(theThing + " moved outside the world");
		}
		
		remove(oldLocation);
		
		try
		{
			place(newLocation, theThing);
		}
		catch(ThingCollisionException e)
		{
			place(oldLocation, theThing);
			throw e;
		}
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
			throw new ThingCollisionException("There's already " + aThing + " at " + aLocation);
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
