/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern.thing;

import java.awt.*;
import jcavern.ui.*;
import jcavern.*;

/**
 * Represents a Thing that can participate in combat.
 *
 * @author	Bill Walker
 * @version	$Id$
 */
public abstract class Combatant extends Thing
{
	/** * How many points does this Combatant has now. */
	private int					mPoints;

	/** * How many points has this Combatant ever had. */
	private int					mMaximumPoints;
	
	/** * How many Combatants this Combatant has killed */
	private int					mKills;

	/**
	 * GraphicalCombatantView controls how Combatants appear in a WorldView.
	 * Combatants are highlighted when they are involved in combat.
	 * Highlighting means displaying a graphical indicator of remaining points underneath the board image.
	 */
	public class GraphicalCombatantView extends Thing.GraphicalThingView
	{
		/**
		 * Creates a new GraphicalCombatantView with the given image name.
		 *
		 * @param	inImageName		a non-null String naming the image to use with this View.
		 */
		public GraphicalCombatantView(String inImageName)
		{
			super(inImageName);
		}
		
		/**
		 * Decides whether a particular Combatant should be highlighted,
		 * in the context of a particular event.
		 *
		 * @param	anEvent		the event that could trigger highlighting
		 * @return				<CODE>true</CODE> if this combatant should be highlighted, <CODE>false</CODE> otherwise
		 */
		public boolean shouldHighlight(WorldEvent anEvent)
		{
			return
				(anEvent != null) &&
				(anEvent instanceof CombatEvent) &&
				((anEvent.getSubject() == Combatant.this) || (anEvent.getCause() == Combatant.this));
		}
	
		/**
		 * Paints the Combatant in the WorldView, with optional highlighting.
		 *
		 * @param		inApplet				the current Applet (used to retrieve images)
		 * @param		g						the non-null Graphics on which to paint
		 * @param		plotX					the x location for plotting
		 * @param		plotY					the y location for plotting
		 * @exception	JCavernInternalError	could not retrieve an image
		 */
		public void paint(JCavernApplet inApplet, Graphics g, int plotX, int plotY) throws JCavernInternalError
		{
			g.setColor(JCavernApplet.CavernOrange);
	
			final int gaugeThickness = 3;
			final int gaugeLength = 32;
			
			super.paint(inApplet, g, plotX, plotY);
			
			if (getHighlightThisTurn())
			{
				double	percent = 1.0 * getPoints() / getMaximumPoints();
				int		point = (int) (percent * gaugeLength);
				
				g.fillRect(
					plotX - gaugeLength / 2, plotY + 20, 
					point, gaugeThickness);
					
				g.drawLine(
					plotX - gaugeLength / 2 + point + 2, plotY + (20 + gaugeThickness / 2),
					plotX + gaugeLength / 2, plotY + (20 + gaugeThickness / 2));
			}
		}	
	}

	// Combatants can compute and suffer damage they cause
	
	/**
	 * Returns whether this Combatant can attack a given Combatant.
	 *
	 * @param	aCombatant	a potential opponent
	 * @return				<CODE>true</CODE> if this Combatant can attack the given opponent, <CODE>false</CODE> otherwise
	 */
	public abstract boolean canAttack(Combatant aCombatant);
	
	/**
	 * Returns whether this Combatant can make a ranged attack on a given Combatant.
	 *
	 * @param	aCombatant	a potential opponent
	 * @return				<CODE>true</CODE> if this Combatant can attack the given opponent, <CODE>false</CODE> otherwise
	 */
	public abstract boolean canRangedAttack(Combatant aCombatant);
	
	/**
	 * Returns whether this combatant is vulnerable to attack from a given Monster.
	 *
	 * @param	aMonster	a potential opponent
	 * @return				<CODE>true</CODE> if the Monster can attack this component, <CODE>false</CODE> otherwise
	 */
	public boolean vulnerableToMonsterAttack(Monster aMonster)
	{
		//System.out.println("Combatant.vulnerableToMonsterAttack(Combatant)");
		return true;
	}
	
