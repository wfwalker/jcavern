/* 
	JCavernView.java

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
 * JCavernView knows where the applet is.
 *
 * @author	Bill Walker
 * @version	$Id$
 */
public abstract class JCavernView extends Canvas implements Observer
{
	/** * The Applet to use for loading images. */
	private JCavernApplet	mApplet;
	
	/**
	 * Reacts to updates by repainting.
	 * Must be implemented by all Views.
	 *
	 * @param	a	the object that sent the update
	 * @param	b	information about the update.
	 */
	public abstract void update(Observable a, Object b);
	
	/**
	 * Creates a new Cavern View using the given applet.
	 *
	 * @param	inApplet	the non-null Applet used for loading images
	 */
	public JCavernView(JCavernApplet inApplet)
	{
		mApplet = inApplet;
	}
	
	/**
	 * Returns the current applet for this view.
	 *
	 * @return		a non-null Applet used for loading images.
	 */
	public JCavernApplet getApplet()
	{
		return mApplet;
	}
}
