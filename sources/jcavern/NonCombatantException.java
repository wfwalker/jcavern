/* 
	NonCombatantException.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

/**
 * Represents a situation in which a Combatant attempted to attack a non-combatant.
 *
 * @author	Bill Walker
 * @version	$Id$
 */
public class NonCombatantException extends Exception
{
	/**
	 * Creates a new NonCombatantException
	 *
	 * @param	msg		a non-null String describing the exception
	 */
	public NonCombatantException(String msg)
	{
		super(msg);
	}
}