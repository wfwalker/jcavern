
package jcaverntest;

import junit.framework.*;

import jcavern.*;

/**
 * Tests functionality of Location.
 *
 * @author	Bill Walker
 */
public class LocationTest extends TestCase
{	
	/**
	 * Creates a suite of tests.
	 */
	public LocationTest(String name)
	{
		super(name);
	}

	public void testNeighborToward()
	{
		Location zero = new Location(10, 10);
		
		assert("neighbor toward east", zero.getNeighborToward(new Location(20, 10)).equals(new Location(11, 10)));
		assert("neighbor toward west", zero.getNeighborToward(new Location(0, 10)).equals(new Location(9, 10)));
		
		assert("neighbor toward north", zero.getNeighborToward(new Location(10, 0)).equals(new Location(10, 9)));
		assert("neighbor toward south", zero.getNeighborToward(new Location(10, 20)).equals(new Location(10, 11)));
		
		assert("neighbor toward northeast", zero.getNeighborToward(new Location(20, 0)).equals(new Location(11, 9)));
		assert("neighbor toward northwest", zero.getNeighborToward(new Location(0, 0)).equals(new Location(9, 9)));
		
		assert("neighbor toward southeast", zero.getNeighborToward(new Location(20, 20)).equals(new Location(11, 11)));
		assert("neighbor toward southwest", zero.getNeighborToward(new Location(0, 20)).equals(new Location(9, 11)));
	}
}