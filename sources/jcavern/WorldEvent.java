package jcavern;

import java.util.Observable;
import java.awt.*;

/**
 * Class used to describe occurences in a World.
 *
 * @author	Bill Walker
 * @version	$Id$
 */
public class WorldEvent
{
	/** * Used to indicate the kind of change that happened to a thing. */
	public static final int		PLACED = 1;
	
	/** * Used to indicate the kind of change that happened to a thing. */
	public static final int		REMOVED = 2;
	
	/** * Used to indicate the kind of change that happened to a thing. */
	public static final int		ATTACKED_MISSED = 3;
	
	/** * Used to indicate the kind of change that happened to a thing. */
	public static final int		ATTACKED_HIT = 4;
	
	/** * Used to indicate the kind of change that happened to a thing. */
	public static final int		ATTACKED_KILLED = 5;
	
	/** * Used to indicate the kind of change that happened to a thing. */
	public static final int		REVEALED = 6;
	
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
	
	/** * One of the codes above. */
	private int					mEventCode;
	
	/** * The Thing to which the event occurred */
	private Thing				mSubject;
	
	/** * The Thing that caused the subject to experience the event, or <CODE>null</CODE> if none. */
	private Thing				mCause;
	
	/** * A message describing what happened. */
	private String				mMessage;
	
	/**
	 * Creates a new WorldEvent.
	 */
	public WorldEvent(Thing subject, int code, Thing cause, String message)
	{
		mSubject = subject;
		mEventCode = code;
		mCause = cause;
		mMessage = message;
	}
	
	/**
	 * Creates a new WorldEvent.
	 */
	public WorldEvent(Thing subject, int code)
	{
		this(subject, code, null, null);
	}
	
	/**
	 * Creates a new WorldEvent.
	 */
	public WorldEvent(Thing subject, int code, String message)
	{
		this(subject, code, null, message);
	}
	
	/**
	 * Creates a new WorldEvent.
	 */
	public WorldEvent(Thing subject, int code, Thing cause)
	{
		this(subject, code, cause, null);
	}
	
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
				case PLACED:
						theEventString.append(" was placed "); break;
				case REMOVED:
						theEventString.append(" was removed "); break;
				case ATTACKED_MISSED:
						theEventString.append(" was missed "); break;
				case ATTACKED_HIT:
						theEventString.append(" was hit "); break;
				case ATTACKED_KILLED:
						theEventString.append(" was killed "); break;
				case REVEALED:
						theEventString.append(" was revealed "); break;
				case ADDED_INVENTORY:
						theEventString.append(" picked up "); break;
				case TURN_START:
						theEventString.append(" turn start "); break;
				case TURN_STOP:
						theEventString.append(" turn stop "); break;
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
	
	public Thing getSubject()
	{
		return mSubject;
	}
	
	public Thing getCause()
	{
		return mCause;
	}
	
	public int getEventCode()
	{
		return mEventCode;
	}
}