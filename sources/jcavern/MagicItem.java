/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

import jcavern.thing.*;
import jcavern.ui.*;

import java.util.Vector;

/**
 * MagicItems are Things which can perform some magical operation.
 *
 * @author	Bill Walker
 * @version	$Id$
 */
public class MagicItem extends Treasure
{
	/** * Which magic power does this particular item have? */
	private int					mPower;
	
	/** * Indicates the power to do nothing.*/
	public static final int		MAGIC_NOTHING = 0;
	
	/** * Indicates the power to permanently reveal invisiblet Things. */
	public static final int		MAGIC_REVEAL_INVISIBILITY = 1;

	/** * Indicates the power to teleport the player randomly within the world. */
	public static final int		MAGIC_TELEPORTATION = 2;

	/** * Indicates the power to locate all trasure chests, regardless of location. */
	public static final int		MAGIC_DETECT_TREASURE = 3;

	/** * Indicates the poiwer to location all magic castles, reguardless of location */
	public static final int		MAGIC_DETECT_MAGIC_CASTLE = 4;
	
	/**
	 * Creates a new MagicItem with the given power.
	 *
	 * @param		the integer thhat represents this item's magic power.
	 */ 
	public MagicItem(String aName, int power)
	{
		super(aName);
		mPower = power;
	}
	
	public String toString()
	{
		return getName();
	}
	
	public void startUseBy(Player aPlayer, World aWorld) throws JCavernInternalError
	{
		aWorld.eventHappened(new WorldEvent(aPlayer, WorldEvent.INFO_MESSAGE, aPlayer.getName() + " starts using " + getName()));
		
		switch (mPower)
		{
			case MAGIC_NOTHING:
					aWorld.eventHappened(new WorldEvent(aPlayer, WorldEvent.INFO_MESSAGE, getName() + ": nothing happens")); break;
					
			case MAGIC_REVEAL_INVISIBILITY:
					aWorld.eventHappened(new WorldEvent(aPlayer, WorldEvent.INFO_MESSAGE, getName() + ": reveal invisibility"));
					doRevealInvisibility(aPlayer, aWorld);
					break;
					
			case MAGIC_TELEPORTATION:
					aWorld.eventHappened(new WorldEvent(aPlayer, WorldEvent.INFO_MESSAGE, getName() + ": teleportation")); 
					doTeleportation(aPlayer, aWorld); break;
					
			case MAGIC_DETECT_TREASURE:
					aWorld.eventHappened(new WorldEvent(aPlayer, WorldEvent.INFO_MESSAGE, getName() + ": detect treasure")); 
					doDetectTreasure(aPlayer, aWorld); break;
					
			case MAGIC_DETECT_MAGIC_CASTLE:
					aWorld.eventHappened(new WorldEvent(aPlayer, WorldEvent.INFO_MESSAGE, getName() + ": detect magic castle")); 
					doDetectMagicCastle(aPlayer, aWorld); break;
			
			default:
					throw new JCavernInternalError("MagicItem.startUseBy(), known power");
		}
	}
	
	public void stopUseBy(Player aPlayer)
	{
	}

	public Object clone()
	{
		return new MagicItem(new String(getName()), mPower);
	}
		
	public int getPower()
	{
		return mPower;
	}
	
	/**
	 * Removes the Thing from the world and places it randomly.
	 */
	private void doTeleportation(Thing aThing, World aWorld) throws JCavernInternalError
	{
		try
		{
			aWorld.remove(aThing);
			aWorld.place(aWorld.getRandomEmptyLocation(), aThing);
		}
		catch (ThingCollisionException tce)
		{
			throw new JCavernInternalError("can't teleport");
		}
	}
	
	private void doRevealInvisibility(Player aPlayer, World aWorld)
	{
		Vector	theThings = aWorld.getThings();
		
		for (int index = 0; index < theThings.size(); index++)
		{
			Thing	detectee = (Thing) theThings.elementAt(index);

			if (detectee.getInvisible())
			{
				aWorld.eventHappened(new WorldEvent(aPlayer, WorldEvent.INFO_MESSAGE, detectee, "There is an invisible " + detectee.getName()));
				detectee.setInvisible(false);
				aWorld.eventHappened(WorldContentsEvent.revealed(detectee, aPlayer));
			}
		}
	}
	
	private void doDetectThings(Thing seeker, World aWorld, Thing aPrototype) throws JCavernInternalError
	{
		Vector	theThings = aWorld.getThings(aPrototype);
		
		for (int index = 0; index < theThings.size(); index++)
		{
			Thing	detectee = (Thing) theThings.elementAt(index);
			int		direction = aWorld.getDirectionToward(seeker, detectee);
			
			aWorld.eventHappened(new WorldEvent(
					seeker,
					WorldEvent.INFO_MESSAGE,
					detectee,
					"There is a " +
					aPrototype.getName() + " " +
					aWorld.getDistanceBetween(seeker, detectee) + " moves " + 
					Location.directionToString(direction) + " of " + seeker.getName()));
		}
	}
	
	private void doDetectTreasure(Thing seeker, World aWorld) throws JCavernInternalError
	{
		doDetectThings(seeker, aWorld, new TreasureChest(null, 0));
	}
	
	private void doDetectMagicCastle(Thing seeker, World aWorld) throws JCavernInternalError
	{
		doDetectThings(seeker, aWorld, new Castle());
	}
}