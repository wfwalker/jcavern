/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern.ui;

import jcavern.*;
import jcavern.thing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * LogView displays the log messages for MissionCards.
 *
 * @author	Bill Walker
 * @version	$Id$
 */
public class LogView extends JCavernView
{
	/** * A list of log messages, used as a fixed length, FIFO queue */
	private Vector	mLogLines;
	
	/** * The number of log messages displayed. */
	private int		mNumberOfLines;
	
	/** * The subject of this log view. */
	private Thing		mSubject;
	
	/**
	 * Creates a new LogView.
	 *
	 * @param	inApplet	the Applet hosting this view
	 * @param	inSubject	the Player starring in this view
	 */
	public LogView(JCavernApplet inApplet, Thing inSubject)
	{
		super(inApplet);
		
		mLogLines = new Vector();
		mNumberOfLines = 5;
		mSubject = inSubject;
		
		setBackground(Color.black);
		setForeground(JCavernApplet.CavernOrange);
	}

	/**
	 * Responds to updates by displaying log messages.
	 *
	 * @param	a	the World being observed
	 * @param	b	the WorldEvent that just happened
	 */
	public void update(Observable a, Object b)
	{
		//System.out.println("LogView.update(" + a + ", " + b + ")");
		
		WorldEvent anEvent = (WorldEvent) b;
		
		if (mSubject == anEvent.getSubject())
		{
			switch (anEvent.getEventCode())
			{
				case CombatEvent.ATTACKED_MISSED:
				case CombatEvent.ATTACKED_HIT:
				case CombatEvent.ATTACKED_KILLED:
				case CombatEvent.GAINED_POINTS:
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
	 *
	 * @param	aString		a String to add to the log
	 */
	private void addLine(String aString)
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

		int lineHeight = g.getFontMetrics().getHeight();
		int y = lineHeight;
		
		mNumberOfLines = (int) Math.floor((double) getSize().height / lineHeight) - 1;
				
		for (int index = mLogLines.size() - 1; index >= 0; index--)
		{
			g.drawString((String) mLogLines.elementAt(index), 1, y); y += lineHeight;
		}
		
		g.drawLine(0, lineHeight + 2, getWidth(), lineHeight + 2);
	}
}