/* 
	KeyboardCommandListener.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

import jcavern.ui.*;
import java.awt.event.*;
import java.awt.*;
import java.util.Enumeration;

/**
 * KeyboardCommandListener receives keypress events, tracks input modes, and causes the Player
 * to do the appropriate actions.
 *
 * @author	Bill Walker
 * @version	$Id$
 */
public class KeyboardCommandListener extends KeyAdapter
{
	/** * A model of the game world */
	private World				mWorld;
	
	private WorldView			mWorldView;
	
	private MissionView			mMissionView;
	
	/** * The representation of the player */
	private Player				mPlayer;
	
	private int					mCurrentMode;
	
	/** * In normal command mode (movement, etc) */
	private static final int	NORMAL_MODE = 1;
	
	/** * In sword attack mode */
	private static final int	SWORD_MODE = 2;
	
	/** * In ranged attack mode */
	private static final int	RANGED_ATTACK_MODE = 3;
	
	/** * In castle visiting mode */
	private static final int	CASTLE_MODE = 4;
	
	/** * In start using mode */
	private static final int	USE_MODE = 5;
	
	/** * In start using mode */
	private static final int	UNUSE_MODE = 6;


	public KeyboardCommandListener(World aWorld, WorldView aWorldView, Player aPlayer, MissionView aMissionView)
	{
		mWorld = aWorld;
		mWorldView = aWorldView;
		mPlayer = aPlayer;
		mMissionView = aMissionView;
		mCurrentMode = NORMAL_MODE;
	}
	
	private int parseDirectionKey(KeyEvent e)
	{
	    switch (e.getKeyChar())
		{
			case 'q' : return Location.NORTHWEST;
			case 'w' : return Location.NORTH;
			case 'e' : return Location.NORTHEAST;
			case 'a' : return Location.WEST;
			case 'd' : return Location.EAST;
			case 'z' : return Location.SOUTHWEST;
			case 'x' : return Location.SOUTH;
			case 'c' : return Location.SOUTHEAST;
		}
		
		throw new IllegalArgumentException("not a movement key!");
	}

	/**
	 * Handles keyboard commands.
	 */
	public void keyTyped(KeyEvent e)
	{
		mWorld.eventHappened(new WorldEvent(mPlayer, WorldEvent.TURN_START));
		
	    try
		{
			switch (mCurrentMode)
			{
				case NORMAL_MODE:			keyTypedNormalMode(e); break;
				case CASTLE_MODE:			keyTypedCastleMode(e); mCurrentMode = NORMAL_MODE; break;
				case SWORD_MODE:			keyTypedSwordMode(e); mCurrentMode = NORMAL_MODE; break;
				case RANGED_ATTACK_MODE:	keyTypedRangedAttackMode(e); mCurrentMode = NORMAL_MODE; break;
				case USE_MODE:				keyTypedUseMode(e); mCurrentMode = NORMAL_MODE; break;
				case UNUSE_MODE:			keyTypedUnuseMode(e); mCurrentMode = NORMAL_MODE; break;
			}
	
			// and now, the monsters get a turn
			if (mCurrentMode == NORMAL_MODE)
			{
				mWorld.doTurn();
			}
		}
		catch(JCavernInternalError jcie)
		{
			System.out.println("internal error " + jcie);
		}

		mWorld.eventHappened(new WorldEvent(mPlayer, WorldEvent.TURN_STOP));
	}

	private void keyTypedNormalMode(KeyEvent e) throws JCavernInternalError
	{
		switch (e.getKeyChar())
		{
			// movement commands
			case 'q' :
			case 'w' :
			case 'e' :
			case 'a' :
			case 'd' :
			case 'z' :
			case 'x' :
			case 'c' :
				doMove(parseDirectionKey(e));
				break;
			case 's' :
				mCurrentMode = SWORD_MODE;
				mWorld.eventHappened(new WorldEvent(mPlayer, WorldEvent.INFO_MESSAGE, "Normal attack, direction?"));
				break;
			case 'b' :
				mCurrentMode = RANGED_ATTACK_MODE;
				mWorld.eventHappened(new WorldEvent(mPlayer, WorldEvent.INFO_MESSAGE, "Ranged attack, direction?"));
				break;
			case 'v' :
				if (mPlayer.getCastle() != null)
				{
					mCurrentMode = CASTLE_MODE;
					mWorld.eventHappened(new WorldEvent(mPlayer, WorldEvent.INFO_MESSAGE, "Visiting Castle, command?"));
				}
				else
				{
					mWorld.eventHappened(new WorldEvent(mPlayer, WorldEvent.INFO_MESSAGE, "No castle to visit"));
				}
				break;
			case '.' :
				mWorld.eventHappened(new WorldEvent(mPlayer, WorldEvent.INFO_MESSAGE, "Sit"));
				break;
			case 'o' :
				doOpen();
				break;
			case 'u' :
				mCurrentMode = USE_MODE;
				mWorld.eventHappened(new WorldEvent(mPlayer, WorldEvent.INFO_MESSAGE, "Start using which item?"));
				break;
			case 'U' :
				mCurrentMode = UNUSE_MODE;
				mWorld.eventHappened(new WorldEvent(mPlayer, WorldEvent.INFO_MESSAGE, "Stop using which item?"));
				break;
			default  :
				mWorld.eventHappened(new WorldEvent(mPlayer, WorldEvent.ERROR_MESSAGE, "Unknown command"));
		}
	}

