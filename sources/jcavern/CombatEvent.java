package jcavern;

import java.util.Observable;
import java.awt.*;
import jcavern.thing.Combatant;

/**
 * Class used to describe combat activities in a World.
 *
 * @author	Bill Walker
 * @version	$Id$
 */
public class CombatEvent extends WorldEvent
{
	/** * Used to indicate the kind of change that happened to a thing. */
	public static final int		ATTACKED_MISSED = 3;
	
	/** * Used to indicate the kind of change that happened to a thing. */
	public static final int		ATTACKED_HIT = 4;
	
	/** * Used to indicate the kind of change that happened to a thing. */
	public static final int		ATTACKED_KILLED = 5;
	
	/**
	 * Creates a new CombatEvent.
	 */
	private CombatEvent(Location aLocation, Combatant attackee, int outcome, Combatant attacker, int damage)
	{
		super(aLocation, attackee, outcome, attacker, "");
		
		StringBuffer	theBuffer = new StringBuffer();
		
		theBuffer.append(attacker.getNounPhrase() + " ");
		
		switch (outcome)
		{
			case ATTACKED_MISSED:
					theBuffer.append("cannot attack" + " " + attackee.getNounPhrase()); break;
			case ATTACKED_HIT:
					theBuffer.append(attackee.getHitVerb() + " " + attackee.getNounPhrase() + " for " + damage); break;
			case ATTACKED_KILLED:
					theBuffer.append(attackee.getKilledVerb() + " " + attackee.getNounPhrase()); break;
			default:
					theBuffer.append("no such outcome " + outcome);
		}
		
		setMessage(theBuffer.toString());
	}
	
	/**
	 * Creates a CombatEvent for a missed attack.
	 */
	public static CombatEvent missed(Location aLocation, Combatant attackee, Combatant attacker)
	{
		return new CombatEvent(aLocation, attackee, ATTACKED_MISSED, attacker, 0);
	}
	
	/**
	 * Creates a CombatEvent for a successful hit.
	 */
	public static CombatEvent hit(Location aLocation, Combatant attackee, Combatant attacker, int damage)
	{
		return new CombatEvent(aLocation, attackee, ATTACKED_HIT, attacker, damage);
	}

	/**
	 * Creates a CombatEvent for a successful kill.
	 */
	public static CombatEvent killed(Location aLocation, Combatant attackee, Combatant attacker, int damage)
	{
		return new CombatEvent(aLocation, attackee, ATTACKED_KILLED, attacker, damage);
	}
	
	

}