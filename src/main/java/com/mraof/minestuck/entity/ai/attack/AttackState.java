package com.mraof.minestuck.entity.ai.attack;

/**
 * The state of attack that an entity may be in.
 * Used for slow attacks that have a preparation and recovery phase.
 */
public enum AttackState
{
	NONE,
	ATTACK,
	ATTACK_RECOVERY;
	
	/**
	 * An interface for an entity which may hold an attack state.
	 */
	public interface Holder
	{
		/**
		 * @return the current state of the entity's melee attack
		 */
		AttackState getAttackState();
		
		/**
		 * Used to set the entity's attack state.
		 * The attack state is meant to be set exclusively by {@link MoveToTargetGoal}.
		 *
		 * @param state The new state of the entity's melee attack
		 */
		void setAttackState(AttackState state);
		
		/**
		 * @return true during the attack animation right before an attack lands.
		 */
		default boolean isPreparingToAttack()
		{
			return this.getAttackState() == ATTACK;
		}
		
		/**
		 * Used to start animations and other things
		 *
		 * @return true if the entity performing an attack
		 */
		default boolean isAttacking()
		{
			AttackState state = this.getAttackState();
			return state == ATTACK || state == ATTACK_RECOVERY;
		}
	}
}
