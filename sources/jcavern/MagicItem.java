/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

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
}