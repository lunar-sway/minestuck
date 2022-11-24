package com.mraof.minestuck.entity;

import com.mraof.minestuck.entity.animation.MSMobAnimation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

/**
 * Abstract class that provide a way to track an action with optional durations for animated entities.
 */
public abstract class AnimatedPathfinderMob extends PathfinderMob
{
	/**
	 * Retains the action from MSMobAnimation
	 */
	private static final EntityDataAccessor<Integer> CURRENT_ACTION = SynchedEntityData.defineId(AnimatedPathfinderMob.class, EntityDataSerializers.INT);
	
	private MSMobAnimation mobAnimation = MSMobAnimation.DEFAULT_IDLE_ANIMATION;
	private int remainingAnimationTicks;
	
	
	protected AnimatedPathfinderMob(EntityType<? extends AnimatedPathfinderMob> type, Level level)
	{
		super(type, level);
	}
	
	@Override
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		entityData.define(CURRENT_ACTION, MSMobAnimation.IDLE_ACTION.ordinal());
	}
	
	@Override
	public void tick()
	{
		super.tick();
		
		if(mobAnimation != null && mobAnimation.freezesMovement())
			this.getNavigation().stop();
		
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
	protected void endCurrentAction()
	{
		this.setCurrentAnimation(MSMobAnimation.DEFAULT_IDLE_ANIMATION);
	}
	
	/**
	 * Get the current action to coordinate animations
	 *
	 * @return The action this entity is currently executing
	 */
	protected MSMobAnimation getCurrentAnimation()
	{
		return mobAnimation;
	}
	
	/**
	 * Get the current action to coordinate animations
	 *
	 * @return The action this entity is currently executing
	 */
	protected MSMobAnimation.Actions getCurrentAction()
	{
		return MSMobAnimation.Actions.values()[this.entityData.get(CURRENT_ACTION)];
	}
	
	/**
	 * Used to set the entity's action
	 *
	 * @param MSMobAnimation The animation that contains the action
	 */
	public void setCurrentAnimation(MSMobAnimation MSMobAnimation)
	{
		this.entityData.set(CURRENT_ACTION, MSMobAnimation.getAction().ordinal());
		this.mobAnimation = MSMobAnimation;
		this.remainingAnimationTicks = MSMobAnimation.getAnimationLength();
	}
	
	/**
	 * Typically used by passive mobs
	 */
	public MSMobAnimation getPanicAnimation()
	{
		return null;
	}
	
	public MSMobAnimation getDefaultAttackAnimation()
	{
		return null;
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag compound)
	{
		super.addAdditionalSaveData(compound);
		compound.put("mobAnimation", mobAnimation.getCompoundTag());
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag compound)
	{
		if(compound.contains("mobAnimation"))
			mobAnimation = MSMobAnimation.createTrackerFromCompoundTag(compound.getCompound("mobAnimation"));
		else
			mobAnimation = MSMobAnimation.DEFAULT_IDLE_ANIMATION;
	}
}