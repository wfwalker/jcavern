
package jcaverntest;

import junit.framework.*;
import java.net.*;

import jcavern.*;
import jcavern.thing.*;

/**
 * Tests functionality of Mission.
 *
 * @author	Bill Walker
 * @version	$Id$
 */
public class MissionTest extends TestCase
{
	/** * A game world for testing. */
	private World	mWorld;
	
	/** * A player with a mission; for testing. */
	private Player	mPlayer;
	
	/**
	 * Creates a suite of tests.
	 *
	 * @param	name	a non-null String containing a test name.
	 */
	public MissionTest(String name)
	{
		super(name);
		
		mWorld = new World();
		mPlayer= new Player("Mr. Test");
	}

	/**
	 * Tests whether mission quotas update correctly, and whether Mission.toString() is correct.
	 *
	 * @exception	MalformedURLException	bogus URL for the <CODE>monster.dat</CODE> file.
	 */
	public void testQuota() throws MalformedURLException
	{
		MonsterFactory.loadPrototypes(new URL("file:bin/monster.dat"));
		
		Player		aPlayer = new Player("Mr. Test");
		Mission		aMission = MonsterFactory.createMission(aPlayer);
		
		for (int index = 0; index < aMission.getQuota(); index++)
		{
			// System.out.println("MissionTest.testQuota() " + aMission);
			
			assert("mission not complete", ! aMission.getCompleted());
			assert("mission name says not complete", aMission.toString().indexOf("completed") == -1);
			aMission.adjustQuota(mWorld, mPlayer, (Thing) aMission.getTarget().clone());
		}
			
		// System.out.println("MissionTest.testQuota() " + aMission);

		assert("mission complete", aMission.getCompleted());
		assert("mission name says complete", aMission.toString().indexOf("completed") >= 0);
	}
}