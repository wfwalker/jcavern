/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

import java.io.*;

import jcavern.ui.*;
import jcavern.thing.*;
import java.util.Observable;

/**
 * A Mission represents the set of Monsters to be defeated
 * in order to complete a particular level.
 *
 * @author	Bill Walker
 * @version	$Id$
 */
public class Mission extends Observable implements Serializable
{
	/** * What kind of monster must be defeated? */
	private Monster		mTarget;
	
	/** * How many of these monsters must be defeated? */
	private int			mQuota;
	
	/** * How many of these monsters have been defeated? */
	private int			mKills;
	
	/**
	 * Returns the current quota of monsters to kill.
	 *
	 * @return		the current quota of monsters to kill.
	 */
	public int getQuota()
	{
		return mQuota;
	}
	
	/**
	 * Returns the target of this mission
	 *
	 * @return		the target Monster for this mission
	 */
	public Monster getTarget()
	{
		return mTarget;
	}
	
	/**
	 * Returns the number of quest monsters already killed
	 *
	 * @return		the number of monsters already killed
	 */
	public int getKills()
	{
		return mKills;
	}
	
	/**
	 * Returns whether the mission has been completed
	 *
	 * @return		<CODE>true</CODE> if the mission has been completed, <CODE>false</CODE> otherwise
	 */
	public boolean getCompleted()
	{
		return mKills >= mQuota;
	}
	
	/**
	 * Adjusts the quota for this mission to reflect the Player's latest kill.
	 * The quota is only adjusted if the dead monster is the target of the this mission.
	 *
	 * @param	aWorld		the non-null world in which the mission takes place
	 * @param	aPlayer		the non-null Player performing the mission
	 * @param	theVictim	the non-null Monster just killed by the Player
	 */
	public void adjustQuota(World aWorld, Player aPlayer, Thing theVictim)
	{
		if (theVictim.getName().equals(mTarget.getName()))
		{
			mKills++;
			aWorld.eventHappened(new WorldEvent(aPlayer, WorldEvent.INFO_MESSAGE, "Your mission is now " + this));
			
			setChanged();
			notifyObservers();		
		}
	}
	
	/**
	 * Returns a string-based representation of the mission.
	 *
	 * @return		a string-based representation of the mission.
	 */
	public String toString()
	{
		if (mKills < mQuota)
		{
			return "to kill " + (mQuota - mKills) + " of " + mQuota + " " + mTarget.getName() + "s";
		}
		else
		{
			return "completed";
		}
	}
	
	/**
	 * Creates a new mission
	 *
	 * @param	target	the target monster for this mission
	 * @param	quota	how many of the target monsters should be killed
	 */
	public Mission(Monster target, int quota)
	{
		mTarget = target;
		mQuota = quota;
		mKills = 0;
	}
}
