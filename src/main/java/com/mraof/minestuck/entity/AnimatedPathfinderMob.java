package com.mraof.minestuck.entity;

import com.mraof.minestuck.entity.animation.MSMobAnimations;
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
	 * Retains the action from MSMobAnimations
	 */
	private static final EntityDataAccessor<Integer> CURRENT_ACTION = SynchedEntityData.defineId(AnimatedPathfinderMob.class, EntityDataSerializers.INT);
	
	private MSMobAnimations mobAnimations = MSMobAnimations.DEFAULT_IDLE_ANIMATION;
	private int remainingAnimationTicks;
	
	
	protected AnimatedPathfinderMob(EntityType<? extends AnimatedPathfinderMob> type, Level level)
	{
		super(type, level);
	}
	
	@Override
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		entityData.define(CURRENT_ACTION, MSMobAnimations.IDLE_ACTION.ordinal());
	}
	
	@Override
	public void tick()
	{
		super.tick();
		
		if(mobAnimations != null && mobAnimations.stopsMovement())
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
		this.setCurrentAnimation(MSMobAnimations.DEFAULT_IDLE_ANIMATION);
	}
	
	/**
	 * Get the current action to coordinate animations
	 *
	 * @return The action this entity is currently executing
	 */
	protected MSMobAnimations getCurrentAnimation()
	{
		return mobAnimations;
	}
	
	/**
	 * Get the current action to coordinate animations
	 *
	 * @return The action this entity is currently executing
	 */
	protected MSMobAnimations.Actions getCurrentAction()
	{
		return MSMobAnimations.Actions.values()[this.entityData.get(CURRENT_ACTION)];
	}
	
	/**
	 * Used to set the entity's action
	 *
	 * @param MSMobAnimations The animation that contains the action
	 */
	public void setCurrentAnimation(MSMobAnimations MSMobAnimations)
	{
		this.entityData.set(CURRENT_ACTION, MSMobAnimations.getAction().ordinal());
		this.mobAnimations = MSMobAnimations;
		this.remainingAnimationTicks = MSMobAnimations.getAnimationLength();
	}
	
	/**
	 * Typically used by passive mobs
	 */
	public MSMobAnimations getPanicAnimation()
	{
		return null;
	}
	
	public MSMobAnimations getDefaultAttackAnimation()
	{
		return null;
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag compound)
	{
		super.addAdditionalSaveData(compound);
		compound.put("mobAnimations", mobAnimations.getCompoundTag());
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag compound)
	{
		if(compound.contains("mobAnimations"))
			mobAnimations = MSMobAnimations.createTrackerFromCompoundTag(compound.getCompound("mobAnimations"));
		else
			mobAnimations = MSMobAnimations.DEFAULT_IDLE_ANIMATION;
	}
}