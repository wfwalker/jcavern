/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

public class Player extends Thing
{
	private int			mGold;
	private Sword		mSword;
	private int			mArrows;
	private int			mExperience;
	private Mission		mMission;
	
	public Player(String name)
	{
		super(name);
		
		mGold = 0;
		mSword = null;
		mArrows = 10;
		mExperience = 10;
		mMission = new Mission();
	}
	
	public String getAppearance()
	{
		return "P";
	}
	
	public int getGold()
	{
		return mGold;
	}
	
	public Sword getSword()
	{
		return mSword;
	}
	
	public int getArrows()
	{
		return mArrows;
	}
	
	public int getExperience()
	{
		return mExperience;
	}

	public void collideWithTree()
	{
		mExperience--;
		
		setChanged();
		notifyObservers();
	}
}
