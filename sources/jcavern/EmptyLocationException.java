/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

/**
 * Represents a situation in which the contents of a location were unexpectedly empty.
 */
public class EmptyLocationException extends Exception
{
	public EmptyLocationException(String msg)
	{
		super(msg);
	}
}