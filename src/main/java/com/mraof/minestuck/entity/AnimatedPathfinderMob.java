package com.mraof.minestuck.entity;

import com.mraof.minestuck.entity.animation.MobAnimation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

/**
 * Abstract class for handling the actions of Geckolib animated mobs. It keeps track of which animation is in use via a MobAnimation and EntityDataAccessor
 */
public abstract class AnimatedPathfinderMob extends PathfinderMob
{
	/**
	 * Retains the action from MobAnimation
	 */
	private static final EntityDataAccessor<Integer> CURRENT_ACTION = SynchedEntityData.defineId(AnimatedPathfinderMob.class, EntityDataSerializers.INT);
	
	private MobAnimation mobAnimation = MobAnimation.DEFAULT_IDLE_ANIMATION;
	
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
	public void endCurrentAction()
	{
		this.setCurrentAnimation(MobAnimation.DEFAULT_IDLE_ANIMATION);
	}
	
	/**
	 * Get the current action to coordinate animations
	 *
	 * @return The action this entity is currently executing
	 */
	public MobAnimation getCurrentAnimation()
	{
		return mobAnimation;
	}
	
	/**
	 * Get the current action to coordinate animations
	 *
	 * @return The action this entity is currently executing
	 */
	protected MobAnimation.Actions getCurrentAction()
	{
		return MobAnimation.Actions.values()[this.entityData.get(CURRENT_ACTION)];
	}
	
	/**
	 * Used to set the entity's animation and action
	 *
	 * @param animation The animation to set, also contains the action to set entityData from
	 */
	public void setCurrentAnimation(MobAnimation animation)
	{
		this.entityData.set(CURRENT_ACTION, animation.getAction().ordinal());
		this.mobAnimation = animation;
		this.remainingAnimationTicks = animation.getAnimationLength();
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
			mobAnimation = MobAnimation.createTrackerFromCompoundTag(compound.getCompound("mobAnimation"));
		else
			mobAnimation = MobAnimation.DEFAULT_IDLE_ANIMATION;
	}
}