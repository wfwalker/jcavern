/* 
	EmptyLocationException.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

/**
 * Represents a situation in which the contents of a location were unexpectedly empty.
 *
 * @author	Bill Walker
 * @version	$Id$
 */
public class EmptyLocationException extends Exception
{
	/**
	 * Creates a new EmptyLocationException
	 *
	 * @param	msg		a non-null String describing the exception
	 */
	public EmptyLocationException(String msg)
	{
		super(msg);
	}
}