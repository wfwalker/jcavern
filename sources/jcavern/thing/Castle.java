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
	public Castle()
	{
		super("Castle", "castle");
	}
	
	public Object clone()
	{
		return new Castle();
	}
}

