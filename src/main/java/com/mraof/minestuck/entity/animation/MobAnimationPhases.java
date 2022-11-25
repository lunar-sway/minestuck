package com.mraof.minestuck.entity.animation;

import net.minecraft.world.entity.PathfinderMob;

/**
 * Subdivision of animations from MobAnimation that is split into stages where additional functionality can be implemented, such as particles or sound effects.
 * Primarily used for combat animations.
 */
public class MobAnimationPhases
{
	private final int initiationStart;
	private final int contactStart;
	private final int recoveryStart;
	private final int recoveryEnd;
	
	public MobAnimationPhases(int initiationStart, int contactStart, int recoveryStart, int recoveryEnd)
	{
		this.initiationStart = initiationStart;
		this.contactStart = contactStart;
		this.recoveryStart = recoveryStart;
		this.recoveryEnd = recoveryEnd;
	}
	
	public Phases getCurrentPhase(int time)
	{
		if(time < initiationStart)
			return Phases.ANTICIPATION;
		else if(time < contactStart)
			return Phases.INITIATION;
		else if(time < recoveryStart)
			return Phases.CONTACT;
		else if(time < recoveryEnd)
			return Phases.RECOVERY;
		else
			return Phases.NEUTRAL;
	}
	
	public int getInitiationStartTime()
	{
		return initiationStart;
	}
	
	public int getContactStartTime()
	{
		return contactStart;
	}
	
	public int getRecoveryStartTime()
	{
		return recoveryStart;
	}
	
	/**
	 * Equivalent to getting recoveryEnd value
	 */
	public int getTotalAnimationLength()
	{
		return recoveryEnd;
	}
	
	public <T extends PathfinderMob & MobAnimationPhases.Phases.Holder> void attemptPhaseChange(int time, T entity, MobAnimation animation)
	{
		if(time == getInitiationStartTime())
			entity.setAnimationPhase(Phases.INITIATION, animation.getAction());
		else if(time == getContactStartTime())
			entity.setAnimationPhase(Phases.CONTACT, animation.getAction());
		else if(time == getRecoveryStartTime())
			entity.setAnimationPhase(Phases.RECOVERY, animation.getAction());
		else if(time >= getTotalAnimationLength())
			entity.setAnimationPhase(Phases.NEUTRAL, animation.getAction());
	}
	
	public enum Phases
	{
		NEUTRAL,
		ANTICIPATION,
		INITIATION,
		CONTACT,
		RECOVERY;
		
		public static Phases nextPhase(Phases phaseIn)
		{
			return Phases.fromInt(phaseIn.ordinal() < Phases.values().length - 1 ? phaseIn.ordinal() + 1 : 0);
		}
		
		/**
		 * Converts int back into enum
		 */
		public static Phases fromInt(int ordinal)
		{
			if(0 <= ordinal && ordinal < Phases.values().length)
				return Phases.values()[ordinal];
			else
				throw new IllegalArgumentException("Invalid ordinal of " + ordinal + " for phases!");
		}
		
		/**
		 * An interface for an entity which may hold an animation phase.
		 */
		public interface Holder
		{
			/**
			 * @return the current phase of the entity's animation
			 */
			MobAnimationPhases.Phases getPhase();
			
			/**
			 * Used to set the entity's animation phase.
			 *
			 * @param phase The new phase of the entity's animation
			 */
			void setAnimationPhase(MobAnimationPhases.Phases phase, MobAnimation.Actions animation);
			
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
				MobAnimationPhases.Phases state = this.getPhase();
				return state != NEUTRAL;
			}
		}
	}
}
