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
			for (int yIndex = 0; yIndex < mModel.getBounds().height; yIndex++)
			{
				for (int xIndex = 0; xIndex < mModel.getBounds().width; xIndex++)
				{
					Location aLocation = new Location(xIndex, yIndex);
					
					if (! mModel.isEmpty(aLocation))
					{
						Thing theThing = mModel.getThing(aLocation);
						
						g.drawString(theThing.getAppearance(), 20 * xIndex, 20 + 20 * yIndex);
					}
					else
					{
						g.drawString(".", 20 * xIndex, 20 + 20 * yIndex);
					}
				}
			}
		}
		catch (NoSuchThingException nste)
		{
			System.out.println("Can't paint world view, " + nste);
		}
	}
}