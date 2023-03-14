package com.mraof.minestuck.entity.animation;

import net.minecraft.world.entity.PathfinderMob;

/**
 * A supplement to MobAnimation that allows for certain code to be performed at the point where one phase of an animation ends and the other begins.
 * Primarily intended for use with {@link com.mraof.minestuck.entity.ai.MobAnimationPhaseGoal}
 */
public class PhasedMobAnimation
{
	private final MobAnimation animation;
	private final int initiationStart;
	private final int contactStart;
	private final int recoveryStart;
	private final int recoveryEnd;
	
	/**
	 * @param initiationStart not the first frame of animation
	 * @param contactStart the apex of animations, when attacks connect
	 */
	public PhasedMobAnimation(MobAnimation animation, int initiationStart, int contactStart, int recoveryStart)
	{
		this.animation = animation;
		this.initiationStart = initiationStart;
		this.contactStart = contactStart;
		this.recoveryStart = recoveryStart;
		this.recoveryEnd = animation.getAnimationLength(); //recoveryEnd is identical to animationLength in MobAnimation
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
	
	public MobAnimation getAnimation()
	{
		return animation;
	}
	
	/**
	 * Equivalent to getting recoveryEnd value
	 */
	public int getTotalAnimationLength()
	{
		return recoveryEnd;
	}
	
	/**
	 * Is called every tick to check whether its time to transition to a new phase
	 */
	public <T extends PathfinderMob & PhasedMobAnimation.Phases.Holder> void attemptPhaseChange(int time, T entity)
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
	
	/**
	 * A list of all the phases, the transition to ANTICIPATION is not included in a PhasedMobAnimation object because the transition time is equivalent to the start of when its called.
	 * Contains the Holder interface
	 */
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
			PhasedMobAnimation.Phases getPhase();
			
			/**
			 * Used to set the entity's animation phase.
			 *
			 * @param phase The new phase of the entity's animation
			 */
			void setAnimationPhase(PhasedMobAnimation.Phases phase, MobAnimation.Actions animation);
			
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
				PhasedMobAnimation.Phases state = this.getPhase();
				return state != NEUTRAL;
			}
		}
	}
}
