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
	private Thing	mMover;
	private Thing	mMovee;
	
	public ThingCollisionException(Thing mover, Thing movee, String msg)
	{
		super(msg);

		mMover = mover;
		mMovee = movee;		
	}
	
	public ThingCollisionException(Thing mover, String msg)
	{
		this(mover, null, msg);
	}
	
	public Thing getMover()
	{
		return mMover;
	}
	
	public Thing getMovee()
	{
		return mMovee;
	}
}