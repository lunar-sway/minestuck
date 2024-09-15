package com.mraof.minestuck.entity.animation;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;

import javax.annotation.Nullable;

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
	@Nullable
	private final Attribute speedModifyingAttribute;
	
	public 	PhasedMobAnimation(MobAnimation animation, int initiationStart, int contactStart, int recoveryStart, int recoveryEnd)
	{
		this(animation, initiationStart, contactStart, recoveryStart, recoveryEnd, Attributes.ATTACK_SPEED);
	}
	
	
	/**
	 * @param initiationStart not the first frame of animation
	 * @param contactStart the apex of animations, when attacks connect
	 * @param speedModifyingAttribute the entity attribute responsible for affecting the animation's speed
	 */
	public PhasedMobAnimation(MobAnimation animation, int initiationStart, int contactStart, int recoveryStart, int recoveryEnd, @Nullable Attribute speedModifyingAttribute)
	{
		this.animation = animation;
		this.initiationStart = initiationStart;
		this.contactStart =  contactStart;
		this.recoveryStart =  recoveryStart;
		this.recoveryEnd =  recoveryEnd;
		this.speedModifyingAttribute = speedModifyingAttribute;
	}
	
	@Nullable
	public Attribute getSpeedModifyingAttribute()
	{
		return speedModifyingAttribute;
	}
	
	public Phases getCurrentPhase(PathfinderMob entity, int time, double speed)
	{
		if(time < getInitiationStartTime(speed))
			return Phases.ANTICIPATION;
		else if(time < getContactStartTime(speed))
			return Phases.INITIATION;
		else if(time < getRecoveryStartTime(speed))
			return Phases.CONTACT;
		else if(time < getTotalAnimationLength(speed))
			return Phases.RECOVERY;
		else
			return Phases.NEUTRAL;
	}
	
	public int getInitiationStartTime(double speed)
	{
		return (int) Math.round(initiationStart / speed);
	}
	
	public int getContactStartTime(double speed)
	{
		return (int) Math.round(contactStart / speed);
	}
	
	public int getRecoveryStartTime(double speed)
	{
		return (int) Math.round(recoveryStart / speed);
	}
	
	
	public MobAnimation getAnimation()
	{
		return animation;
	}
	
	/**
	 * Equivalent to getting recoveryEnd value
	 */
	public int getTotalAnimationLength(double speed)
	{
		return (int) Math.round(recoveryEnd / speed);
	}
	
	/**
	 * Is called every tick to check whether its time to transition to a new phase
	 */
	public <T extends PathfinderMob & PhasedMobAnimation.Phases.Holder> void attemptPhaseChange(int time, T entity, double speed)
	{
		if(time == getInitiationStartTime(speed))
			entity.setAnimationPhase(Phases.INITIATION, animation.action());
		else if(time == getContactStartTime(speed))
			entity.setAnimationPhase(Phases.CONTACT, animation.action());
		else if(time == getRecoveryStartTime(speed))
			entity.setAnimationPhase(Phases.RECOVERY, animation.action());
		else if(time >= getTotalAnimationLength(speed))
			entity.setAnimationPhase(Phases.NEUTRAL, animation.action());
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
			void setAnimationPhase(PhasedMobAnimation.Phases phase, MobAnimation.Action animation);
			
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
