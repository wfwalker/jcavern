/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

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
	private static final int	MAGIC_NOTHING = 0;
	
	/** * Indicates the power to permanently reveal invisiblet Things. */
	private static final int	MAGIC_REVEAL_INVISIBILITY = 1;

	/** * Indicates the power to teleport the player randomly within the world. */
	private static final int	MAGIC_TELEPORTATION = 2;

	/** * Indicates the power to locate all trasure chests, regardless of location. */
	private static final int	MAGIC_DETECT_TREASURE = 3;

	/** * Indicates the poiwer to location all magic castles, reguardless of location */
	private static final int	MAGIC_DETECT_MAGIC_CASTLE = 4;
	
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
		JCavernWindow.log(aPlayer.getName() + " uses magic item " + getName());
		
		switch (mPower)
		{
			case MAGIC_NOTHING:
					JCavernWindow.log(getName() + ": nothing happens"); break;
					
			case MAGIC_REVEAL_INVISIBILITY:
					JCavernWindow.log(getName() + ": reveal invisibility");
					doRevealInvisibility(aPlayer, aWorld);
					break;
					
			case MAGIC_TELEPORTATION:
					JCavernWindow.log(getName() + ": teleportation"); 
					doTeleportation(aPlayer, aWorld); break;
					
			case MAGIC_DETECT_TREASURE:
					JCavernWindow.log(getName() + ": detect treasure"); 
					doDetectTreasure(aPlayer, aWorld); break;
					
			case MAGIC_DETECT_MAGIC_CASTLE:
					JCavernWindow.log(getName() + ": detect magic castle"); 
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
				JCavernWindow.log("There is an invisible " + detectee.getName());
				detectee.setInvisible(false);
				aWorld.thingChanged(detectee);
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
			
			JCavernWindow.log("There is a " +
					aPrototype.getName() + " " +
					aWorld.getDistanceBetween(seeker, detectee) + " moves " + 
					Location.directionToString(direction) + " of " + seeker.getName());
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