
package jcaverntest;

import junit.framework.*;

import jcavern.*;

/**
 * Tests functionality of jcavern.
 *
 * @author	Bill Walker
 */
public class CombatantTest extends TestCase
{
	/** * A model of the game world */
	private World			mWorld;
	
	/** * The representation of the player */
	private Player			mPlayer;

	/**
	 * Creates a suite of tests.
	 */
	public CombatantTest(String name)
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
	 * Tests whether you can attack with a tree that ain't there.
	 */
	public void testBogusAttacker() throws NonCombatantException
	{
		Player		aPlayer = new Player("bogus");
		
		try
		{
			aPlayer.attack(mWorld, Location.WEST);
			fail("testBogusAttacker failed exception");
		}
		catch(JCavernInternalError e)
		{
		}
		catch(IllegalLocationException ile)
		{
		}
		catch(EmptyLocationException ile)
		{
		}
	}
	
	/**
	 * Tests whether you can attack an empty square
	 */
	public void testAttackNothing() throws NonCombatantException
	{
		try
		{
			mPlayer.attack(mWorld, Location.WEST);
			fail("testAttackNothing failed exception");
		}
		catch(JCavernInternalError e)
		{
		}
		catch(IllegalLocationException ile)
		{
		}
		catch(EmptyLocationException ile)
		{
		}
	}
	
	/**
	 * Tests whether you can attack and remove a tree.
	 */
	public void testAttackTree() throws ThingCollisionException, NonCombatantException, EmptyLocationException, IllegalLocationException, JCavernInternalError
	{
		Tree		aTree = new Tree();
		
		Location	playerLocation = mWorld.getLocation(mPlayer);
		Location	treeLocation = playerLocation.getNeighbor(Location.WEST);
		
		mWorld.place(treeLocation, aTree);		
		mPlayer.attack(mWorld, Location.WEST);
		
		assert("testAttackTree removes tree", mWorld.isEmpty(treeLocation));
	}
}