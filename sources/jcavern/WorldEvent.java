package jcavern;

import java.util.Observable;
import java.awt.*;
import jcavern.thing.*;

/**
 * Class used to describe occurences in a World.
 *
 * @author	Bill Walker
 * @version	$Id$
 */
public class WorldEvent
{
	/** * Used to indicate the kind of change that happened to a thing. */
	public static final int		ADDED_INVENTORY = 7;
	
	/** * Used to indicate the kind of change that happened to a thing. */
	public static final int		USE_INVENTORY = 8;
	
	/** * Used to indicate the kind of change that happened to a thing. */
	public static final int		COMMAND_PROMPT = 9;
	
	/** * Used to indicate the kind of change that happened to a thing. */
	public static final int		ERROR_MESSAGE = 10;
	
	/** * Used to indicate the kind of change that happened to a thing. */
	public static final int		INFO_MESSAGE = 11;
	
	/** * Used to indicate the kind of change that happened to a thing. */
	public static final int		TURN_START = 12;
	
	/** * Used to indicate the kind of change that happened to a thing. */
	public static final int		TURN_STOP = 13;
	
	/** * Used to indicate the kind of change that happened to a thing. */
	public static final int		RANGED_ATTACK = 14;
		
	/** * Used to indicate that the mission ended. */
	public static final int		MISSION_END = 15;

	/** * One of the codes above. */
	private int					mEventCode;
	
	/** * The Thing to which the event occurred */
	private Thing				mSubject;
	
	/** * The Thing that caused the subject to experience the event, or <CODE>null</CODE> if none. */
	private Thing				mCause;
	
	/** * The Location where the event occurred */
	private Location			mLocation;
	
	/** * A message describing what happened. */
	private String				mMessage;
	
	/**
	 * Creates a new WorldEvent.
	 * The event code field is mandatory; the others are optional.
	 *
	 * @param	aLocation	where the event took place
	 * @param	subject		which Thing the event is focussed on (i. e., who died)
	 * @param	code		an event code (as defined in this class)
	 * @param	cause		which Thing caused this event (i. e., who killed the subject)
	 * @param	message		a description of this event
	 */
	public WorldEvent(Location aLocation, Thing subject, int code, Thing cause, String message)
	{
		mLocation = aLocation;
		mSubject = subject;
		mEventCode = code;
		mCause = cause;
		mMessage = message;
	}
	
	/**
	 * Creates a new WorldEvent when the Location isn't known
	 *
	 * @param	subject		which Thing the event is focussed on (i. e., who died)
	 * @param	code		an event code (as defined in this class)
	 * @param	cause		which Thing caused this event (i. e., who killed the subject)
	 * @param	message		a description of this event
	 */
	public WorldEvent(Thing subject, int code, Thing cause, String message)
	{
		this(null, subject, code, cause, message);
	}
	
	/**
	 * Creates a new WorldEvent when only the Location is known
	 *
	 * @param	aLocation	where the event took place
	 * @param	code		an event code (as defined in this class)
	 */
	public WorldEvent(Location aLocation, int code)
	{
		this(aLocation, null, code, null, null);
	}

	/**
	 * Creates a new WorldEvent when only the subject is known.
	 *
	 * @param	subject		which Thing the event is focussed on (i. e., who died)
	 * @param	code		an event code (as defined in this class)
	 */
	public WorldEvent(Thing subject, int code)
	{
		this(null, subject, code, null, null);
	}
	
	/**
	 * Creates a new WorldEvent when only the subject and a message are known.
	 *
	 * @param	subject		which Thing the event is focussed on (i. e., who died)
	 * @param	code		an event code (as defined in this class)
	 * @param	message		a description of this event
	 */
	public WorldEvent(Thing subject, int code, String message)
	{
		this(null, subject, code, null, message);
	}
	
	/**
	 * Creates a new WorldEvent when only the subject and cause are known
	 *
	 * @param	subject		which Thing the event is focussed on (i. e., who died)
	 * @param	code		an event code (as defined in this class)
	 * @param	cause		which Thing caused this event (i. e., who killed the subject)
	 */
	public WorldEvent(Thing subject, int code, Thing cause)
	{
		this(null, subject, code, cause, null);
	}
	
	/**
	 * Returns a string-based representation of the event.
	 *
	 * @return		a string-based representation of the event.
	 */
	public String toString()
	{
		if (mMessage != null)
		{
			return mMessage;
		}
		else
		{
			StringBuffer	theEventString = new StringBuffer();
			
			theEventString.append(mSubject.getName());
			theEventString.append(" ");
			
			switch (mEventCode)
			{
				case WorldContentsEvent.PLACED:
						theEventString.append(" was placed "); break;
				case WorldContentsEvent.REMOVED:
						theEventString.append(" was removed "); break;
				case CombatEvent.ATTACKED_MISSED:
						theEventString.append(" was missed "); break;
				case CombatEvent.ATTACKED_HIT:
						theEventString.append(" was hit "); break;
				case CombatEvent.ATTACKED_KILLED:
						theEventString.append(" was killed "); break;
				case WorldContentsEvent.REVEALED:
						theEventString.append(" was revealed "); break;
				case ADDED_INVENTORY:
						theEventString.append(" picked up "); break;
				case TURN_START:
						theEventString.append(" turn start "); break;
				case TURN_STOP:
						theEventString.append(" turn stop "); break;
				case MISSION_END:
						theEventString.append(" mission end "); break;
				default:
						theEventString.append("no such event code " + mEventCode);
			}
			
			if (mCause != null)
			{
				theEventString.append("by ");
				theEventString.append(mCause.getName());
			}
			
			return theEventString.toString();
		}
	}
	
	/**
	 * Sets the message.
	 *
	 * @param	aString		the non-null String message
	 */
	public void setMessage(String aString)
	{
		mMessage = aString;
	}
	
	/**
	 * Sets the Location
	 *
	 * @param	aLocation		the non-null Location
	 */
	public void setLocation(Location aLocation)
	{
		mLocation = aLocation;
	}
	
	/**
	 * Returns the Location for this event
	 *
	 * @return	the Location for this event
	 */
	public Location getLocation()
	{
		return mLocation;
	}
	
	/**
	 * Returns the message for this event
	 *
	 * @return	the message for this event
	 */
	public String getMessage()
	{
		return mMessage;
	}
	
	/**
	 * Returns the subject for this event
	 *
	 * @return	the subject for this event
	 */
	public Thing getSubject()
	{
		return mSubject;
	}
	
	/**
	 * Returns the cause for this event
	 *
	 * @return	the cause for this event
	 */
	public Thing getCause()
	{
		return mCause;
	}
	
	/**
	 * Returns the event code for this event
	 *
	 * @return	the event code for this event
	 */
	public int getEventCode()
	{
		return mEventCode;
	}
	
	/**
	 * Creates a WorldEvent for end of mission.
	 *
	 * @param	inLocation	where the mission ended
	 * @param	inPlayer	whose mission ended
	 * @param	inMessage	a description of why the mission ended
	 * @return				a non-null WorldEvent
	 */
	public static WorldEvent missionEnd(Location inLocation, Player inPlayer, String inMessage)
	{
		return new WorldEvent(inLocation, inPlayer, MISSION_END, null, inMessage);
	}
}
