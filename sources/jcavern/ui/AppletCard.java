package jcavern.ui;

import java.applet.*;
import java.awt.*;
import java.awt.Color;
import jcavern.*;

public class AppletCard
{
	protected JCavernApplet	mApplet;
	
	public AppletCard(JCavernApplet anApplet)
	{
		mApplet = anApplet;
	}
	
	protected Panel createLabelledButtonPanel(Button aButton, String aLabel)
	{
		return createLabelledButtonPanel(aButton, aLabel, aButton.isEnabled());
	}

	protected Panel createLabelledButtonPanel(Button aButton, String aLabel, boolean isEnabled)
	{
		aButton.setEnabled(isEnabled);
		aButton.setForeground(Color.black);

		Panel aPanel = new Panel();

		aPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		aPanel.add(aButton, BorderLayout.WEST);
		aPanel.add(new Label(aLabel), BorderLayout.CENTER);
		
		return aPanel;
	}

	public void show()
	{
		mApplet.removeAll();

		mApplet.setBackground(Color.black);
		mApplet.setForeground(JCavernApplet.CavernOrange);
	}
}