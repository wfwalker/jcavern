/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

import java.awt.Graphics;

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

