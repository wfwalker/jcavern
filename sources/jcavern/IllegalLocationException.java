/* 
	IllegalLocationException.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

/**
 * Represents an attempt to create an illegal Location.
 *
 * @author	Bill Walker
 * @version	$Id$
 */
public class IllegalLocationException extends Exception
{
	/**
	 * Creates a new IllegalLocationException
	 *
	 * @param	msg		a non-null String describing the exception
	 */
	public IllegalLocationException(String msg)
	{
		super(msg);
	}
}