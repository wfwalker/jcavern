/* 
	JCavernApplet.java

	Title:			JCavern And Glen
	Author:			Bill Walker
	Description:	
*/

package jcavern;

/**
 * Represents a Thing that can participate in combat.
 */
public abstract class Combatant extends Thing
{
	protected static final int	HIT = 1;
	
	protected static final int	MISS = 2;
	
	protected static final int	KILL = 3;
	
	private int					mPoints;
	
	// Combatants can compute and suffer damage they cause
	
	/**
	 * Returns whether this Combatant can attack a given Combatant.
	 */
	public abstract boolean canAttack(Combatant aCombatant);
	
	/**
	 * Returns whether this Combatant can make a ranged attack on a given Combatant.
	 */
	public abstract boolean canRangedAttack(Combatant aCombatant);
	
	/**
	 * Returns whether this combatant is vulnerable to attack from a given Monster.
	 */
	public boolean vulnerableToMonsterAttack(Monster aMonster)
	{
		//System.out.println("Combatant.vulnerableToMonsterAttack(Combatant)");
		return true;
	}
	
	/**
	 * Returns whether this combatant is vulnerable to attack from a given Player.
	 */
	public boolean vulnerableToPlayerAttack(Player aPlayer)
	{
		//System.out.println("Combatant.vulnerableToPlayerAttack(Combatant)");
		return true;
	}
	
	/**
	 * Returns whether this combatant is vulnerable to attack from a given Monster.
	 */
	public boolean vulnerableToMonsterRangedAttack(Monster aMonster)
	{
		//System.out.println("Combatant.vulnerableToMonsterRangedAttack(Combatant)");
		return true;
	}
	
	/**
	 * Returns whether this combatant is vulnerable to attack from a given Player.
	 */
	public boolean vulnerableToPlayerRangedAttack(Player aPlayer)
	{
		System.out.println("Combatant.vulnerableToPlayerRangedAttack(Combatant)");
		return true;
	}
	
	/**
	 * Computes the damage this combatant does to a given opponent in a sword attack.
	 *
	 * @param	opponent	a non-null Combatant on whom the receiver will inflict damage
	 */
	public abstract int computeDamageTo(Combatant opponent);
	
	/**
	 * Computes the damage this combatant does to a given opponent in a ranged attack.
	 *
	 * @param	opponent	a non-null Combatant on whom the receiver will inflict damage
	 */
	public abstract int computeRangedDamageTo(Combatant opponent);

	/**
	 * Causes this combatant to suffer damage.
	 *
	 * @param	theDamage	how much damage this combatant suffers.
	 */
	public void sufferDamage(int theDamage)
	{
		mPoints -= theDamage;
	}
	
	/**
	 * Decrements the number of ranged attacks this Combatant can perform.
	 */
	public abstract void decrementRangedAttackCount();
	
	// Combatants can attack things
	
	/**
	 * Performs an attack on a potential opponent, if appropriate.
	 *
	 * @param	aWorld				a non-null World in which the attack occurs
	 * @param	potentialOpponent	a non-null Thing that the receiver will try to attack
	 */
	public void attack(World aWorld, Thing potentialOpponent) throws JCavernInternalError, NonCombatantException
	{
		if (! (potentialOpponent instanceof Combatant))
		{
			throw new NonCombatantException(potentialOpponent + " is not a combatant!");
		}
		
		Combatant opponent = (Combatant) potentialOpponent;

		if (this.canAttack(opponent))
		{
			finishAttack(aWorld, computeDamageTo(opponent), opponent);
		}
		else
		{
			reportResultAgainst(opponent, Combatant.MISS, 0);
		}
	}
	
	// this method can go up to Combatant
	public void attack(World aWorld, int aDirection) throws JCavernInternalError, EmptyLocationException, IllegalLocationException, NonCombatantException
	{
		Thing	potentialOpponent = aWorld.getThing(aWorld.getLocation(this).getNeighbor(aDirection));
	
		attack(aWorld, potentialOpponent);
	}
	
	/**
	 * Performs a ranged attack on a potential opponent, if appropriate.
	 *
	 * @param	aWorld				a non-null World in which the attack occurs
	 * @param	potentialOpponent	a non-null Thing that the receiver will try to attack
	 */
	public void rangedAttack(World aWorld, Thing potentialOpponent) throws JCavernInternalError, NonCombatantException
	{
		System.out.println(this + " ranged attack " + potentialOpponent);
		
		if (! (potentialOpponent instanceof Combatant))
		{
			throw new NonCombatantException(potentialOpponent + " is not a combatant!");
		}
		
		Combatant opponent = (Combatant) potentialOpponent;
		
		decrementRangedAttackCount();

		if (this.canRangedAttack(opponent))
		{
			finishAttack(aWorld, computeRangedDamageTo(opponent), opponent);
		}
		else
		{
			reportResultAgainst(opponent, Combatant.MISS, 0);
		}
	}
		
	// this method can go up to Combatant
	public void rangedAttack(World aWorld, int aDirection) throws JCavernInternalError, NonCombatantException, IllegalLocationException
	{
		Thing	potentialOpponent = aWorld.getThingToward(this, aDirection);
	
		rangedAttack(aWorld, potentialOpponent);
	}
	
	protected void reportResultAgainst(Combatant opponent, int outcome, int damage)
	{
		switch (outcome)
		{
			case Combatant.MISS: JCavernApplet.log(getName() + " misses " + opponent.getName()); break;
			case Combatant.HIT: JCavernApplet.log(getName() + " hits " + opponent.getName() + " for " + damage); break;
			case Combatant.KILL: JCavernApplet.log(getName() + " kills " + opponent.getName()); break;
		}
	}

	/**
	 * Concludes an attack by removing dead opponents and awarding experience, if appropriate.
	 *
	 * @param	opponent	a non-null Combatant who was attacked by the receiver.
	 */
	private void finishAttack(World aWorld, int damage, Combatant opponent) throws JCavernInternalError
	{
		opponent.sufferDamage(damage);

		try
		{
			if (opponent.isDead())
			{
				reportResultAgainst(opponent, Combatant.KILL, damage);
				gainExperience(opponent);
				aWorld.remove(opponent);
			}
			else
			{
				reportResultAgainst(opponent, Combatant.HIT, damage);
			}
		}
		catch (JCavernInternalError jcie)
		{
			throw new JCavernInternalError("tried to finish attack, dead opponent not found");
		}
	}
	
	public void gainExperience(Combatant theVictim)
	{
		mPoints += theVictim.getWorth();
	}
	
	public abstract int getWorth();
	
	public boolean isDead()
	{
		return mPoints < 0;
	}
	
	public int getPoints()
	{
		return mPoints;
	}
	
	public Combatant(String name, int points)
	{		super(name);
		
		mPoints = points;
	}
}
