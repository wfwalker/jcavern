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
	public static final int		PLACED = 300;
	
	/** * Used to indicate the kind of change that happened to a thing. */
	public static final int		REMOVED = 301;
	
	/** * Used to indicate the kind of change that happened to a thing. */
	public static final int		REVEALED = 302;
	
	/** * Used to indicate the kind of change that happened to a thing. */
	public static final int		MOVED = 303;
	
	/**
	 * Creates a WorldContentsEvent for Things placed in the World.
	 *
	 * @param	aLocation	a non-null Location
	 * @param	aThing		a non-null Thing
	 * @return				a non-null WorldContentsEvent
	 */
	public static WorldContentsEvent placed(Location aLocation, Thing aThing)
	{
		return new WorldContentsEvent(aLocation, aThing, PLACED, null, aThing.getName() + " was placed");
	}
	
	/**
	 * Creates a WorldContentsEvent for Things removed from the World.
	 *
	 * @param	aLocation	a non-null Location
	 * @param	aThing		a non-null Thing
	 * @return				a non-null WorldContentsEvent
	 */
	public static WorldContentsEvent removed(Location aLocation, Thing aThing)
	{
		return new WorldContentsEvent(aLocation, aThing, REMOVED, null, aThing.getName() + " was removed");
	}
	
	/**
	 * Creates a WorldContentsEvent for formerly invisible Things revealed in the World.
	 *
	 * @param	aThing		a non-null Thing that was revealed
	 * @param	aCause		a non-null Thing that did the revealing
	 * @return				a non-null WorldContentsEvent
	 */
	public static WorldContentsEvent revealed(Thing aThing, Thing aCause)
	{
		return new WorldContentsEvent(null, aThing, REVEALED, aCause, aThing.getName() + " was revealed");
	}
	
	/**
	 * Creates a WorldContentsEvent for things that move.
	 *
	 * @param	oldLocation	a non-null Location from which the Thing moved
	 * @param	aThing		a non-null Thing that moved
	 * @return				a non-null WorldContentsEvent
	 */
	public static WorldContentsEvent moved(Location oldLocation, Thing aThing)
	{
		return new WorldContentsEvent(oldLocation, aThing, MOVED, null, aThing.getName() + " moved");
	}
	
	/**
	 * Creates a new WorldContentsEvent.
	 *
	 * @param	aLocation	a non-null Location
	 * @param	subject		a non-null Thing, the subject of the event
	 * @param	code		an event code
	 * @param	cause		a non-null Thing, the cause of the event
	 * @param	message		a non-null String message describing the event
	 */
	public WorldContentsEvent(Location aLocation, Thing subject, int code, Thing cause, String message)
	{
		super(aLocation, subject, code, cause, message);
	}
}
