package jcavern;

import jcavern.thing.*;

import java.util.Observable;
import java.awt.*;

/**
 * Class used to describe Things appearing, disappearing, or being revealed in a World.
 *
 * @author	Bill Walker
 * @version	$Id$
 */
public class WorldContentsEvent extends WorldEvent
{
	/** * Used to indicate the kind of change that happened to a thing. */
	public static final int		PLACED = 1;
	
	/** * Used to indicate the kind of change that happened to a thing. */
	public static final int		REMOVED = 2;
	
	/** * Used to indicate the kind of change that happened to a thing. */
	public static final int		REVEALED = 6;
	
	/**
	 * Creates a WorldEvent for Things placed in the World.
	 */
	public static WorldContentsEvent placed(Location aLocation, Thing aThing)
	{
		return new WorldContentsEvent(aLocation, aThing, PLACED, null, aThing.getName() + " was placed");
	}
	
	/**
	 * Creates a WorldEvent for Things removed from the World.
	 */
	public static WorldContentsEvent removed(Location aLocation, Thing aThing)
	{
		return new WorldContentsEvent(aLocation, aThing, REMOVED, null, aThing.getName() + " was removed");
	}
	
	/**
	 * Creates a WorldEvent for formerly invisible Things revealed in the World.
	 */
	public static WorldContentsEvent revealed(Thing aThing, Thing aCause)
	{
		return new WorldContentsEvent(null, aThing, REVEALED, aCause, aThing.getName() + " was revealed");
	}
	
	/**
	 * Creates a new WorldContentsEvent.
	 */
	public WorldContentsEvent(Location aLocation, Thing subject, int code, Thing cause, String message)
	{
		super(aLocation, subject, code, cause, message);
	}
		
}