/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

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
			
			if (mPlayer.isDead())
			{
				JCavernApplet.current().log("Sorry, " + mPlayer.getName() + ", your game is over.");
			}
		}
		catch(JCavernInternalError jcie)
		{
			System.out.println("internal error " + jcie);
		}
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
			case 'c' : doMove(parseDirectionKey(e)); break;
			case 's' : mCurrentMode = SWORD_MODE; JCavernApplet.current().log(mPlayer.getSword().getName() + " attack, direction?"); break;
			case 'b' : mCurrentMode = RANGED_ATTACK_MODE; JCavernApplet.current().log("Ranged attack, direction?"); break;
			case 'v' : if (mPlayer.getCastle() != null)
						{
							mCurrentMode = CASTLE_MODE; JCavernApplet.current().log("Visiting Castle, command?");
						}
						else
						{
							JCavernApplet.current().log("No castle to visit");
						}
						break;
			case '.' : JCavernApplet.current().log("Sit"); break;
			case 'o' : doOpen(); break;
			case 'u' : mCurrentMode = USE_MODE; JCavernApplet.current().log("Start using which item?"); break;
			case 'U' : mCurrentMode = UNUSE_MODE; JCavernApplet.current().log("Stop using which item?"); break;
			default  : JCavernApplet.current().log("Unknown command");
		}
	}

	private void keyTypedCastleMode(KeyEvent e) throws JCavernInternalError
	{
		switch (e.getKeyChar())
		{
			case 'q' : doEndMission(); break;
			default  : JCavernApplet.current().log("Unknown castle visit command");
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
			case 'c' : doAttack(parseDirectionKey(e)); break;
			default  : JCavernApplet.current().log("Unknown attack direction");
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
			default  : JCavernApplet.current().log("Unknown attack direction");
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
			JCavernApplet.current().log("There's no item " + anIndex + " to use!");
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
			JCavernApplet.current().log("There's no item " + anIndex + " to stop using!");
		}
	}
	
	private void doEndMission() throws JCavernInternalError
	{
		if (mPlayer.getMission().getCompleted())
		{
			JCavernApplet.current().log("Congratulations, " + mPlayer.getName() + ".");
			
			// let's do it again!
			mPlayer.setMission(MonsterFactory.createMission(mPlayer));
			mMissionView.setModel(mPlayer.getMission());
	
			// Create a world  and a view of the world
			mWorld.populateFor(mPlayer);
		}
		else
		{
			JCavernApplet.current().log("Sorry, " + mPlayer.getName() + ", you have not completed your mission");
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
				JCavernApplet.current().log(mPlayer.getName() + " can't attack that!");
			}
			catch(IllegalLocationException ile)
			{
				JCavernApplet.current().log(mPlayer.getName() + " shot arrow of the edge of the world!"); 
			}
		}
		else
		{
			JCavernApplet.current().log(mPlayer.getName() + " has no more arrows!"); 
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
			JCavernApplet.current().log(mPlayer.getName() + " can't attack off the edge of the world!");
		}
		catch(EmptyLocationException nce)
		{
			JCavernApplet.current().log(mPlayer.getName() + " has nothing to attack!");
		}
		catch(NonCombatantException nce)
		{
			JCavernApplet.current().log(mPlayer.getName() + " can't attack that!");
		}

		mCurrentMode = NORMAL_MODE;
	}
	
	private void doOpen() throws JCavernInternalError
	{
		TreasureChest aChest = (TreasureChest) mWorld.getNeighboring(mWorld.getLocation(mPlayer), new TreasureChest(null, 0));

		mWorld.remove(aChest);
		JCavernApplet.current().log(mPlayer.getName() + " found " + aChest);
		
		if (aChest.getGold() > 0)
		{
			mPlayer.receiveGold(aChest.getGold());
		}
		
		if (aChest.getContents() != null)
		{
			mPlayer.receiveItem(aChest.getContents());
		}
	}
	
	private void doMove(int direction) throws JCavernInternalError
	{
		try
		{
			Location oldLocation = mWorld.getLocation(mPlayer);
			
			mWorld.move(mPlayer, direction);
			
			if (mPlayer.getCastle() != null)
			{
				mWorld.place(oldLocation, mPlayer.getCastle());
				mPlayer.setCastle(null);
			}
		}
		catch (ThingCollisionException tce)
		{
			if (tce.getMovee() instanceof Castle)
			{
				Castle	theCastle = (Castle) tce.getMovee();
				
				mWorld.remove(theCastle);
				doMove(direction);
				mPlayer.setCastle(theCastle);
				JCavernApplet.current().log(mPlayer.getName() + " entered " + tce.getMovee().getName());
			}
			else
			{
				JCavernApplet.current().log(mPlayer.getName() + " collided with " + tce.getMovee().getName());
			}
		}
		catch (IllegalLocationException tce)
		{
			JCavernApplet.current().log(mPlayer.getName() + " can't move off the edge of the world!");
		}
	}
}
