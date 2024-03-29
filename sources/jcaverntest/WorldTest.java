
package jcaverntest;

import junit.framework.*;

import jcavern.*;
import jcavern.thing.*;

/**
 * Tests functionality of jcavern.
 *
 * @author	Bill Walker
 * @version	$Id$
 */
public class WorldTest extends TestCase
{
	/** * A model of the game world */
	private World			mWorld;
	
	/** * The representation of the player */
	private Player			mPlayer;

	/**
	 * Creates a suite of tests.
	 *
	 * @param	name		name of the suite of tests
	 */
	public WorldTest(String name)
	{
		super(name);
	}

	/**
	 * Builds a world and a player, puts the player in the world.
	 *
	 * @exception	ThingCollisionException		two things landed up in the same place
	 * @exception	JCavernInternalError		could not set up the world
	 */
	public void setUp() throws ThingCollisionException, JCavernInternalError
	{
		// Create a world
		mWorld = new World();

		// Create a player
		mPlayer  = new Player("Bill");

		// Put the player in the world
		mWorld.place(new Location(5, 5), mPlayer);
	}
	
	/**
	 * Tears down after tests. Not currently in use.
	 */
	public void tearDown()
	{
		mWorld = null;
		mPlayer = null;
	}
	
	
	/**
	 * Tests whether you can find the player where you left him.
	 *
	 * @exception	JCavernInternalError		could not complete the test
	 */
	public void testFindPlayer() throws JCavernInternalError
	{
		Location	expectedLocation = new Location(5, 5);
		Location	foundLocation = mWorld.getLocation(mPlayer);
		
		assertTrue("testFindPlayer", expectedLocation.equals(foundLocation));
	}
	
	/**
	 * Tests whether two trees can be placed into the same location.
	 *
	 * @exception	ThingCollisionException		could not complete the test
	 * @exception	JCavernInternalError		could not complete the test
	 */
	public void testCollision() throws ThingCollisionException, JCavernInternalError
	{
		Tree		aTree = new Tree();
		Tree		anotherTree = new Tree();
		Location	aLocation = mWorld.getRandomEmptyLocation();
		
		mWorld.place(aLocation, aTree);
		
		try
		{
			mWorld.place(aLocation, anotherTree);
			fail("testCollision exception");
		}
		catch(ThingCollisionException e)
		{
		}
	}
	
	/**
	 * Tests whether you can find a tree that ain't there.
	 */
	public void testRetrieval()
	{
		Tree		aTree = new Tree();
		
		try
		{
			mWorld.getLocation(aTree);
			fail("testRetrieval exception");
		}
		catch(JCavernInternalError e)
		{
		}
	}
	
	/**
	 * Tests whether a World properly detects Locations within and outside its bounds.
	 */
	public void testBounds()
	{
		assertTrue("5, -1 out", mWorld.inBounds(new Location(5, -1)) == false);
		assertTrue("-1, 5 out", mWorld.inBounds(new Location(-1, 5)) == false);
		
		assertTrue("5, 0 in", mWorld.inBounds(new Location(5, 0)) == true);
		assertTrue("0, 5 in", mWorld.inBounds(new Location(0, 5)) == true);
		
		assertTrue("5, 20 out", mWorld.inBounds(new Location(5, 20)) == false);
		assertTrue("20, 5 out", mWorld.inBounds(new Location(20, 5)) == false);
		
		assertTrue("5, 19 in", mWorld.inBounds(new Location(5, 19)) == true);
		assertTrue("19, 5 in", mWorld.inBounds(new Location(19, 5)) == true);

	}
	
	/**
	 * Tests whether a World properly retrieves things.
	 */
	public void testGetThings()
	{
		assertTrue("found something", mWorld.getThings().size() > 0);
		assertTrue("found something", mWorld.getThings().contains(mPlayer));
	}

	/**
	 * Tests whether you can remove a tree that ain't there.
	 *
	 * @exception	ThingCollisionException		two things landed up in the same place
	 */
	public void testBogusRemoval() throws ThingCollisionException
	{
		Tree		aTree = new Tree();
		
		try
		{
			mWorld.remove(aTree);
			fail("testBogusRemoval exception");
		}
		catch(JCavernInternalError e)
		{
		}
	}
}
