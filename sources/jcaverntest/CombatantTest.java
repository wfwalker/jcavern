
package jcaverntest;

import junit.framework.*;

import jcavern.*;

/**
 * Tests functionality of jcavern.
 *
 * @author	Bill Walker
 * @version	$Id$
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
	
	/**
	 * Tests whether you can attack and remove a tree without a sword!
	 */
	public void testAttackTreeWithoutSword() throws ThingCollisionException, NonCombatantException, EmptyLocationException, IllegalLocationException, JCavernInternalError
	{
		Tree		aTree = new Tree();
		
		Location	playerLocation = mWorld.getLocation(mPlayer);
		Location	treeLocation = playerLocation.getNeighbor(Location.WEST);
		
		mPlayer.stopUsing(mPlayer.getInUseTreasureAt(0));
		
		mWorld.place(treeLocation, aTree);		
		mPlayer.attack(mWorld, Location.WEST);
		
		assert("testAttackTreeWithoutSword removed tree", ! mWorld.isEmpty(treeLocation));
	}
	
	public void testVulnerability()
	{
		Monster aMonster = new Monster("Bogus", "monster", 1, 1, false);
		Tree	aTree = new Tree();
		
		assert("player attacks monster", mPlayer.canAttack(aMonster));
		assert("player attacks tree", mPlayer.canAttack(aTree));
		assert("monster attacks player", aMonster.canAttack(mPlayer));
		assert("monster NOT attacks tree", ! aMonster.canAttack(aTree));
		assert("tree NOT attacks player", ! aTree.canAttack(mPlayer));
		assert("tree NOT attacks monster", ! aTree.canAttack(aMonster));
	}
	
	public void testPlayerInCastleVulnerability()
	{
		Monster aMonster = new Monster("Bogus", "monster", 1, 1, false);
		Tree	aTree = new Tree();
		
		mPlayer.setCastle(new Castle());
		
		assert("monster NOT attacks player", ! aMonster.canAttack(mPlayer));
		assert("tree NOT attacks player", ! aTree.canAttack(mPlayer));
	}
}