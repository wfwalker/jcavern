
package jcaverntest;

import junit.framework.*;

import jcavern.*;

/**
 * Tests functionality of jcavern.
 *
 * @author	Bill Walker
 */
public class WorldTest extends TestCase
{
	/** * A model of the game world */
	private World			mWorld;
	
	/** * The representation of the player */
	private Player			mPlayer;

	/**
	 * Creates a suite of tests.
	 */
	public WorldTest(String name)
	{
		super(name);
	}

	/**
	 * Builds a world and a player, puts the player in the world.
	 */
	public void setUp() throws ThingCollisionException
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
	 * Tests whether two trees can be placed into the same location.
	 */
	public void testFindPlayer() throws NoSuchThingException
	{
		Location	expectedLocation = new Location(5, 5);
		Location	foundLocation = mWorld.getLocation(mPlayer);
		
		assert("testFindPlayer", expectedLocation.equals(foundLocation));
	}
	
	/**
	 * Tests whether two trees can be placed into the same location.
	 */
	public void testCollision() throws ThingCollisionException, NoSuchThingException
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

		mWorld.remove(aTree);
	}
	
	/**
	 * Tests whether you can find a tree that ain't there.
	 */
	public void testRetrieval() throws ThingCollisionException
	{
		Tree		aTree = new Tree();
		
		try
		{
			mWorld.getLocation(aTree);
			fail("testRetrieval exception");
		}
		catch(NoSuchThingException e)
		{
		}
	}
	
	/**
	 * Tests whether you can remove a tree that ain't there.
	 */
	public void testBogusRemoval() throws ThingCollisionException
	{
		Tree		aTree = new Tree();
		
		try
		{
			mWorld.remove(aTree);
			fail("testBogusRemoval exception");
		}
		catch(NoSuchThingException e)
		{
		}
	}
	
	/**
	 * Tests whether you can attack with a tree that ain't there.
	 */
	public void testBogusAttacker()
	{
		Tree		aTree = new Tree();
		
		try
		{
			mWorld.attack(aTree, Location.WEST);
			fail("testBogusAttacker failed exception");
		}
		catch(NoSuchThingException e)
		{
		}
	}
	
	/**
	 * Tests whether you can attack an empty square
	 */
	public void testAttackNothing()
	{
		try
		{
			mWorld.attack(mPlayer, Location.WEST);
			fail("testAttackNothing failed exception");
		}
		catch(NoSuchThingException e)
		{
		}
	}
	
	/**
	 * Tests whether you can attack and remove a tree.
	 */
	public void testAttackTree() throws NoSuchThingException, ThingCollisionException
	{
		Tree		aTree = new Tree();
		
		Location	playerLocation = mWorld.getLocation(mPlayer);
		Location	treeLocation = playerLocation.getNeighbor(Location.WEST);
		
		mWorld.place(treeLocation, aTree);		
		mWorld.attack(mPlayer, Location.WEST);
		
		try
		{
			mWorld.getThing(treeLocation);
			fail("testAttackTree, tree still there");
		}
		catch(NoSuchThingException nste)
		{
		}
	}
}