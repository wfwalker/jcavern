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
	private static final double		kTreeFraction = 17.0 / 100.0;
	
	/** * The usual fraction of trees in the world. */
	private static final double		kCastleFraction = 0.4 / 100.0;
	
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

	public static World createWorld(Player aPlayer) throws JCavernInternalError
	{
		try
		{
			// Create a world  and a view of the world
			World aWorld = new World();
		
			// Place trees
			aWorld.placeRandomTrees();
			
			// Place castles
			aWorld.placeRandomCastles();
			
			// Place treasure chests
			aWorld.placeRandomTreasureChests(aPlayer);
			
			// Place monsters
			// orignally: Num_Monster := 3*Mis_quota + 2*Random(Mis_Quota);
			int quota = aPlayer.getMission().getQuota();			
			int desiredPopulation = (int) (3 * quota + 2 * Math.random() * quota);
			
			aWorld.placeRandom(aPlayer.getMission().getTarget(), quota);
			aWorld.placeWorthyOpponents(aPlayer, desiredPopulation - quota);
		
			// Put the player in the world
			aWorld.place(aWorld.getRandomEmptyLocation(), aPlayer);
			
			return aWorld;
		}
		catch (ThingCollisionException tce)
		{
			throw new JCavernInternalError("trouble creating a world " + tce);
		}
	}
	
	/**
	 * Places random trees using the default fraction.
	 */
	public void placeRandomTrees() throws ThingCollisionException
	{
		placeRandom(new Tree(), kTreeFraction);
	}
	
	/**
	 * Places random trees using the default fraction.
	 */
	public void placeRandomCastles() throws ThingCollisionException
	{
		placeRandom(new Castle(), kCastleFraction);
	}
	
	/**
	 * Places random trees using the default fraction.
	 */
	public void placeRandomTreasureChests(Player aPlayer) throws ThingCollisionException
	{
		placeRandom(new TreasureChest(), aPlayer.getMission().getQuota() / 2);
	}
	
	/**
	 * Places random trees according to the fraction passed in.
	 */
	public void placeRandom(Thing aThingPrototype, double fraction) throws ThingCollisionException
	{
		int	numberOfThings = (int) (getBounds().width * getBounds().height * fraction);
		
		System.out.println("Place fraction " + fraction + " Random " + aThingPrototype);
		
		placeRandom(aThingPrototype, numberOfThings);
	}
	
	/**
	 * Places random trees according to the fraction passed in.
	 */
	public void placeRandom(Thing aThingPrototype, int numberOfThings) throws ThingCollisionException
	{
		System.out.println("Place " + numberOfThings + " Random " + aThingPrototype);
		
		for (int index = 0; index < numberOfThings; index++)
		{
			place(getRandomEmptyLocation(), (Thing) aThingPrototype.clone());
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
	public Thing getThingToward(Thing attacker, int aDirection) throws JCavernInternalError, IllegalLocationException
	{
		//System.out.println("attack(" + attacker + ", " + Location.directionToString(aDirection) + ")");
		
		if (! mThingsToLocations.containsKey(attacker))
		{
			throw new JCavernInternalError("There's no " + attacker + " to attack");
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
		
		try
		{
			return getThing(attackeeLocation);
		}
		catch (EmptyLocationException ele)
		{
			throw new JCavernInternalError("World says location isn't empty, but throws EmptyLocationException");
		}
	}
	
	/**
	 * Moves the given thing in the given direction.
	 */
	public void move(Combatant aThing, int direction) throws ThingCollisionException, JCavernInternalError, IllegalLocationException
	{
		if (! mThingsToLocations.containsKey(aThing))
		{
			throw new JCavernInternalError("There's no " + aThing + " in this world to move");
		}
		
		Location	oldLocation = (Location) mThingsToLocations.get(aThing);
		Location	newLocation = oldLocation.getNeighbor(direction);

		if (! newLocation.inBounds(kBounds))
		{
			throw new IllegalLocationException(aThing + " moved outside the world");
		}
		
		if (isEmpty(newLocation)) // no collision on move
		{
			remove(oldLocation);
			place(newLocation, aThing);
		}
		else // collision on move, find out what's currently there
		{
			try
			{
				Thing	currentOccupant = getThing(newLocation);
		
				throw new ThingCollisionException(aThing, currentOccupant, aThing.getName() + " collided with " + currentOccupant.getName());
			}
			catch (EmptyLocationException ele)
			{
				throw new JCavernInternalError("World says location isn't empty, but throws EmptyLocationException!");
			}
		}	
	}
		
	/**
	 * Finds the direction between two things.
	 */
	public int directionToward(Thing aThing, Thing anotherThing) throws JCavernInternalError
	{
		if (! mThingsToLocations.containsKey(aThing))
		{
			throw new JCavernInternalError("There's no " + aThing + " in this world to move");
		}
		
		if (! mThingsToLocations.containsKey(anotherThing))
		{
			throw new JCavernInternalError("There's no " + anotherThing + " in this world to move toward");
		}
		
		Location	oldLocation1 = (Location) mThingsToLocations.get(aThing);
		Location	oldLocation2 = (Location) mThingsToLocations.get(anotherThing);

		return oldLocation1.getDirectionToward(oldLocation2);
	}
	
	/**
	 * Retrieves the thing at the given location.
	 */
	public Thing getThing(Location aLocation) throws EmptyLocationException, IllegalLocationException
	{
		if (mLocationsToThings.containsKey(aLocation))
		{
			return (Thing) mLocationsToThings.get(aLocation);
		}
		else if (! aLocation.inBounds(getBounds()))
		{
			throw new IllegalLocationException(aLocation + " is not in bounds");
		}
		else
		{
			throw new EmptyLocationException("There's nothing at " + aLocation);
		}
	}
	
	public Player getPlayer() throws JCavernInternalError
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
		
		throw new JCavernInternalError("No players found in this world");
	}
	
	public void doTurn() throws JCavernInternalError
	{
		Enumeration theThings = mThingsToLocations.keys();

		while (theThings.hasMoreElements())
		{
			((Thing) theThings.nextElement()).doTurn(this);
		}
	}
	
	/**
	 * Answers whether there is any thing at the given location.
	 */
	public boolean isEmpty(Location aLocation)
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
	public Location getLocation(Thing aThing) throws JCavernInternalError
	{
		if (! mThingsToLocations.containsKey(aThing))
		{
			throw new JCavernInternalError("There's no " + aThing + " in this world to locate");
		}
		
		return (Location) mThingsToLocations.get(aThing);
	}
	
	/**
	 * Remove the thing at the given location from the world.
	 */
	public void remove(Location locationToRemove) throws JCavernInternalError
	{
		Thing	thingToRemove = (Thing) mLocationsToThings.get(locationToRemove);
		
		if (thingToRemove == null)
		{
			throw new JCavernInternalError("There's no " + thingToRemove + " in this world to remove");
		}
		
		mLocationsToThings.remove(locationToRemove);
		mThingsToLocations.remove(thingToRemove);

		setChanged();
		notifyObservers();
	}
	
	/**
	 * Remove the given thing from the world.
	 */
	public void remove(Thing thingToRemove) throws JCavernInternalError
	{
		Location	locationToRemove = (Location) mThingsToLocations.get(thingToRemove);
		
		if (locationToRemove == null)
		{
			throw new JCavernInternalError("There's no " + thingToRemove + " in this world to remove");
		}
		
		mLocationsToThings.remove(locationToRemove);
		mThingsToLocations.remove(thingToRemove);

		setChanged();
		notifyObservers();
	}
}
