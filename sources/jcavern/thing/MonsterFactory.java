/* 
	MonsterFactory.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern.thing;

import jcavern.*;

import java.util.*;
import java.net.*;
import java.io.*;

/**
 * The MonsterFactory creates Monsters based on the monsters.dat file. It also
 * creates Missions appropriate to the prowess of the player.
 *
 * @author	Bill Walker
 * @version	$Id$
 */
public class MonsterFactory
{
	/** * A list of Monsters stored in the order they were read from monsters.dat. */
	public static Vector			gMonsters;
	
	/**
	 * Retrieves an appropriate opponent for this Player.
	 *
	 * @param	aPlayer		a non-null Player for against whom worthiness is measured.
	 * @return				a worthy, non-null Monster
	 */	
	public static Monster getWorthyOpponent(Player aPlayer)
	{
		int		randomIndex = (int) (Math.random() * gMonsters.size());
		int		thresholdPoints = aPlayer.getPoints() / 2;
		
		while (((Monster) gMonsters.elementAt(randomIndex)).getPoints() > thresholdPoints)
		{
			randomIndex = (int) (Math.random() * gMonsters.size());
		}
		
		return (Monster) ((Monster) gMonsters.elementAt(randomIndex)).clone();
	}
	
	/**
	 * Retrieves the first opponent tougher than this Player.
	 *
	 * @param	aPlayer 	a non-null Player against whom the monsters are compared
	 * @return				an instance of the first kind of Monster with more points than the given player.
	 */	
	public static Monster getMostWorthyOpponent(Player aPlayer)
	{
		int		monsterIndex = 0;
		int		thresholdPoints = aPlayer.getPoints() / 2;
		
		while (((Monster) gMonsters.elementAt(monsterIndex)).getPoints() <= thresholdPoints)
		{
			monsterIndex++;
		}
		
		return (Monster) ((Monster) gMonsters.elementAt(monsterIndex)).clone();
	}
	
	/**
	 * Creates an appropriate Mission for a given player.
	 *
	 * @param	aPlayer 	a non-null Player for whom the Mission will be created
	 * @return				a non-null Mission.
	 */
	public static Mission createMission(Player aPlayer)
	{
		/*
		{setup the mission completion variables}
		Mis_done := false; Mis_quota := round(ln(exp)) + Random(3);
		
		{scan through the monster list looking for an appropriate mission}
		i := 1;
		while (i<MaxMonster) and (exp>M_list[i].points) do i := i + 1;
		Mis_target := i;
		*/
		
		int quota = (int) (Math.log(aPlayer.getPoints()) + 3 * Math.random());
		
		return new Mission(getMostWorthyOpponent(aPlayer), quota);
	}
	
	/**
	 * Load the prototypical monsters from the monsters.dat file.
	 *
	 * @param	aURL	a non-null giving the location of the <CODE>monsters.dat</CODE> file.
	 */
	public static void loadPrototypes(URL aURL)
	{
		System.out.println("loadPrototypes(" + aURL + ")");
		
		gMonsters = new Vector();
		
		try
		{
			//InputStream		aStream = MonsterFactory.class.getResourceAsStream("/monster.dat");
			InputStream		aStream = aURL.openStream();
			BufferedReader	aReader = new BufferedReader(new InputStreamReader(aStream));
			String			aMessageLine;
			int				imageNameIndex = 0;

			while ((aMessageLine = aReader.readLine()) != null)
			{
				StringTokenizer		aTokenizer = new StringTokenizer(aMessageLine, ",");
				
				String				aName = aTokenizer.nextToken().trim();
				String				hitVerb = aTokenizer.nextToken().trim();			
				String				killedVerb = aTokenizer.nextToken().trim();				
				Double				points = Double.valueOf(aTokenizer.nextToken().trim());
				Double				worth = Double.valueOf(aTokenizer.nextToken().trim());
				Integer				invisible = Integer.valueOf(aTokenizer.nextToken().trim());
				
				String				imageName;
				
				if (aName.startsWith("Tree"))
				{
					imageName = "tree2";
				}
				else
				{
					imageName = JCavernApplet.MonsterImageNames[(imageNameIndex++) %
									JCavernApplet.MonsterImageNames.length];
				}

				gMonsters.addElement(
					new Monster(
						aName,
						imageName,
						hitVerb,
						killedVerb,
						points.doubleValue(),
						worth.doubleValue(),
						invisible.intValue() < 0));
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

		// System.out.println("STOP " + gMonsters);
	}
}
