
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
public class CombatantTest extends TestCase
{
	/** * A model of the game world */
	private World			mWorld;
	
	/** * The representation of the player */
	private Player			mPlayer;

	/**
	 * Creates a suite of tests.
	 *
	 * @param	name	the name of the suite of tests.
	 */
	public CombatantTest(String name)
	{
		super(name);
	}

	/**
	 * Builds a world and a player, puts the player in the world.
	 *
	 * @exception	ThingCollisionException		trouble placing Things
	 * @exception	JCavernInternalError		trouble placing Things
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
	 * Tests whether you can attack with a player that hasn't been placed in the world.
	 *
	 * @exception	NonCombatantException		bogus attacker or attackee
	 * @exception	EmptyLocationException		nothing to attack
	 * @exception	IllegalLocationException	attacking off the edge of the world
	 */
	public void testBogusAttacker() throws NonCombatantException, IllegalLocationException, EmptyLocationException
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
	}
	
	/**
	 * Tests whether you can attack an empty square
	 *
	 * @exception	NonCombatantException		bogus attacker or attackee
	 * @exception	IllegalLocationException	attacking off the edge of the world
	 * @exception	JCavernInternalError		some kind of internal error
	 */
	public void testAttackNothing() throws NonCombatantException, IllegalLocationException, JCavernInternalError
	{
		try
		{
			mPlayer.attack(mWorld, Location.WEST);
			fail("testAttackNothing failed exception");
		}
		catch(EmptyLocationException ile)
		{
		}
	}
	
	/**
	 * Tests whether you can attack and remove a tree.
	 *
	 * @exception	ThingCollisionException		placed two things in the same place
	 * @exception	NonCombatantException		bogus attacker or attackee
	 * @exception	EmptyLocationException		nothing to attack
	 * @exception	IllegalLocationException	attacking off the edge of the world
	 * @exception	JCavernInternalError		some kind of internal error
	 */
	public void testAttackTree() throws ThingCollisionException, NonCombatantException, EmptyLocationException, IllegalLocationException, JCavernInternalError
	{
		Tree		aTree = new Tree();
		
		Location	playerLocation = mWorld.getLocation(mPlayer);
		Location	treeLocation = playerLocation.getNeighbor(Location.WEST);
		
		mWorld.place(treeLocation, aTree);		
		mPlayer.attack(mWorld, Location.WEST);
		
		assertTrue("testAttackTree removes tree", mWorld.isEmpty(treeLocation));
	}
	
	/**
	 * Tests whether you can attack and remove a tree without a sword!
	 *
	 * @exception	ThingCollisionException		placed two things in the same place
	 * @exception	NonCombatantException		bogus attacker or attackee
	 * @exception	EmptyLocationException		nothing to attack
	 * @exception	IllegalLocationException	attacking off the edge of the world
	 * @exception	JCavernInternalError		some kind of internal error
	 */
	public void testAttackTreeWithoutSword() throws ThingCollisionException, NonCombatantException, EmptyLocationException, IllegalLocationException, JCavernInternalError
	{
		Tree		aTree = new Tree();
		
		Location	playerLocation = mWorld.getLocation(mPlayer);
		Location	treeLocation = playerLocation.getNeighbor(Location.WEST);
		
		mPlayer.stopUsing(mPlayer.getInUseTreasureAt(1));
		
		mWorld.place(treeLocation, aTree);		
		mPlayer.attack(mWorld, Location.WEST);
		
		assertTrue("testAttackTreeWithoutSword removed tree", ! mWorld.isEmpty(treeLocation));
	}
	
	/**
	 * Tests whether players, monsters, and trees are appropriately vulnerable to each other.
	 */
	public void testVulnerability()
	{
		Monster aMonster = new Monster("Bogus", "monster", "hit", "killed", 1, 1, false);
		Tree	aTree = new Tree();
		
		assertTrue("player attacks monster", mPlayer.canAttack(aMonster));
		assertTrue("player attacks tree", mPlayer.canAttack(aTree));
		assertTrue("monster attacks player", aMonster.canAttack(mPlayer));
		assertTrue("monster NOT attacks tree", ! aMonster.canAttack(aTree));
		assertTrue("tree NOT attacks player", ! aTree.canAttack(mPlayer));
		assertTrue("tree NOT attacks monster", ! aTree.canAttack(aMonster));
	}
	
	/**
	 * Tests whether a player in a castle is vulnerable or not.
	 */
	public void testPlayerInCastleVulnerability()
	{
		Monster aMonster = new Monster("Bogus", "monster", "hit", "killed", 1, 1, false);
		Tree	aTree = new Tree();
		
		mPlayer.setCastle(new Castle());
		
		assertTrue("monster NOT attacks player", ! aMonster.canAttack(mPlayer));
		assertTrue("tree NOT attacks player", ! aTree.canAttack(mPlayer));
	}
	
	/**
	 * Tests the rules by which Combatants are highlighted.
	 */
	public void testHighlightMonsters()
	{
		Combatant 	aMonster = new Monster("Bogus", "monster", "hit", "killed", 1, 1, false);
		Combatant 	anotherMonster = new Monster("Bogus", "monster2", "hit", "killed", 1, 1, false);
		Tree		aTree = new Tree();
		
		WorldEvent	playerHitsMonster = CombatEvent.hit(null, aMonster, mPlayer, 1);
		WorldEvent	monsterHitsPlayer = CombatEvent.hit(null, mPlayer, aMonster, 1);
		
		WorldEvent	playerHitsTree = CombatEvent.hit(null, aTree, mPlayer, 1);
		WorldEvent	monsterMissesTree = CombatEvent.missed(null, aTree, aMonster);
		
		WorldEvent	playerMissesMonster = CombatEvent.missed(null, aMonster, mPlayer);
		WorldEvent	monsterMissesPlayer = CombatEvent.missed(null, mPlayer, aMonster);
		
		WorldEvent	monster2MissesMonster = CombatEvent.missed(null, anotherMonster, aMonster);
		WorldEvent	monster2HitsMonster = CombatEvent.hit(null, anotherMonster, aMonster, 1);

		assertTrue("highlight monster when player hits monster", aMonster.getGraphicalThingView().shouldHighlight(playerHitsMonster));
		assertTrue("highlight player when player hits monster", mPlayer.getGraphicalThingView().shouldHighlight(playerHitsMonster));

		assertTrue("highlight tree when player hits tree", ! aTree.getGraphicalThingView().shouldHighlight(playerHitsTree));
		assertTrue("highlight player when player hits tree", mPlayer.getGraphicalThingView().shouldHighlight(playerHitsTree));

		assertTrue("highlight tree when monster misses tree", ! aTree.getGraphicalThingView().shouldHighlight(monsterMissesTree));
		assertTrue("highlight monster when monster misses tree", ! aMonster.getGraphicalThingView().shouldHighlight(monsterMissesTree));

		assertTrue("highlight monster when player misses monster", aMonster.getGraphicalThingView().shouldHighlight(playerMissesMonster));
		assertTrue("highlight player when player misses monster", mPlayer.getGraphicalThingView().shouldHighlight(playerMissesMonster));

		assertTrue("highlight monster when monster hits player", aMonster.getGraphicalThingView().shouldHighlight(monsterHitsPlayer));
		assertTrue("highlight player when monster hits player", mPlayer.getGraphicalThingView().shouldHighlight(monsterHitsPlayer));

		assertTrue("highlight monster when monster misses player", aMonster.getGraphicalThingView().shouldHighlight(monsterMissesPlayer));
		assertTrue("highlight player when monster misses player ", mPlayer.getGraphicalThingView().shouldHighlight(monsterMissesPlayer));

		assertTrue("highlight player when monster misses monster2 ", ! mPlayer.getGraphicalThingView().shouldHighlight(monster2MissesMonster));
		assertTrue("highlight player when monster misses monster2 ", ! mPlayer.getGraphicalThingView().shouldHighlight(monster2HitsMonster));
	}
	
}