	private void keyTypedCastleMode(KeyEvent e) throws JCavernInternalError
	{
		switch (e.getKeyChar())
		{
			case 'q' :
				doEndMission();
				break;
			case 'a' :
				doBuyArrows(1);
				break;
			case 'A' :
				doBuyArrows(10);
				break;
			default  :
				mWorld.eventHappened(new WorldEvent(mPlayer, WorldEvent.INFO_MESSAGE, "Unknown castle visit command"));
				mWorld.eventHappened(new WorldEvent(mPlayer, WorldEvent.INFO_MESSAGE, "q = quit mission, a = buy an arrow, A = buy 10 arrows"));
		}
	}
	
	private void keyTypedSwordMode(KeyEvent e) throws JCavernInternalError
	{
		switch (e.getKeyChar())
		{
			// direction keys
			case 'q' :
			case 'w' :
			case 'e' :
			case 'a' :
			case 'd' :
			case 'z' :
			case 'x' :
			case 'c' :
				doAttack(parseDirectionKey(e));
				break;
			default  :
				mWorld.eventHappened(new WorldEvent(mPlayer, WorldEvent.INFO_MESSAGE, "Unknown attack direction"));
		}
	}
	
	private void keyTypedRangedAttackMode(KeyEvent e) throws JCavernInternalError
	{
		switch (e.getKeyChar())
		{
			// direction keys
			case 'q' :
			case 'w' :
			case 'e' :
			case 'a' :
			case 'd' :
			case 'z' :
			case 'x' :
			case 'c' : doRangedAttack(parseDirectionKey(e)); break;
			default  : mWorld.eventHappened(new WorldEvent(mPlayer, WorldEvent.INFO_MESSAGE, "Unknown attack direction"));
		}
	}
	
	private void keyTypedUseMode(KeyEvent e) throws JCavernInternalError
	{
		switch (e.getKeyChar())
		{
			case '0' :
			case '1' :
			case '2' :
			case '3' :
			case '4' :
			case '5' :
			case '6' :
			case '7' :
			case '8' :
			case '9' : doUse(Character.getNumericValue(e.getKeyChar()));
		}
 	}
	
	private void keyTypedUnuseMode(KeyEvent e) throws JCavernInternalError
	{
		switch (e.getKeyChar())
		{
			case '0' :
			case '1' :
			case '2' :
			case '3' :
			case '4' :
			case '5' :
			case '6' :
			case '7' :
			case '8' :
			case '9' : doUnuse(Character.getNumericValue(e.getKeyChar()));
		}
 	}
	
	//
	// ------------ methods for controlling the player
	//
		
	private void doUse(int anIndex) throws JCavernInternalError
	{
		try
		{
			mPlayer.getUnusedTreasureAt(anIndex).startUseBy(mPlayer, mWorld);
		}
		catch (IllegalArgumentException iae)
		{
			mWorld.eventHappened(new WorldEvent(mPlayer, WorldEvent.INFO_MESSAGE, "There's no item " + anIndex + " to use!"));
		}
	}

	private void doUnuse(int anIndex)
	{
		try
		{
			mPlayer.getInUseTreasureAt(anIndex).stopUseBy(mPlayer, mWorld);
		}
		catch (IllegalArgumentException iae)
		{
			mWorld.eventHappened(new WorldEvent(mPlayer, WorldEvent.INFO_MESSAGE, "There's no item " + anIndex + " to stop using!"));
		}
	}
	
	private void doEndMission() throws JCavernInternalError
	{
		if (mPlayer.getMission().getCompleted())
		{
			MissionCard.endMissionAlert("Congratulations, " + mPlayer.getName() + ".");
			JCavernApplet.setPlayer(mPlayer);
		}
		else
		{
			mWorld.eventHappened(new WorldEvent(mPlayer, WorldEvent.INFO_MESSAGE, "Sorry, " + mPlayer.getName() + ", you have not completed your mission"));
		}		
	}
	
