package jcavern.ui;

import java.applet.*;
import java.awt.*;
import java.awt.Color;
import jcavern.*;

/**
 * A Canvas that just displays a freakin' Image. Man, AWT is lame.
 */
public class ImageCanvas extends Canvas
{
	Image	mImage;
	
	public ImageCanvas(Image anImage)
	{
		mImage = anImage;
		
		setSize(mImage.getWidth(null), mImage.getHeight(null));	
	}
	
	public void paint(Graphics g)
	{
		g.drawImage(mImage, 0, 0, null);
	}
}