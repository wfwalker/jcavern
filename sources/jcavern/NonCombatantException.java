/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

/**
 * Represents a situation in which an asked-for thing was not found.
 *
 * @author	Bill Walker
 * @version	$Id$
 */
public class NonCombatantException extends Exception
{
	public NonCombatantException(String msg)
	{
		super(msg);
	}
}