	private void doRangedAttack(int direction) throws JCavernInternalError
	{
		if (mPlayer.getArrows() > 0)
		{
			try
			{
				mPlayer.rangedAttack(mWorld, direction);
			}
			catch(NonCombatantException nce)
			{
				mWorld.eventHappened(new WorldEvent(mPlayer, WorldEvent.INFO_MESSAGE, mPlayer.getName() + " can't attack that!"));
			}
			catch(IllegalLocationException ile)
			{
				mWorld.eventHappened(new WorldEvent(mPlayer, WorldEvent.INFO_MESSAGE, mPlayer.getName() + " shot arrow of the edge of the world!")); 
			}
		}
		else
		{
			mWorld.eventHappened(new WorldEvent(mPlayer, WorldEvent.INFO_MESSAGE, mPlayer.getName() + " has no more arrows!")); 
		}
				
		mCurrentMode = NORMAL_MODE;
	}	

	private void doAttack(int direction) throws JCavernInternalError
	{
		try
		{
			mPlayer.attack(mWorld, direction);
		}
		catch(IllegalLocationException nce)
		{
			mWorld.eventHappened(new WorldEvent(mPlayer, WorldEvent.INFO_MESSAGE, mPlayer.getName() + " can't attack off the edge of the world!"));
		}
		catch(EmptyLocationException nce)
		{
			mWorld.eventHappened(new WorldEvent(mPlayer, WorldEvent.INFO_MESSAGE, mPlayer.getName() + " has nothing to attack!"));
		}
		catch(NonCombatantException nce)
		{
			mWorld.eventHappened(new WorldEvent(mPlayer, WorldEvent.INFO_MESSAGE, mPlayer.getName() + " can't attack that!"));
		}

		mCurrentMode = NORMAL_MODE;
	}
	
	private void doOpen() throws JCavernInternalError
	{
		try
		{
			TreasureChest aChest = (TreasureChest) mWorld.getNeighboring(mWorld.getLocation(mPlayer), new TreasureChest(null, 0));
	
			mWorld.remove(aChest);
			mWorld.eventHappened(new WorldEvent(mPlayer, WorldEvent.INFO_MESSAGE, mPlayer.getName() + " found " + aChest));
			
			if (aChest.getGold() > 0)
			{
				mPlayer.receiveGold(aChest.getGold());
			}
			
			if (aChest.getContents() != null)
			{
				mPlayer.receiveItem(aChest.getContents());
			}
		}
		catch(IllegalArgumentException iae)
		{
			mWorld.eventHappened(new WorldEvent(mPlayer, WorldEvent.INFO_MESSAGE, mPlayer.getName() + " has no neighboring treasure chest to open!"));
		}
	}
	
	private void doBuyArrows(int numberOfArrows) throws JCavernInternalError
	{
		if (mPlayer.getGold() < numberOfArrows)
		{
				mWorld.eventHappened(new WorldEvent(mPlayer, WorldEvent.INFO_MESSAGE, mPlayer.getName() + " doesn't have " + numberOfArrows + " gold to buy arrows"));
		}
		else
		{
			mPlayer.spendGold(numberOfArrows);
			mPlayer.receiveArrows(numberOfArrows);
		}
	}
	
	private void doMove(int direction) throws JCavernInternalError
	{
		try
		{
			Location oldLocation = mWorld.getLocation(mPlayer);
			mWorld.move(mPlayer, direction);
		}
		catch (ThingCollisionException tce)
		{
			if (tce.getMovee() instanceof Castle)
			{
				Castle	theCastle = (Castle) tce.getMovee();
				
				mWorld.remove(theCastle);
				doMove(direction);
				mPlayer.setCastle(theCastle);
				mWorld.eventHappened(new WorldEvent(mPlayer, WorldEvent.INFO_MESSAGE, mPlayer.getName() + " entered " + tce.getMovee().getName()));
			}
			else
			{
				mWorld.eventHappened(new WorldEvent(mPlayer, WorldEvent.INFO_MESSAGE, mPlayer.getName() + " collided with " + tce.getMovee().getName()));
			}
		}
		catch (IllegalLocationException tce)
		{
			mWorld.eventHappened(new WorldEvent(mPlayer, WorldEvent.INFO_MESSAGE, mPlayer.getName() + " can't move off the edge of the world!"));
		}
	}
}
