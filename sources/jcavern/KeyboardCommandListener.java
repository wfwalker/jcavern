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

public class KeyboardCommandListener extends KeyAdapter
{
	/** * A model of the game world */
	private World				mWorld;
	
	/** * The representation of the player */
	private Player				mPlayer;
	
	private int					mCurrentMode;
	
	/** * In normal command mode (movement, etc) */
	private static final int	NORMAL_MODE = 1;
	
	/** * In normal command mode (movement, etc) */
	private static final int	SWORD_MODE = 2;
	
	/** * In normal command mode (movement, etc) */
	private static final int	RANGED_ATTACK_MODE = 3;
	
	/** * In normal command mode (movement, etc) */
	private static final int	CASTLE_MODE = 4;


	public KeyboardCommandListener(World aWorld, Player aPlayer)
	{
		mWorld = aWorld;
		mPlayer = aPlayer;
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
				case NORMAL_MODE: switch (e.getKeyChar())
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
							case 's' : mCurrentMode = SWORD_MODE; JCavernApplet.log("Sword attack, direction?"); break;
							case 'b' : mCurrentMode = RANGED_ATTACK_MODE; JCavernApplet.log("Ranged attack, direction?"); break;
							case '.' : JCavernApplet.log("Sit"); break;
						} break;
				case SWORD_MODE: switch (e.getKeyChar())
						{
							// movement commands
							case 'q' :
							case 'w' :
							case 'e' :
							case 'a' :
							case 'd' :
							case 'z' :
							case 'x' :
							case 'c' : doAttack(parseDirectionKey(e)); break;
						} break;
				case RANGED_ATTACK_MODE: switch (e.getKeyChar())
						{
							// movement commands
							case 'q' :
							case 'w' :
							case 'e' :
							case 'a' :
							case 'd' :
							case 'z' :
							case 'x' :
							case 'c' : doRangedAttack(parseDirectionKey(e)); break;
						} break;
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
		
	}

	private void doRangedAttack(int direction) throws JCavernInternalError
	{
		try
		{
			mPlayer.rangedAttack(mWorld, direction);
		}
		catch(NonCombatantException nce)
		{
			JCavernApplet.log(mPlayer.getName() + " can't attack that!");
		}
		catch(IllegalLocationException ile)
		{
			JCavernApplet.log(mPlayer.getName() + " shot arrow of the edge of the world"); 
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
			JCavernApplet.log(mPlayer.getName() + " can't attack off the edge of the world!");
		}
		catch(EmptyLocationException nce)
		{
			JCavernApplet.log(mPlayer.getName() + " has nothing to attack!");
		}
		catch(NonCombatantException nce)
		{
			JCavernApplet.log(mPlayer.getName() + " can't attack that!");
		}

		mCurrentMode = NORMAL_MODE;
	}
	
	private void doMove(int direction) throws JCavernInternalError
	{
		try
		{
			mWorld.move(mPlayer, direction);
		}
		catch (ThingCollisionException tce)
		{
			System.out.println("player collided with " + tce.getMovee());
			
			if (tce.getMovee() instanceof TreasureChest)
			{
				mWorld.remove(tce.getMovee());
				doMove(direction);
				JCavernApplet.log(mPlayer.getName() + " collected " + tce.getMovee().getName());
			}
			else
			{
				JCavernApplet.log(mPlayer.getName() + " collided with " + tce.getMovee().getName());
			}
		}
		catch (IllegalLocationException tce)
		{
			JCavernApplet.log(mPlayer.getName() + " can't move off the edge of the world!");
		}
	}
}