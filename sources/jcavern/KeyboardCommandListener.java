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
							case 'c' : mWorld.move(mPlayer, parseDirectionKey(e)); break;
							case 's' : mCurrentMode = SWORD_MODE; JCavernApplet.log("Sword attack, direction?"); break;
							case 'b' : mCurrentMode = RANGED_ATTACK_MODE; JCavernApplet.log("Ranged attack, direction?"); break;
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
							case 'c' : mWorld.attack(mPlayer, parseDirectionKey(e)); mCurrentMode = NORMAL_MODE; break;
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
							case 'c' : mWorld.rangedAttack(mPlayer, parseDirectionKey(e)); mCurrentMode = NORMAL_MODE; break;
						} break;
			}
		}
		catch(ThingCollisionException tce)
		{
			JCavernApplet.log("Collided with " + tce.getMovee()); 
			mPlayer.collideWithTree();
		}
		catch(IllegalLocationException ile)
		{
			JCavernApplet.log("Tried to move off the edge of the world"); 
		}
		catch(NoSuchThingException nste)
		{
			JCavernApplet.log("Nothing to attack!");
			mCurrentMode = NORMAL_MODE;
		}
		
		// and now, the monsters get a turn
		if (mCurrentMode == NORMAL_MODE)
		{
			mWorld.doTurn();
		}
	}
}