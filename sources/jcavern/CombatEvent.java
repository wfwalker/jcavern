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
	public static final int		ATTACKED_MISSED = 103;
	
	/** * Used to indicate the kind of change that happened to a thing. */
	public static final int		ATTACKED_HIT = 104;
	
	/** * Used to indicate the kind of change that happened to a thing. */
	public static final int		ATTACKED_KILLED = 105;
	
	/** * Used to indicate the kind of change that happened to a thing. */
	public static final int		GAINED_POINTS = 106;
	
	/** * Used to indicate the kind of change that happened to a thing. */
	public static final int		LOST_POINTS = 107;
	
	/**
	 * Creates a new CombatEvent.
	 *
	 * @param	aLocation	where the combat took place
	 * @param	attackee	who attacked
	 * @param	outcome		what was the outcome (one of the symbolic constants defined in CombatEvent)
	 * @param	attacker	who was damaged
	 * @param	damage		how much damage was done
	 */
	private CombatEvent(Location aLocation, Combatant attackee, int outcome, Combatant attacker, int damage)
	{
		super(aLocation, attackee, outcome, attacker, "");
		
		StringBuffer	theBuffer = new StringBuffer();
		
		
		switch (outcome)
		{
			case ATTACKED_MISSED:
					theBuffer.append(attacker.getNounPhrase() + " ");
					theBuffer.append("cannot attack" + " " + attackee.getNounPhrase()); break;
			case ATTACKED_HIT:
					theBuffer.append(attacker.getNounPhrase() + " ");
					theBuffer.append(attackee.getHitVerb() + " " + attackee.getNounPhrase() + " for " + damage); break;
			case ATTACKED_KILLED:
					theBuffer.append(attacker.getNounPhrase() + " ");
					theBuffer.append(attackee.getKilledVerb() + " " + attackee.getNounPhrase()); break;
			case GAINED_POINTS:
					theBuffer.append(attackee.getNounPhrase());
					theBuffer.append(" gained " + damage + " points"); break;
			case LOST_POINTS:
					theBuffer.append(attackee.getNounPhrase());
					theBuffer.append(" lost " + damage + " points"); break;
			default:
					theBuffer.append("no such outcome " + outcome);
		}
		
		setMessage(theBuffer.toString());
	}
	
	/**
	 * Creates a CombatEvent for a missed attack.
	 *
	 * @param	aLocation	where the combat took place
	 * @param	attackee	who attacked
	 * @param	attacker	who was damaged
	 * @return				a non-null CombatEvent
	 */
	public static CombatEvent missed(Location aLocation, Combatant attackee, Combatant attacker)
	{
		return new CombatEvent(aLocation, attackee, ATTACKED_MISSED, attacker, 0);
	}
	
	/**
	 * Creates a CombatEvent for a successful hit.
	 *
	 * @param	aLocation	where the combat took place
	 * @param	attackee	who attacked
	 * @param	attacker	who was damaged
	 * @param	damage		how much damage was done
	 * @return				a non-null CombatEvent
	 */
	public static CombatEvent hit(Location aLocation, Combatant attackee, Combatant attacker, int damage)
	{
		return new CombatEvent(aLocation, attackee, ATTACKED_HIT, attacker, damage);
	}

	/**
	 * Creates a CombatEvent for a successful kill.
	 *
	 * @param	aLocation	where the combat took place
	 * @param	attackee	who attacked
	 * @param	attacker	who was damaged
	 * @param	damage		how much damage was done
	 * @return				a non-null CombatEvent
	 */
	public static CombatEvent killed(Location aLocation, Combatant attackee, Combatant attacker, int damage)
	{
		return new CombatEvent(aLocation, attackee, ATTACKED_KILLED, attacker, damage);
	}
	
	/**
	 * Creates a CombatEvent for gained points.
	 *
	 * @param	aLocation	where the combat took place
	 * @param	gainee		who gained points
	 * @param	gain		how many points were gained
	 * @return				a non-null CombatEvent
	 */
	public static CombatEvent gained(Location aLocation, Combatant gainee, int gain)
	{
		return new CombatEvent(aLocation, gainee, GAINED_POINTS, null, gain);
	}
	
	/**
	 * Creates a CombatEvent for lost points.
	 *
	 * @param	aLocation	where the combat took place
	 * @param	lossee		who lost points
	 * @param	loss		how many points were lost
	 * @return				a non-null CombatEvent
	 */
	public static CombatEvent lost(Location aLocation, Combatant lossee, int loss)
	{
		return new CombatEvent(aLocation, lossee, LOST_POINTS, null, loss);
	}
}
