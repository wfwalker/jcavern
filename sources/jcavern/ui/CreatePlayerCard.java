package jcavern.ui;

import jcavern.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class CreatePlayerCard extends AppletCard implements ActionListener
{
    TextField		mField;
    Button			mOKButton;

	public CreatePlayerCard(JCavernApplet anApplet)
	{
		super(anApplet);
		
		mField = new TextField(30);
		mField.addActionListener(this);
		
		mOKButton = new Button("OK");
		mOKButton.setForeground(Color.black);
		mOKButton.addActionListener(this);
	}
	
    public void show()
    {
    	super.show();
    	
    	mApplet.setLayout(new GridLayout(8, 1));
    	
		//mApplet.setBackground(Color.white);
		//mApplet.setForeground(Color.black);

		mApplet.add(new Label("Create a New Player"));
		
		// add blank panels, so that the dialog will appear where
		// in the same place as the "create a player" button
		// in the IndexCard.
		mApplet.add(createLabelledButtonPanel(new Button("Load Player"), "Load player from database", false));
		mApplet.add(createLabelledButtonPanel(new Button("Save Player"), "Save player to database", false));
		mApplet.add(createLabelledButtonPanel(new Button("New Mission"), "Undertake a new mission", false));
		
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

    public void actionPerformed(ActionEvent event)
    {
    	if (mField.getText().length() > 0)
    	{
			mApplet.setPlayer(new Player(mField.getText()));
			mApplet.displayHomeCard();
		}
    }
    
    public String getText()
    {
    	return mField.getText();
    }
}
