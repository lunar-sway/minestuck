package com.mraof.minestuck.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

/**
 * Abstract class that provide a way to track an action with optional durations for animated entities.
 */
public abstract class AnimatedCreatureEntity extends PathfinderMob
{
	private static final EntityDataAccessor<Integer> CURRENT_ACTION = SynchedEntityData.defineId(AnimatedCreatureEntity.class, EntityDataSerializers.INT);
	
	private int animationTicks = 0;
	
	
	protected AnimatedCreatureEntity(EntityType<? extends AnimatedCreatureEntity> type, Level level)
	{
		super(type, level);
	}
	
	@Override
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		entityData.define(CURRENT_ACTION, Actions.NONE.ordinal());
	}
	
	@Override
	public void tick()
	{
		super.tick();
		if(animationTicks > 0)
		{
			animationTicks--;
			if(animationTicks == 0)
				this.endTimedAction(this.getCurrentAction());
		}
	}
	
	/**
	 * Gets called when the duration of a timed action has run out.
	 * Default behavior is to set the action to NONE, but this function can be overridden to add behavior for specific actions.
	 * @param action the action whose duration ran out.
	 */
	protected void endTimedAction(Actions action)
	{
		this.setCurrentAction(Actions.NONE);
	}
	
	/**
	 * Get the current action to coordinate animations
	 *
	 * @return The action this entity is currently executing
	 */
	protected Actions getCurrentAction()
	{
		return Actions.values()[this.entityData.get(CURRENT_ACTION)];
	}
	
	/**
	 * Used to set the entity's action
	 *
	 * @param action     The action to be performed
	 * @param tickLength -1 to for infinite action or any positive value to set the tick length
	 */
	public void setCurrentAction(Actions action, int tickLength)
	{
		this.entityData.set(CURRENT_ACTION, action.ordinal());
		this.animationTicks = tickLength;
	}
	
	/**
	 * Used to set the entity's action - will repeat indefinitely
	 *
	 * @param action The action to be performed
	 */
	public void setCurrentAction(Actions action)
	{
		setCurrentAction(action, -1);
	}
	
	public enum Actions
	{
		NONE,
		TALK,
		PANIC
	}
}