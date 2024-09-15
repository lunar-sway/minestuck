package com.mraof.minestuck.entity.animation;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;

import javax.annotation.Nullable;

/**
 * Keeps track of a given animation. Can store stages of the animation in which to perform additional action/events
 *
 * @param action         category of the animation
 * @param animationLength last frame of the animation
 * @param freezeMovement  whether the entity can move voluntarily during an animation, handled both in Goals and is updated upon the change of animation in AnimatedPathfinderMob
 * @param freezeSight     whether the entity has their facing direction locked, handled by Goals
 */
public record MobAnimation(Action action, int animationLength, boolean freezeMovement, boolean freezeSight)
{
	public static final int LOOPING_ANIMATION = -1; //when set to -1, it will continue looping until another animation overrides it
	public static final Action IDLE_ACTION = Action.IDLE;
	public static final MobAnimation DEFAULT_IDLE_ANIMATION = new MobAnimation(IDLE_ACTION, LOOPING_ANIMATION, false, false);
	
	
	/**
	 * Util function to determine how fast an animation should go
	 * @param entity The entity playing the animation
	 * @param speedModifyingAttribute The attribute used to determine the animation's speed
	 * @return The speed at which the animation should play
	 */
	public static double getAttributeAffectedSpeed(LivingEntity entity, @Nullable Attribute speedModifyingAttribute)
	{
		if(speedModifyingAttribute == null)
			return 1;
		AttributeInstance attributeInstance = entity.getAttribute(speedModifyingAttribute);
		return attributeInstance == null ? 1 : attributeInstance.getValue();
	}
	
	/**
	 * @param entity the entity to check
	 * @return whether the entity is moving horizontally
	 */
	public static boolean isEntityMovingHorizontally(Entity entity)
	{
		return entity.getDeltaMovement().horizontalDistanceSqr() > 0;
	}
	
	/**
	 * Animations that at least one mob uses. The only kind of animation that is likely to occur at the same time as one of these is walking
	 */
	public enum Action
	{
		//passive/neutral actions
		IDLE,
		TALK,
		PANIC,
		
		//aggressive actions
		RIGHT_PUNCH,
		LEFT_PUNCH,
		CLAW,
		BITE,
		KICK,
		SWING,
		MELEE, //weapon
		SLAM,
		SHOOT, //projectile
		SHOVE;
		
		/**
		 * Converts int back into enum
		 */
		public static Action fromInt(int ordinal)
		{
			if(0 <= ordinal && ordinal < Action.values().length)
				return Action.values()[ordinal];
			else
				throw new IllegalArgumentException("Invalid ordinal of " + ordinal + " for action!");
		}
	}
}