/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

import java.util.*;
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
	
	public static void loadPrototypes(World aWorld)
	{
		gMonstersByName = new Hashtable();
		
		InputStream		aStream = MonsterFactory.class.getResourceAsStream("/monster.dat");
		BufferedReader	aReader = new BufferedReader(new InputStreamReader(aStream));
		String			aMessageLine;
		
		try
		{
			while ((aMessageLine = aReader.readLine()) != null)
			{
				String				aName = aMessageLine.substring(0, 15).trim();
				
				String				theNumbers = aMessageLine.substring(15);
				StringTokenizer		aTokenizer = new StringTokenizer(theNumbers, " ");
				
				double				points = Double.parseDouble(aTokenizer.nextToken());
				double				worth = Double.parseDouble(aTokenizer.nextToken());
				boolean				invisible = Integer.parseInt(aTokenizer.nextToken()) < 0;
				
				gMonstersByName.put(aName, new Monster(aName, aName.substring(0, 2), points, worth, invisible));
			}
		}
		catch (IOException e)
		{
			System.out.println("IO Error reading monsters " + e);
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