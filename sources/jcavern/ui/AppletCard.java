package jcavern.ui;

import java.applet.*;
import java.awt.*;
import java.awt.Color;
import jcavern.*;

/**
 * AppletCard defines a set of classes that display user interfaces in an applet.
 * The name "card" is intended to suggest a stack of full screen user interface
 * displays, like Apple's HyperCard.
 */
public class AppletCard
{
	/** * The applet in which this card displays. */
	protected JCavernApplet		mApplet;
	
	/**
	 * Create a new AppletCard for the given Applet.
	 *
	 * @param	anApplet	a non-null Applet
	 */
	public AppletCard(JCavernApplet anApplet)
	{
		mApplet = anApplet;
	}
	
	/**
	 * Create a Panel containing a button and a label.
	 * Used by IndexCard, EndMissionCard, and CreatePlayerCard.
	 *
	 * @param	aButton		a non-null Button
	 * @param	aLabel		a non-null String containing the text of a label
	 * @return				a non-null Panel containing a Button and a Label
	 */
	protected Panel createLabelledButtonPanel(Component aButton, String aLabel)
	{
		return createLabelledButtonPanel(aButton, aLabel, aButton.isEnabled());
	}

	/**
	 * Create a Panel containing an optionally enabled button and a label.
	 * Used by IndexCard, EndMissionCard, and CreatePlayerCard.
	 *
	 * @param	aButton		a non-null Button
	 * @param	aLabel		a non-null String containing the text of a label
	 * @param	isEnabled	<CODE>true</CODE> if the button should be enabled, <CODE>false</CODE> otherwise
	 * @return				a non-null Panel containing a Button and a Label
	 */
	protected Panel createLabelledButtonPanel(Component aButton, String aLabel, boolean isEnabled)
	{
		aButton.setEnabled(isEnabled);
		aButton.setForeground(Color.black);

		Panel aPanel = new Panel();

		aPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		aPanel.add(aButton, BorderLayout.WEST);
		aPanel.add(new Label(aLabel), BorderLayout.CENTER);
		
		return aPanel;
	}
	
	/**
	 * Notifies a Card that it was removed from view by the Applet.
	 */
	public void cardRemoved()
	{
	}

	/**
	 * Display this card in its Applet.
	 * This implementation merely removes all the components from the applet
	 * and sets the foreground and background colors. Subclasses are expected
	 * to override show().
	 */
	public void show()
	{
		mApplet.removeAll();
		
		mApplet.setAppletCard(this);

		mApplet.setBackground(Color.black);
		mApplet.setForeground(JCavernApplet.CavernOrange);
	}
}