	/**
	 * Returns whether this combatant is vulnerable to attack from a given Player.
	 *
	 * @param	aPlayer		a potential opponent
	 * @return				<CODE>true</CODE> if the Player can attack this component, <CODE>false</CODE> otherwise
	 */
	public boolean vulnerableToPlayerAttack(Player aPlayer)
	{
		//System.out.println("Combatant.vulnerableToPlayerAttack(Combatant)");
		return true;
	}
	
	/**
	 * Returns whether this combatant is vulnerable to ranged attack from a given Monster.
	 *
	 * @param	aMonster	a potential opponent
	 * @return				<CODE>true</CODE> if the Monster can ranged attack this component, <CODE>false</CODE> otherwise
	 */
	public boolean vulnerableToMonsterRangedAttack(Monster aMonster)
	{
		//System.out.println("Combatant.vulnerableToMonsterRangedAttack(Combatant)");
		return true;
	}
	
	/**
	 * Returns whether this combatant is vulnerable to ranged attack from a given Player.
	 *
	 * @param	aPlayer		a potential opponent
	 * @return				<CODE>true</CODE> if the Player can ranged attack this component, <CODE>false</CODE> otherwise
	 */
	public boolean vulnerableToPlayerRangedAttack(Player aPlayer)
	{
		//System.out.println("Combatant.vulnerableToPlayerRangedAttack(Combatant)");
		return true;
	}
	
	/**
	 * Computes the damage this combatant does to a given opponent in a sword attack.
	 *
	 * @param	opponent	a non-null Combatant on whom this Combatant will inflict damage
	 * @return				how much damage this combatant does
	 */
	public abstract int computeDamageTo(Combatant opponent);
	
	/**
	 * Computes the damage this combatant does to a given opponent in a ranged attack.
	 *
	 * @param	opponent	a non-null Combatant on whom this Combatant will inflict damage
	 * @return				how much damage this combatant does
	 */
	public abstract int computeRangedDamageTo(Combatant opponent);

	/**
	 * Causes this combatant to suffer damage.
	 *
	 * @param		inWorld					a non-null World in which the action takes place.
	 * @param		inDamage				how much damage this combatant suffers (not accounting for armour)
	 * @return								how much damage the combatant actually suffers (after accounting for armour)
	 * @exception	JCavernInternalError	trouble send notification that mission ended when Combatant died
	 */
	public int sufferDamage(World inWorld, int inDamage) throws JCavernInternalError
	{
		mPoints -= inDamage;
		return inDamage;
	}
	
	/**
	 * Decrements the number of ranged attacks this Combatant can perform.
	 * This simulates using up a supply of arrows.
	 */
	public void decrementRangedAttackCount()
	{
	
	}
	
	/**
	 * Decrements the number of attacks this Combatant can perform.
	 * This simulates wearing out a sword.
	 */
	public void decrementAttackCount()
	{
	
	}
	
	// Combatants can attack things

	/**
	 * Performs an attack on a potential opponent, if appropriate.
	 *
	 * @param		aWorld						a non-null World in which the attack occurs
	 * @param		potentialOpponent			a non-null Thing that the receiver will try to attack
	 * @exception	JCavernInternalError 		could not perform the attack
	 * @exception	NonCombatantException 		tried to attack a non-combatant (i. e., treasure chest, castle)
	 */
	public void attack(World aWorld, Thing potentialOpponent) throws JCavernInternalError, NonCombatantException
	{
		if (! (potentialOpponent instanceof Combatant))
		{
			throw new NonCombatantException(potentialOpponent + " is not a combatant!");
		}
		
		Combatant	opponent = (Combatant) potentialOpponent;
		Location	aLocation = aWorld.getLocation(potentialOpponent);

		decrementAttackCount();
		
		if (this.canAttack(opponent))
		{
			finishAttack(aWorld, aLocation, computeDamageTo(opponent), opponent);
		}
		else
		{
			aWorld.eventHappened(CombatEvent.missed(aLocation, opponent, this));
		}
	}
	
