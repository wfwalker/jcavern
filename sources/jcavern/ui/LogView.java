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
 * LogView displays the log messages for MissionCards.
 *
 * @author	Bill Walker
 * @version	$Id$
 */
public class LogView extends Canvas implements Observer
{
	/** * A list of log messages, used as a fixed length, FIFO queue */
	private Vector	mLogLines;
	
	/** * The number of log messages displayed. */
	private int		mNumberOfLines;
	
	/** * The subject of this log view. */
	private Thing		mSubject;
	
	/**
	 * Creates a new LogView.
	 */
	public LogView(Thing aSubject)
	{
		mLogLines = new Vector();
		mNumberOfLines = 5;
		mSubject = aSubject;
		
		setBackground(Color.black);
		setForeground(JCavernApplet.CavernOrange);
	}

	/**
	 * Responds to updates by displaying log messages.
	 */
	public void update(Observable a, Object b)
	{
		//System.out.println("LogView.update(" + a + ", " + b + ")");
		
		WorldEvent anEvent = (WorldEvent) b;
		
		if (mSubject == anEvent.getSubject())
		{
			switch (anEvent.getEventCode())
			{
				case WorldEvent.ATTACKED_MISSED:
				case WorldEvent.ATTACKED_HIT:
				case WorldEvent.ATTACKED_KILLED:
				case WorldEvent.INFO_MESSAGE:
				case WorldEvent.ERROR_MESSAGE:
					addLine(anEvent.toString());
					break;
				default:
					//System.out.println("rejected " + anEvent);
			}
		}
		else if (mSubject == anEvent.getCause())
		{
			addLine(anEvent.toString());
		}
		else
		{
			//System.out.println("rejected " + anEvent);
		}
	}
	
	/**
	 * Adds a log mesage to this LogView.
	 */
	public void addLine(String aString)
	{
		mLogLines.addElement(aString);
		
		if (mLogLines.size() > mNumberOfLines)
		{
			mLogLines.removeElementAt(0);
		}
		
		repaint();		
	}
	
	/**
	 * Paints this LogView by drawing the current queue of log messages.
	 *
	 * @param	g	a non-null Graphics object for painting.
	 */
	public void paint(Graphics g)
	{
		setBackground(Color.black);
		setForeground(JCavernApplet.CavernOrange);

		int y = g.getFontMetrics().getHeight();
		int lineHeight = g.getFontMetrics().getHeight();
		
		mNumberOfLines = (int) Math.floor((double) getSize().height / lineHeight) - 1;
		
		for (int index = 0; index < mLogLines.size(); index++)
		{
			g.drawString((String) mLogLines.elementAt(index), 1, y); y += lineHeight;
		}		
	}
}