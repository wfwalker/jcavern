package jcavern.ui;

import java.applet.*;
import java.awt.*;
import java.awt.Color;
import jcavern.*;

/**
 * JCavernButton is a black and orange button designed to fit in with the rest of the
 * JCavern look and feel.
 */
public class JCavernButton extends Button
{
	/**
	 * Creates a new button with the given label.
	 *
	 * @param	inLabel		the non-null String button label.
	 */
	public JCavernButton(String inLabel)
	{
		super(inLabel);
		setBackground(Color.black);
		setForeground(JCavernApplet.CavernOrange);
	}
	
	/**
	 * Paints the button.
	 * If enabled, draws two nested rectangles and the label in bright orange.
	 * If disabled, draws one rectangle and the label in dim orange.
	 *
	 * @param	g	the non-null Graphics context in which to paint.
	 */
	public void paint(Graphics g)
	{
		Rectangle myRect = getBounds();
		int	labelHeight = g.getFontMetrics().getHeight();
		int labelWidth = g.getFontMetrics().stringWidth(getLabel());
		
		g.setColor(Color.black);
		g.fillRect(0, 0, myRect.width - 1, myRect.height - 1);
		
		
		if (isEnabled())
		{
			g.setColor(JCavernApplet.CavernOrange);
			g.drawRect(2, 2, myRect.width - 5, myRect.height - 5);		
		}
		else
		{
			g.setColor(JCavernApplet.CavernOrangeDim);
		}

		g.drawString(getLabel(),
						(myRect.width - labelWidth) / 2,
						-4 + (myRect.height + labelHeight) / 2);
							
		g.drawRect(0, 0, myRect.width - 1, myRect.height - 1);
	}
}
