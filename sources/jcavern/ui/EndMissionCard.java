package jcavern.ui;

import jcavern.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class EndMissionCard extends AppletCard implements ActionListener
{
	Label	mMessage;
    Button	mOKButton;
    
    public EndMissionCard(JCavernApplet anApplet, String aMessage)
    {
    	super(anApplet);
    	
    	mMessage = new Label(aMessage);
    	
		mOKButton = new Button("OK");
		mOKButton.setForeground(Color.black);
		mOKButton.addActionListener(this);
    }

    public void show()
    {
    	mApplet.removeAll();
    	
    	mApplet.setLayout(new GridLayout(8, 1));
    	
		//mApplet.setForeground(Color.black);
		//mApplet.setBackground(Color.white);

		mApplet.add(new Label("End of Mission"));

		// add spacer panels, so the dialog shows up where
		// the new mission button was.		
		
		mApplet.add(createLabelledButtonPanel(new Button("Load Player"), "Load player from database", false));
		mApplet.add(createLabelledButtonPanel(new Button("Save Player"), "Save player to database", false));
		
		//Create middle section.
		Panel alertPanel = new Panel();
		alertPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		alertPanel.add(mMessage);
		alertPanel.add(mOKButton);
		
		mApplet.add(alertPanel);
		
		mApplet.add(createLabelledButtonPanel(new Button("New Player"), "Create a new player", false));

		//Initialize this dialog to its preferred size.
		mApplet.validate();
    }

    public void actionPerformed(ActionEvent event)
    {
		Object source = event.getSource();
		mApplet.displayHomeCard();
    }
}
