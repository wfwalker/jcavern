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
		
		setBackground(Color.black);
		setForeground(Color.orange);
	}
	
	public void update(Observable a, Object b)
	{
		repaint();
	}
	
	public void paint(Graphics g)
	{
		//g.setFont(new Font("Monospaced", Font.PLAIN, 12));

		String statusString =
			"Gold: " + mModel.getGold() +
			" Weapon: " + mModel.getSword() +
			" Arrows: " + mModel.getArrows() +
			" Experience: " + mModel.getPoints() + 
			" Mission: " + mModel.getMission();
			
		g.drawString(statusString, 10, 20);
	}
}