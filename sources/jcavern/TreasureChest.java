/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

public class TreasureChest extends Thing
{
	public TreasureChest() { super("Treasure Chest"); }
	
	public Object clone()
	{
		return new TreasureChest();
	}
	
	public String getAppearance() { return "$$"; }
}

