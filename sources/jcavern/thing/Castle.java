/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern.thing;

import java.awt.Graphics;

/**
 * Castle represents a stationary shelter that renders
 * the player invulnerable to all monster attacks.
 *
 * @author	Bill Walker
 * @version	$Id$
 */
public class Castle extends Thing
{
	/**
	 * Creates a new Castle.
	 */
	public Castle()
	{
		super("Castle", "castle");
	}
	
	/**
	 * Creates a clone of this Castle.
	 * Since all castles are the same, this is not very interesting.
	 *
	 * @return	a Castle.
	 */
	public Object clone()
	{
		return new Castle();
	}
}

