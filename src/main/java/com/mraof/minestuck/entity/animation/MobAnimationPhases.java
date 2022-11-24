package com.mraof.minestuck.entity.animation;

import com.mraof.minestuck.entity.ai.attack.MoveToTargetGoal;

/**
 * Subdivision of animations from MSMobAnimation that is split into stages where additional functionality can be implemented, such as particles or sound effects.
 * Primarily used for combat animations.
 */
public enum MobAnimationPhases
{
	NEUTRAL,
	ANTICIPATION,
	INITIATION,
	CONTACT,
	RECOVERY;
	
	/**
	 * An interface for an entity which may hold an attack state.
	 */
	public interface Holder
	{
		/**
		 * @return the current state of the entity's melee attack
		 */
		MobAnimationPhases getPhase();
		
		/**
		 * Used to set the entity's animation phase.
		 * The attack phase is meant to be set exclusively by {@link MoveToTargetGoal}.
		 *
		 * @param phase The new phase of the entity's melee attack
		 */
		void setAnimationPhase(MobAnimationPhases phase, MSMobAnimation.Actions animation);
		
		/**
		 * @return true if the animation has not reached its apex
		 */
		default boolean isBeforeContact()
		{
			return this.getPhase().ordinal() < CONTACT.ordinal();
		}
		
		/**
		 * Used to start animations and other things
		 *
		 * @return true if the entity performing the animation
		 */
		default boolean isActive()
		{
			MobAnimationPhases state = this.getPhase();
			return state != NEUTRAL;
		}
	}
}
