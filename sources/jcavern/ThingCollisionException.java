/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

/**
 * Represents an attempt to put two things into the same location..
 */
public class ThingCollisionException extends Exception
{
	public ThingCollisionException(String msg)
	{
		super(msg);
	}
}