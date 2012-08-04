
package jcaverntest;

import junit.framework.*;

import jcavern.*;
import jcavern.thing.*;

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
	
	/** * the representation of the world */
	private World			mWorld;

	/**
	 * Creates a suite of tests.
	 *
	 * @param	name	the suite name
	 */
	public PlayerTest(String name)
	{
		super(name);
	}

	/**
	 * Builds a world and a player, puts the player in the world.
	 *
	 * @exception	ThingCollisionException		placed two things in the same place
	 * @exception	JCavernInternalError		trouble setting up things
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
	 * Tests whether you can convert a Player to and from a data String.
	 *
	 * @exception JCavernInternalError	had internal problem
	 */
	public void testSaveLoad() throws JCavernInternalError
	{
		Treasure anItem = new Sword("Fred", 1);
		
		mPlayer.receiveItem(anItem);

		byte[]		theBytes = ThingUtils.thingToByteArray(mPlayer);
		String		dataString = ThingUtils.byteArrayToDataString(theBytes);
		byte[]		moreBytes = ThingUtils.dataStringToByteArray(dataString);
		
		assertTrue("bytes the same length", theBytes.length == moreBytes.length);

		for (int index = 0; index < theBytes.length; index++)
		{
			assertTrue("byte " + index + " the same", theBytes[index]== moreBytes[index]);
		}
		
		Player newPlayer = (Player) ThingUtils.byteArrayToThing(moreBytes);

		assertTrue("rehydrated name matches", newPlayer.getName().equals(mPlayer.getName()));
		assertTrue("rehydrated points matches", newPlayer.getPoints() == mPlayer.getPoints());
		assertTrue("rehydrated gold matches", newPlayer.getGold() == mPlayer.getGold());
		assertTrue("rehydrated sword matches", newPlayer.getSword().getName().equals(mPlayer.getSword().getName()));
	}
	
	/**
	 * Tests whether you can use and unuse items.
	 *
	 * @exception JCavernInternalError	had internal problem
	 */
	public void testItems() throws JCavernInternalError
	{
		Treasure anItem = new Sword("Fred", 1);
		
		mPlayer.receiveItem(anItem);
		
		assertTrue("testItems received item1 ", mPlayer.getUnusedItems().contains(anItem));
		assertTrue("testItems received item2 ", ! mPlayer.getInUseItems().contains(anItem));
		
		mPlayer.startUsing(anItem);
		
		assertTrue("testItems start using item1 ", ! mPlayer.getUnusedItems().contains(anItem));
		assertTrue("testItems start using item2 ", mPlayer.getInUseItems().contains(anItem));
		
		mPlayer.stopUsing(anItem);
		
		assertTrue("testItems stop using 1 ", mPlayer.getUnusedItems().contains(anItem));
		assertTrue("testItems stop using 2", ! mPlayer.getInUseItems().contains(anItem));
	}
}
