package jcavern.ui;

import jcavern.*;
import jcavern.thing.*;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;

/**
 * This AppletCard allows the user to specify the name of a new Player.
 */
public class CreatePlayerCard extends AppletCard implements ActionListener
{
	/** * A field into which the user types the Player's name. */
    TextField		mField;
    
    /** * The Button pressed by the user after typing in a Player name. */
    Button			mOKButton;

	/**
	 * Creates a CreatePlayerCard for the given Applet.
	 *
	 * @param	anApplet	a non-null Applet.
	 */
	public CreatePlayerCard(JCavernApplet anApplet)
	{
		super(anApplet);
		
		mField = new TextField(30);
		mField.addActionListener(this);
		
		mOKButton = new JCavernButton("OK");
		mOKButton.setForeground(Color.black);
		mOKButton.addActionListener(this);
	}
	
	/** 
	 * Shows this AppletCard in its Applet.
	 */
    public void show()
    {
    	super.show();

		try
		{	
	    	mApplet.setLayout(new GridLayout(8, 1));
	    	
			// add blank panels, so that the dialog will appear where
			// in the same place as the "create a player" button
			// in the IndexCard.
			mApplet.add(createLabelledButtonPanel(new ImageCanvas(mApplet.getBoardImage("player")), "Create a new player"));
			mApplet.add(createLabelledButtonPanel(new JCavernButton("Load Player"), "Load player from database", false));
			mApplet.add(createLabelledButtonPanel(new JCavernButton("Save Player"), "Save player to database", false));
			mApplet.add(createLabelledButtonPanel(new JCavernButton("New Mission"), "Undertake a new mission", false));
			
			//Create middle section.
			Panel dialogPanel = new Panel();
			dialogPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			dialogPanel.add(new Label("Enter the name of the new player"));
			dialogPanel.add(mField);
			dialogPanel.add(mOKButton);
			mApplet.add(dialogPanel);
	
			//Initialize this dialog to its preferred size.
			mApplet.validate();
		}
		catch (JCavernInternalError jcie)
		{
			System.out.println("can't show create player card " + jcie);
		}
    }

	/**
	 * Responds to ActionEvents from the OK Button by creating a Player and returning to the IndexCard.
	 *
	 * @param	event	a non-null ActionEvent
	 */
    public void actionPerformed(ActionEvent event)
    {
    	if (mField.getText().length() > 0)
    	{
    		try
    		{
				mApplet.displayHomeCard(new Player(mField.getText()));
			}
			catch(JCavernInternalError jcie)
			{
				System.out.println("Can't create new player " + jcie);
			}
		}
    }
}
