/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

public class TreasureChest extends Thing
{
	private Treasure	mContents;
	private int			mGoldPieces;
	
	public TreasureChest(Treasure aTreasure, int howManyGold)
	{
		super("Treasure Chest");
		
		mContents = aTreasure;
		mGoldPieces = howManyGold;
	}
	
	public Treasure getContents()
	{
		return mContents;
	}
	
	public int getGold()
	{
		return mGoldPieces;
	}
	
	public Object clone()
	{
		return new TreasureChest(mContents, mGoldPieces);
	}
	
	public String getAppearance() { return "$$"; }
	
	public String toString()
	{
		return "TreasureChest with " + mGoldPieces + " gold pieces and " + mContents;
	}

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

