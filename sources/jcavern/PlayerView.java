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

public class PlayerView extends Canvas implements Observer
{
	private Player	mModel;
	
	public PlayerView(Player aPlayer)
	{
		mModel = aPlayer;
		
		setBackground(Color.white);
	}
	
	public void update(Observable a, Object b)
	{
		repaint();
	}
	
	public void paint(Graphics g)
	{
		String statusString =
			"Gold " + mModel.getGold() +
			" Sword " + mModel.getSword() +
			" Arrows " + mModel.getArrows() +
			" Experience " + mModel.getExperience();
			
		g.drawString(statusString, 10, 20);
	}
}