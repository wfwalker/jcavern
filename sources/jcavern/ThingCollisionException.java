/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

import jcavern.thing.*;

/**
 * Represents an attempt to put two things into the same location..
 *
 * @author	Bill Walker
 * @version	$Id$
 */
public class ThingCollisionException extends Exception
{
	/** * The thing moving into the Location */
	private Thing	mMover;

	/** * The thing already in the Location */
	private Thing	mMovee;
	
	/**
	 * Creates a new ThingCollisionException
	 *
	 * @param	mover	the non-null Thing moving into the location
	 * @param	movee	the non-null Thing already in the location
	 * @param	msg		a non-null String message about the collision
	 */
	public ThingCollisionException(Thing mover, Thing movee, String msg)
	{
		super(msg);

		mMover = mover;
		mMovee = movee;		
	}
	
	/**
	 * Creates a new ThingCollisionException
	 *
	 * @param	mover	the non-null Thing moving into the location
	 * @param	msg		a non-null String message about the collision
	 */
	public ThingCollisionException(Thing mover, String msg)
	{
		this(mover, null, msg);
	}
	
	/**
	 * Returns the Thing that moved into the collision Location.
	 *
	 * @return	a non-null Thing
	 */
	public Thing getMover()
	{
		return mMover;
	}
	
	/**
	 * Returns the Thing that was first at the collision Location.
	 *
	 * @return	a non-null Thing
	 */
	public Thing getMovee()
	{
		return mMovee;
	}
}
