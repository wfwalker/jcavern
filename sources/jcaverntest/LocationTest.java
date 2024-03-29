
package jcaverntest;

import junit.framework.*;

import java.awt.Rectangle;
import jcavern.*;

/**
 * Tests functionality of Location.
 *
 * @author	Bill Walker
 * @version	$Id$
 */
public class LocationTest extends TestCase
{	
	/**
	 * Creates a suite of tests.
	 *
	 * @param	name	name of the suite of tests.
	 */
	public LocationTest(String name)
	{
		super(name);
	}
	
	/**
	 * Tests whether enforceMinimumOffset works correctly.
	 */
	public void testInset()
	{
		World		aWorld = new World();
		
		// world coordinates goes from 0 to 19
		Location	location1 = new Location(10, 20);
		Location	desiredLocation1 = new Location(10, 15);
		
		assertTrue("inset 1", aWorld.enforceMinimumInset(location1, 4).equals(desiredLocation1));
		
		Location	location2 = new Location(20, 10);
		Location	desiredLocation2 = new Location(15, 10);
		
		assertTrue("inset 1", aWorld.enforceMinimumInset(location2, 4).equals(desiredLocation2));
		
		Location	location3 = new Location(10, 2);
		Location	desiredLocation3 = new Location(10, 4);
		
		assertTrue("inset 1", aWorld.enforceMinimumInset(location3, 4).equals(desiredLocation3));
		
		Location	location4 = new Location(2, 10);
		Location	desiredLocation4 = new Location(4, 10);
		
		assertTrue("inset 1", aWorld.enforceMinimumInset(location4, 4).equals(desiredLocation4));
	}

	/**
	 * Tests whether opposite directions are really opposite of one another.
	 */
	public void testReciprocal()
	{
		Location zero = new Location(10, 10);

		assertTrue("north vs south", zero.getNeighbor(Location.NORTH).getNeighbor(Location.SOUTH).equals(zero));
		assertTrue("south vs north", zero.getNeighbor(Location.SOUTH).getNeighbor(Location.NORTH).equals(zero));
		
		assertTrue("northeast vs southwest", zero.getNeighbor(Location.NORTHEAST).getNeighbor(Location.SOUTHWEST).equals(zero));
		assertTrue("southwest vs northeast", zero.getNeighbor(Location.SOUTHWEST).getNeighbor(Location.NORTHEAST).equals(zero));
		
		assertTrue("east vs west", zero.getNeighbor(Location.EAST).getNeighbor(Location.WEST).equals(zero));
		assertTrue("west vs east", zero.getNeighbor(Location.WEST).getNeighbor(Location.EAST).equals(zero));
		
		assertTrue("southeast vs northwest", zero.getNeighbor(Location.SOUTHEAST).getNeighbor(Location.NORTHWEST).equals(zero));
		assertTrue("northwest vs southeast", zero.getNeighbor(Location.NORTHWEST).getNeighbor(Location.SOUTHEAST).equals(zero));		
	}
	
	/**
	 * Tests whether Locations can correctly compute the direction toward each other.
	 */
	public void testNeighborToward()
	{
		Location zero = new Location(10, 10);
		
		assertTrue("neighbor toward east", zero.getNeighborToward(new Location(20, 10)).equals(new Location(11, 10)));
		assertTrue("neighbor toward west", zero.getNeighborToward(new Location(0, 10)).equals(new Location(9, 10)));
		
		assertTrue("neighbor toward north", zero.getNeighborToward(new Location(10, 0)).equals(new Location(10, 9)));
		assertTrue("neighbor toward south", zero.getNeighborToward(new Location(10, 20)).equals(new Location(10, 11)));
		
		assertTrue("neighbor toward northeast", zero.getNeighborToward(new Location(20, 0)).equals(new Location(11, 9)));
		assertTrue("neighbor toward northwest", zero.getNeighborToward(new Location(0, 0)).equals(new Location(9, 9)));
		
		assertTrue("neighbor toward southeast", zero.getNeighborToward(new Location(20, 20)).equals(new Location(11, 11)));
		assertTrue("neighbor toward southwest", zero.getNeighborToward(new Location(0, 20)).equals(new Location(9, 11)));
	}
}