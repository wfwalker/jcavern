/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

import java.util.*;
import java.net.*;
import java.io.*;

/**
 * Treasures (including Swords, Armour, and MagicItems) are found in TreasureChests, and are
 * picked up and used by Players.
 *
 * @author	Bill Walker
 * @version	$Id$
 */
public abstract class Treasure implements Cloneable
{
	private static Vector	gTreasures;
	
	
	private String			mName;
	
	public Treasure(String aName)
	{
		mName = aName;	
	}
	
	public abstract Object clone();
	
	public String getName()
	{
		return mName;
	}
	
	public static Treasure getRandom()
	{
		int randomIndex = (int) (Math.random() * gTreasures.size());
		
		return (Treasure) ((Treasure) gTreasures.elementAt(randomIndex)).clone();
	}

	/**
	 * Load the prototypical treasure items from the treasures.dat file.
	 */
	public static void loadPrototypes(URL aURL )
	{
		System.out.println("loadPrototypes(" + aURL + ")");
		
		gTreasures = new Vector();
		
		try
		{
			//InputStream		aStream = MonsterFactory.class.getResourceAsStream("/treasure.dat");
			InputStream		aStream = aURL.openStream();
			BufferedReader	aReader = new BufferedReader(new InputStreamReader(aStream));
			String			aMessageLine;

			while ((aMessageLine = aReader.readLine()) != null)
			{
				String				aType = aMessageLine.substring(0,1).trim();
				String				aName = aMessageLine.substring(2,16).trim();
				Integer				aValue = Integer.valueOf(aMessageLine.substring(16).trim());
				
				if (aType.equals("s"))
				{
					gTreasures.addElement(new Sword(aName, aValue.intValue()));
				}
				else if (aType.equals("a"))
				{
					gTreasures.addElement(new Armour(aName, aValue.intValue()));
				}
				else
				{
					gTreasures.addElement(new MagicItem(aName, aValue.intValue()));
				}
			}
		}
		catch (MalformedURLException mue)
		{
			System.out.println("IO Error reading monsters " + mue);
		}
		catch (IOException ioe)
		{
			System.out.println("IO Error reading monsters " + ioe);
		}

		System.out.println("STOP " + gTreasures);
	}

}
