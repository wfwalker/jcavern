
package jcaverntest;

import junit.framework.*;

import jcavern.*;

/**
 * Tests functionality of player.
 *
 * @author	Bill Walker
 * @version	$Id$
 */
public class PlayerTest extends TestCase
{
	/** * The representation of the player */
	private Player			mPlayer;
	
	private World			mWorld;

	/**
	 * Creates a suite of tests.
	 */
	public PlayerTest(String name)
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
	
	
	public void testItems() throws JCavernInternalError
	{
		Treasure anItem = new Sword("Fred", 1);
		
		mPlayer.receiveItem(anItem);
		
		assert("testItems received item1 ", mPlayer.getUnusedItems().contains(anItem));
		assert("testItems received item2 ", ! mPlayer.getInUseItems().contains(anItem));
		
		mPlayer.startUsing(anItem);
		
		assert("testItems start using item1 ", ! mPlayer.getUnusedItems().contains(anItem));
		assert("testItems start using item2 ", mPlayer.getInUseItems().contains(anItem));
		
		mPlayer.stopUsing(anItem);
		
		assert("testItems stop using 1 ", mPlayer.getUnusedItems().contains(anItem));
		assert("testItems stop using 2", ! mPlayer.getInUseItems().contains(anItem));
	}
}