	/**
	 * Makes an attack in a location adjacent to the combatant's current location.
	 *
	 * @param		aWorld						a non-null World in which the attack occurs
	 * @param		aDirection					one of the direction codes in class Location
	 * @exception	JCavernInternalError 		could not perform the attack
	 * @exception	EmptyLocationException 		tried to attack an empty Location
	 * @exception	NonCombatantException 		tried to attack a non-combatant (i. e., treasure chest, castle)
	 * @exception	IllegalLocationException 	tried to attack an illegal location (i. e., off the edge of the world)
	 */
	public void attack(World aWorld, int aDirection) throws JCavernInternalError, EmptyLocationException, IllegalLocationException, NonCombatantException
	{
		Location	aLocation = aWorld.getLocation(this).getNeighbor(aDirection);
		Thing		potentialOpponent = aWorld.getThing(aLocation);
	
		attack(aWorld, potentialOpponent);
	}
	
	/**
	 * Performs a ranged attack on a potential opponent, if appropriate.
	 *
	 * @param		aWorld						a non-null World in which the attack occurs
	 * @param		aLocation					a non-null Location containing the attackee
	 * @param		potentialOpponent			a non-null Thing to attack
	 * @exception	JCavernInternalError 		could not perform the attack
	 * @exception	NonCombatantException 		tried to attack a non-combatant (i. e., treasure chest, castle)
	 */
	public void rangedAttack(World aWorld, Location aLocation, Thing potentialOpponent) throws JCavernInternalError, NonCombatantException
	{
		//System.out.println(this + " ranged attack " + potentialOpponent);
		
		if (! (potentialOpponent instanceof Combatant))
		{
			throw new NonCombatantException(potentialOpponent + " is not a combatant!");
		}
		
		Combatant opponent = (Combatant) potentialOpponent;
		
		decrementRangedAttackCount();

		if (this.canRangedAttack(opponent))
		{
			finishAttack(aWorld, aLocation, computeRangedDamageTo(opponent), opponent);
		}
		else
		{
			aWorld.eventHappened(CombatEvent.missed(aLocation, opponent, this));
		}
	}
		
	/**
	 * Makes a ranged attack in a particular direction.
	 *
	 * @param		aWorld						a non-null World in which the attack occurs
	 * @param		aDirection					one of the direction codes in class Location
	 * @exception	JCavernInternalError 		could not perform the attack
	 * @exception	NonCombatantException 		tried to attack a non-combatant (i. e., treasure chest, castle)
	 * @exception	IllegalLocationException 	tried to attack an illegal location (i. e., off the edge of the world)
	 */
	public void rangedAttack(World aWorld, int aDirection) throws JCavernInternalError, NonCombatantException, IllegalLocationException
	{
		Thing		potentialOpponent = aWorld.getThingToward(this, aDirection);
		Location	aLocation = aWorld.getLocation(potentialOpponent);
	
		rangedAttack(aWorld, aLocation, potentialOpponent);
	}
		
	/**
	 * Returns the verb for attacking this combatant.
	 *
	 * @return	a non-null string containing a singular, transitive, past-tense verb
	 */
	public String getHitVerb()
	{
		return "hit"; 
	}
	
	/**
	 * Returns the verb for killing this combatant.
	 *
	 * @return		a non-null string containing a singular, transitive, past-tense verb.
	 */
	public String getKilledVerb()
	{
		return "killed";
	}
	
