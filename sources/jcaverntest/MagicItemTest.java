
package jcaverntest;

import junit.framework.*;

import java.awt.Rectangle;
import jcavern.*;

/**
 * Tests functionality of MagicItem.
 *
 * @author	Bill Walker
 * @version	$Id$
 */
public class MagicItemTest extends TestCase
{	
	/** * A model of the game world */
	private World			mWorld;
	
	/** * The representation of the player */
	private Player			mPlayer;
	
	/**
	 * Builds a world and a player, puts the player in the world.
	 *
	 * @exception	ThingCollisionException		placed two things in the same place
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
	 * Creates a suite of tests.
	 */
	public MagicItemTest(String name)
	{
		super(name);
	}
	
	/**
	 * Tests whether the reveal invisibility magic item works.
	 *
	 * @exception	ThingCollisionException		placed two things in the same place
	 * @exception	JCavernInternalError		some internal problem
	 */
	public void testRevealInvisibility() throws ThingCollisionException, JCavernInternalError
	{
		Monster		testMonster = new Monster("invisio", "monster", "hit", "killed", 1, 1, true);
		MagicItem	testItem = new MagicItem("revealio", 1);
		mWorld.place(new Location(6, 6), testMonster);
		
		assert("hidden", testMonster.getInvisible() == true);

		testItem.startUseBy(mPlayer, mWorld);
		
		assert("revealed", testMonster.getInvisible() == false);
	}

}
