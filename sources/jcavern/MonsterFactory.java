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

public class MonsterFactory
{
	public static Hashtable		gMonstersByName;
	
	public static Monster[]		gMonsters;
	
	public static Monster getRandomMonster()
	{
		int randomIndex = (int) (Math.random() * gMonsters.length);
		
		return (Monster) gMonsters[randomIndex].clone();
	}
	
	public static Monster getWorthyOpponent(Player aPlayer)
	{
		int randomIndex = (int) (Math.random() * gMonsters.length);
		
		while (gMonsters[randomIndex].getPoints() > aPlayer.getExperience())
		{
			randomIndex = (int) (Math.random() * gMonsters.length);
		}
		
		return (Monster) gMonsters[randomIndex].clone();
	}
	
	public static void loadPrototypes(URL aURL )
	{
		System.out.println("loadPrototypes(" + aURL + ")");
		
		gMonstersByName = new Hashtable();
		
		
		try
		{
			InputStream		aStream = MonsterFactory.class.getResourceAsStream("/monster.dat");
			//InputStream		aStream = aURL.openStream();
			BufferedReader	aReader = new BufferedReader(new InputStreamReader(aStream));
			String			aMessageLine;

			while ((aMessageLine = aReader.readLine()) != null)
			{
				String				aName = aMessageLine.substring(0, 15).trim();
				
				String				theNumbers = aMessageLine.substring(15);
				StringTokenizer		aTokenizer = new StringTokenizer(theNumbers, " ");
				
				Double				points = Double.valueOf(aTokenizer.nextToken());
				Double				worth = Double.valueOf(aTokenizer.nextToken());
				Integer				invisible = Integer.valueOf(aTokenizer.nextToken());
				
				gMonstersByName.put(aName, new Monster(aName, aName.substring(0, 2), points.doubleValue(), worth.doubleValue(), invisible.intValue() < 0));
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
		
		// Copy the list of monsters into an array
		gMonsters = new Monster[gMonstersByName.size()];
		
		Enumeration		theMonsters = gMonstersByName.elements();
		int				monsterCount = 0;
		
		while (theMonsters.hasMoreElements())
		{
			Monster aMonster = (Monster) theMonsters.nextElement();
			gMonsters[monsterCount++] = aMonster;
		}
		
		System.out.println("*** DONE " + gMonsters);
	}
}