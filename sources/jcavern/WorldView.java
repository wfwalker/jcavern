/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class WorldView extends Canvas implements Observer
{
	private World	mModel;
	
	public WorldView(World aWorld)
	{
		System.out.println("Create WorldView");
		
		mModel = aWorld;
		
		setBackground(Color.white);
	}
	
	public void update(Observable a, Object b)
	{
		repaint();
	}
	
	public void paint(Graphics g)
	{
		try
		{
			Player		thePlayer = mModel.getPlayer();
			Location	theLocation = mModel.getLocation(thePlayer);
		
			for (int yIndex = -3; yIndex <= 3; yIndex++)
			{
				for (int xIndex = -3; xIndex <= 3; xIndex++)
				{
					Location	aLocation = new Location(xIndex + theLocation.getX(), yIndex + theLocation.getY());
					int			plotX = 20 * (xIndex + 3);
					int			plotY = 20 + 20 * (yIndex + 3);
					
					if (aLocation.inBounds(mModel.getBounds()))
					{
						if (! mModel.isEmpty(aLocation))
						{
							Thing theThing = mModel.getThing(aLocation);
					
							g.drawString(theThing.getAppearance(), plotX, plotY);
						}
						else
						{
							g.drawString(".", plotX, plotY);
						}
					}
					else
					{
						g.drawString("*", plotX, plotY);
					}
				}
			}
		}
		catch (JCavernInternalError jcie)
		{
			System.out.println("Can't paint world view, " + jcie);
		}
		catch (EmptyLocationException ele)
		{
			System.out.println("Can't paint world view, internal error " + ele);
		}
		catch (IllegalLocationException ile)
		{
			System.out.println("Illegal location exception, " + ile);
		}
	}
}