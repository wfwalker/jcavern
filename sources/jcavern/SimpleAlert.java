package jcavern;

import java.awt.*;
import java.awt.event.*;

class SimpleAlert extends Dialog implements ActionListener
{
    JCavernWindow	mParent;
    Button			mOKButton;

    SimpleAlert(Frame dw, String title)
    {
        super(dw, "JCavern Alert", false);

		setBackground(Color.black);
		setForeground(JCavernApplet.CavernOrange);

        mParent = (JCavernWindow) dw;

		//Create middle section.
		Panel	p1 = new Panel();
		Label	label = new Label(title);
		
		p1.add(label);
		add("Center", p1);

		//Create bottom row.
		Panel	p2 = new Panel();

		p2.setLayout(new FlowLayout(FlowLayout.RIGHT));
		mOKButton = new Button("OK");
		mOKButton.addActionListener(this);
		p2.add(mOKButton);
		add("South", p2);

		//Initialize this dialog to its preferred size.
		pack();
		
		// set the location to be on top of the parent
		setLocation(40, 40);
    }

    public void actionPerformed(ActionEvent event)
    {
		Object source = event.getSource();
		setVisible(false);
    }
}
