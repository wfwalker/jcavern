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
	/** * The image to display. */
	Image	mImage;
	
	/**
	 * Creates a new, blank canvas with no image.
	 *
	 * @param	inWidth		the width of the canvas
	 * @param	inHeight	the height of the canvas
	 */
	public ImageCanvas(int inWidth, int inHeight)
	{
		mImage = null;
		setSize(inWidth, inHeight);
	}
	
	/**
	 * Creates a new canvas to show the given image.
	 *
	 * @param	anImage		a non-null Image to display.
	 */
	public ImageCanvas(Image anImage)
	{
		mImage = anImage;
		
		setSize(mImage.getWidth(null), mImage.getHeight(null));	
	}
	
	/**
	 * Paints the image.
	 *
	 * @param	g	the non-null Graphics on which to paint.
	 */
	public void paint(Graphics g)
	{
		if (mImage != null)
		{
			g.drawImage(mImage, 0, 0, null);
		}
		else
		{
			// TODO: Erase the rect?
		}
	}
}