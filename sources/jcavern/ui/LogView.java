/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern.ui;

import jcavern.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * LogView displays the log messages.
 *
 * @author	Bill Walker
 * @version	$Id$
 */
public class LogView extends Canvas implements Observer
{
	private Vector	mLogLines;
	
	private int		mNumberOfLines;
	
	public LogView()
	{
		mLogLines = new Vector();
		mNumberOfLines = 5;
		
		setBackground(Color.black);
		setForeground(JCavernApplet.CavernOrange);
	}

	public void update(Observable a, Object b)
	{
		//repaint();
	}
	
	public void addLine(String aString)
	{
		mLogLines.addElement(aString);
		
		if (mLogLines.size() > mNumberOfLines)
		{
			mLogLines.removeElementAt(0);
		}
		
		repaint();		
	}
	
	public void paint(Graphics g)
	{
		setBackground(Color.black);
		setForeground(JCavernApplet.CavernOrange);

		int y = g.getFontMetrics().getHeight();
		int lineHeight = g.getFontMetrics().getHeight();
		
		for (int index = 0; index < mLogLines.size(); index++)
		{
			g.drawString((String) mLogLines.elementAt(index), 1, y); y += lineHeight;
		}		
	}
}