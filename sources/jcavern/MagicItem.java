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
	private int					mPower;
	
	private static final int	MAGIC_NOTHING = 0;
	private static final int	MAGIC_REVEAL_INVISIBILITY = 1;
	private static final int	MAGIC_TELEPORTATION = 2;
	private static final int	MAGIC_DETECT_TREASURE = 3;
	private static final int	MAGIC_DETECT_MAGIC_CASTLE = 4;
	
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
		JCavernApplet.log(aPlayer.getName() + " uses magic item " + getName());
		
		switch (mPower)
		{
			case MAGIC_NOTHING:
					JCavernApplet.log(getName() + ": nothing happens"); break;
					
			case MAGIC_REVEAL_INVISIBILITY:
					JCavernApplet.log(getName() + ": reveal invisibility"); break;
					
			case MAGIC_TELEPORTATION:
					JCavernApplet.log(getName() + ": teleportation"); 
					doTeleportation(aPlayer, aWorld); break;
					
			case MAGIC_DETECT_TREASURE:
					JCavernApplet.log(getName() + ": detect treasure"); 
					doDetectTreasure(aPlayer, aWorld); break;
					
			case MAGIC_DETECT_MAGIC_CASTLE:
					JCavernApplet.log(getName() + ": detect magic castle"); 
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
	
	private void doDetectThings(Thing seeker, World aWorld, Thing aPrototype) throws JCavernInternalError
	{
		Vector	theThings = aWorld.getThings(aPrototype);
		
		for (int index = 0; index < theThings.size(); index++)
		{
			Thing	detectee = (Thing) theThings.elementAt(index);
			int		direction = aWorld.getDirectionToward(seeker, detectee);
			
			JCavernApplet.log("There is a " +
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