/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern.thing;

import java.awt.*;
import jcavern.Treasure;

/**
 * TreasureChests are planted around the world for the Player to find.
 * The contain gold pieces or Treasures.
 *
 * @author	Bill Walker
 * @version	$Id$
 */
public class TreasureChest extends Thing
{
	/** * The treasure item in the treasure chest. */
	private Treasure	mContents;
	
	/** * The number of gold pieces in the treasure chest. */
	private int			mGoldPieces;
	
	/**
	 * Creates a new treasure chest with the given contents.
	 *
	 * @param	aTreasure		which treasure item to put in the chest
	 * @param	howManyGold		how many gold pieces to put in the chest
	 */
	public TreasureChest(Treasure aTreasure, int howManyGold)
	{
		super("Treasure Chest", "chest");
		
		mContents = aTreasure;
		mGoldPieces = howManyGold;
	}
	
	/**
	 * Returns the Treasure Item in the chest.
	 *
	 * @return	a Treasure item, or <CODE>null</CODE> if none.
	 */
	public Treasure getContents()
	{
		return mContents;
	}
	
	/**
	 * Returns the number of gold pieces in the treasure chest.
	 *
	 * @return		the number of gold pieces in the treasure chest
	 */
	public int getGold()
	{
		return mGoldPieces;
	}
	
	/**
	 * Implements the cloning interface.
	 *
	 * @return	a clone of the treasure chest.
	 */
	public Object clone()
	{
		return new TreasureChest(mContents, mGoldPieces);
	}
	
	/**
	 * Returns the text-only appearance of this treasure chest.
	 *
	 * @return	a string appearance for this treasure chest
	 */
	public String getAppearance() { return "$$"; }
	
	/**
	 * Returns a String-based representation of the treasure chest.
	 *
	 * @return	a String-based representation of the treasure chest.
	 */
	public String toString()
	{
		StringBuffer aBuffer = new StringBuffer("Treasure Chest");
		
		if (mGoldPieces > 0)
		{
			aBuffer.append(" with " + mGoldPieces + " gold pieces");		
		}
		if (mContents != null)
		{
			aBuffer.append(" with " + mContents);
		}
		
		return aBuffer.toString();
	}

	/**
	 * Create a random Treasure Chest.
	 *
	 * @return	a non-null Treasure Chest containing either money or a Treasure (i. e., sword).
	 */
	public static TreasureChest createRandom()
	{
		/*
	      SetSect(a,b,chest);           {set up the chest}
	      if (Random(18)>6) then begin
	         Q[a,b].gold := Random(50)+1; Q[a,b].id := 0;
	      end else begin
	         Q[a,b].id := Random(MaxTreasure)+1; Q[a,b].gold := 0;
	      end;
		*/
	
		if (Math.random() > 0.5)
		{
			int howMany = 1 + (int) (Math.random() * 50);
			return new TreasureChest(null, howMany);
		}
		else
		{
			return new TreasureChest(Treasure.getRandom(), 0);
		}
	}	
}