	/**
	 * Concludes an attack by removing dead opponents and awarding experience, if appropriate.
	 *
	 * @param		aWorld					a non-null World in which the fight takes place.
	 * @param		aLocation				a non-null Location where the attack took place.
	 * @param		damage					how much damage was inflicted
	 * @param		opponent				who suffered that damage
	 * @exception	JCavernInternalError	could not perform the attack
	 */
	private void finishAttack(World aWorld, Location aLocation, int damage, Combatant opponent) throws JCavernInternalError
	{
		int actualDamage = opponent.sufferDamage(aWorld, damage);

		try
		{
			if (opponent.isDead())
			{
				aWorld.eventHappened(CombatEvent.killed(aLocation, opponent, this, actualDamage));
				aWorld.remove(opponent);

				aWorld.eventHappened(CombatEvent.gained(aLocation, this, opponent.getWorth()));				
				gainPoints(aWorld, opponent);
				mKills++;
			}
			else
			{
				aWorld.eventHappened(CombatEvent.hit(aLocation, opponent, this, actualDamage));
				aWorld.eventHappened(CombatEvent.lost(aLocation, opponent, actualDamage));		
			}
		}
		catch (JCavernInternalError jcie)
		{
			throw new JCavernInternalError("tried to finish attack, dead opponent not found");
		}
	}
	
	/**
	 * Gain points from a victory.
	 * Augments this Combatant's points by the number of points the loser was worth.
	 *
	 * @param	aWorld		a non-null World in which the action takes place
	 * @param	theVictim	a non-null Combatant, freshly killed
	 */
	public void gainPoints(World aWorld, Combatant theVictim)
	{
		mPoints += theVictim.getWorth();
		mMaximumPoints = Math.max(mMaximumPoints, mPoints);
	}
	
	/**
	 * Returns the number of points this Combatant is worth when it loses a battle.
	 *
	 * @return		the number of points this Combatant is worth when it loses a battle.
	 */
	public abstract int getWorth();
	
	/**
	 * Returns whether this Combatant is dead.
	 * 
	 * @return	<CODE>true</CODE> if this Combatant has fewer than zero points, <CODE>false</CODE> otherwise.
	 */
	public boolean isDead()
	{
		return mPoints < 0;
	}
	
	/**
	 * Returns the current number of points for this Combatant.
	 *
	 * @return		the current number of points for this Combatant.
	 */
	public int getPoints()
	{
		return mPoints;
	}
	
	/**
	 * Returns the current number of Combatants this Combatant has killed.
	 *
	 * @return		the current number of Combatants this Combatant has killed.
	 */
	public int getKills()
	{
		return mKills;
	}
	
	/**
	 * Returns the maximum number of points this Combatant has ever had.
	 *
	 * @return	Combatant's lifetime maximum points.
	 */
	public int getMaximumPoints()
	{
		return mMaximumPoints;
	}
	
	/**
	 * Creates a GraphicalThingView appropriate to this Thing.
	 *
	 * @param	inImageName		a non-null String image name
	 * @return					a non-null GraphicalThingView appropriate to this Thing.
	 */
	public GraphicalThingView createGraphicalThingView(String inImageName)
	{
		return new GraphicalCombatantView(inImageName);
	}
	
	/**
	 * Creates a new, visible combatant.
	 *
	 * @param	name		a non-null Name for this combatant
	 * @param	imageName	a non-null filename for the image that represents this combatant
	 * @param	points		how many points this Combatant has.
	 */
	public Combatant(String name, String imageName, int points)
	{
		this(name, imageName, points, 0, false);
	}
	

	/**
	 * Creates a new, possibly invisible combatant.
	 *
	 * @param	name		a non-null Name for this combatant
	 * @param	imageName	a non-null filename for the image that represents this combatant
	 * @param	points		how many points this Combatant has.
	 * @param	kills		how many kills this Combatant has.
	 * @param	invisible	<CODE>true</CODE> if this Combatant is invisible, <CODE>false</CODE> otherwise.
	 */
	public Combatant(String name, String imageName, int points, int kills, boolean invisible)
	{
		super(name, imageName, invisible);
		mPoints = points;
		mKills = kills;
		mMaximumPoints = points;
	}
}
