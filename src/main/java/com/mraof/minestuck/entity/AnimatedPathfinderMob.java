package com.mraof.minestuck.entity;

import com.mraof.minestuck.entity.animation.MobAnimation;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimationController;

import java.util.UUID;

/**
 * Abstract class for handling the action of Geckolib animated mobs. It keeps track of which animation is in use via a MobAnimation and EntityDataAccessor
 */
public abstract class AnimatedPathfinderMob extends PathfinderMob
{
	/**
	 * Retains the action from MobAnimation
	 */
	private static final EntityDataAccessor<Integer> CURRENT_ACTION = SynchedEntityData.defineId(AnimatedPathfinderMob.class, EntityDataSerializers.INT);
	
	private static final UUID FREEZE_MOB_UUID = UUID.fromString("e4c7543e-335c-4217-8d15-25c157e8ce88");
	private static final AttributeModifier STATIONARY_MOB_MODIFIER = new AttributeModifier(FREEZE_MOB_UUID, "Stationary Animation Modifier", -1, AttributeModifier.Operation.MULTIPLY_TOTAL);
	
	private int remainingAnimationTicks; //starts off as the time value stored in mobAnimation when set. Will be set to -1 for looping animations
	
	
	protected AnimatedPathfinderMob(EntityType<? extends AnimatedPathfinderMob> type, Level level)
	{
		super(type, level);
	}
	
	@Override
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		entityData.define(CURRENT_ACTION, MobAnimation.IDLE_ACTION.ordinal());
	}
	
	@Override
	public void tick()
	{
		super.tick();
		
		if(remainingAnimationTicks > 0)
		{
			remainingAnimationTicks--;
			if(remainingAnimationTicks == 0)
				this.endCurrentAction();
		}
	}
	
	/**
	 * Gets called when the duration of time associated with an action has run out.
	 * Default behavior is to set the action to IDLE, but this function can be overridden to add behavior for specific actions.
	 **/
	public void endCurrentAction()
	{
		this.setCurrentAnimation(MobAnimation.DEFAULT_IDLE_ANIMATION);
	}
	
	/**
	 * Get the current action to coordinate animations
	 *
	 * @return The action this entity is currently executing
	 */
	protected MobAnimation.Action getCurrentAction()
	{
		return MobAnimation.Action.values()[this.entityData.get(CURRENT_ACTION)];
	}
	
	public void freezeMob()
	{
		AttributeInstance instance = getAttributes().getInstance(Attributes.MOVEMENT_SPEED);
		if(instance != null && !instance.hasModifier(STATIONARY_MOB_MODIFIER))
			instance.addTransientModifier(STATIONARY_MOB_MODIFIER);
	}
	
	public void unfreezeMob()
	{
		AttributeInstance instance = getAttributes().getInstance(Attributes.MOVEMENT_SPEED);
		if(instance != null)
			instance.removeModifier(STATIONARY_MOB_MODIFIER.getId());
	}
	
	public void setCurrentAnimation(MobAnimation animation)
	{
		setCurrentAnimation(animation, 1);
	}
	
	
	/**
	 * Used to set the entity's animation and action
	 *
	 * @param animation The animation to set, also contains the action to set entityData from
	 * @param animationSpeed Used to adjust the length of the animation. The higher the number, the shorter the animation plays for
	 */
	public void setCurrentAnimation(MobAnimation animation, double animationSpeed)
	{
		if(animation.freezeMovement())
			freezeMob();
		else
			unfreezeMob();
		
		this.entityData.set(CURRENT_ACTION, animation.action().ordinal());
		this.remainingAnimationTicks = (int) Math.round(animation.animationLength() / animationSpeed);
	}
}