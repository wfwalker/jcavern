package jcavern.ui;

import jcavern.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;

/**
 * Creates an AppletCard that informs the user about the end of a Mission.
 */
public class EndMissionCard extends AppletCard implements ActionListener
{
	/** * The message to be displayed to the user. */
	Label	mMessage;
	
	/** * The button pressed by the user after reading the message. */
    Button	mOKButton;

    /**
     * Creates a new EndMissionCard for the given Applet and message.
     *
     * @param	anApplet	a non-null Applet in which to display the message
     * @param	aMessage	a non-null String containing a message to display
     */
    public EndMissionCard(JCavernApplet anApplet, String aMessage)
    {
    	super(anApplet);
    	
    	mMessage = new Label(aMessage);
    	
		mOKButton = new Button("OK");
		mOKButton.setForeground(Color.black);
		mOKButton.addActionListener(this);
    }

	/**
	 * Displays this AppletCard in its Applet.
	 */
    public void show()
    {
    	try
    	{
	    	super.show();
	    	
	    	mApplet.setLayout(new GridLayout(8, 1));
	    	
			// add spacer panels, so the dialog shows up where
			// the new mission button was.		
			mApplet.add(createLabelledButtonPanel(new ImageCanvas(JCavernApplet.getBoardImage("tree")), "End of mission"));
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
		catch (JCavernInternalError jcie)
		{
			System.out.println("can't show end of mission card " + jcie);
		}
    }

	/**
	 * Responds to ActionEvents from the OK Button by returning to the IndexCard.
	 *
	 * @param	event	a non-null ActionEvent.
	 */
    public void actionPerformed(ActionEvent event)
    {
		Object source = event.getSource();
		mApplet.displayHomeCard();
    }
}
