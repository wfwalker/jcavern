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

public class MissionView extends Canvas implements Observer
{
	private Mission	mModel;
	
	public MissionView(Mission aMission)
	{
		mModel = aMission;
		
		setBackground(Color.black);
		setForeground(Color.orange);
		aMission.addObserver(this);
	}
	
	public void setModel(Mission aModel)
	{
		mModel.deleteObserver(this);
		mModel = aModel;
		mModel.addObserver(this);
		repaint();
	}
	
	public void update(Observable a, Object b)
	{
		repaint();
	}
	
	public void paint(Graphics g)
	{
		//g.setFont(new Font("Monospaced", Font.PLAIN, 12));
			
		g.drawString("Your mission is " + mModel.toString(), 10, 20);
	}
}