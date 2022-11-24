package com.mraof.minestuck.entity.animation;

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
	 * An interface for an entity which may hold an animation phase.
	 */
	public interface Holder
	{
		/**
		 * @return the current phase of the entity's animation
		 */
		MobAnimationPhases getPhase();
		
		/**
		 * Used to set the entity's animation phase.
		 *
		 * @param phase The new phase of the entity's animation
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
