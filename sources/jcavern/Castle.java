/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

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
		super("Castle");
	}
	
	public Object clone()
	{
		return new Castle();
	}
	
	public void paint(Graphics g, int plotX, int plotY)
	{
		g.drawOval(plotX - 20, plotY - 20, 40, 40);
	}
}

