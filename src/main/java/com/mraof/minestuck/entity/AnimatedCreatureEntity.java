package com.mraof.minestuck.entity;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationFactory;

/**
 * Abstract class that provide a way to track an action with optional durations for animated entities.
 */
public abstract class AnimatedCreatureEntity extends CreatureEntity implements IAnimatable
{
	private final AnimationFactory factory = new AnimationFactory(this);
	private static final DataParameter<Integer> CURRENT_ACTION = EntityDataManager.defineId(AnimatedCreatureEntity.class, DataSerializers.INT);
	
	private int animationTicks = 0;
	
	
	protected AnimatedCreatureEntity(EntityType<? extends AnimatedCreatureEntity> type, World world)
	{
		super(type, world);
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
	
	protected void endTimedAction(Actions action)
	{
		this.setCurrentAction(Actions.NONE);
	}
	
	@Override
	public AnimationFactory getFactory()
	{
		return this.factory;
	}
	
	/**
	 * Helper to create a new animation controller with custom animation speed
	 *
	 * @param name      name of this controller
	 * @param speed     animation speed - default speed is 1
	 * @param predicate the animation predicate
	 * @return a configured animation controller with speed
	 */
	protected static <T extends IAnimatable> AnimationController<T> createAnimation(T entity, String name, double speed, AnimationController.IAnimationPredicate<T> predicate)
	{
		AnimationController<T> controller = new AnimationController<>(entity, name, 0, predicate);
		controller.setAnimationSpeed(speed);
		return controller;
	}
	
	/**
	 * Used to start animations
	 *
	 * @return true if the entity performing an attack
	 */
	protected boolean isAttacking()
	{
		Actions action = this.getCurrentAction();
		return action == Actions.ATTACK || action == Actions.ATTACK_RECOVERY;
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
	 * @return true if an action has been set with a finite time
	 * with setCurrentAction(Action, int) and its duration has not yet run out.
	 */
	public boolean hasTimedAction()
	{
		return this.animationTicks > 0;
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
		ATTACK,
		ATTACK_RECOVERY,
		TALK,
		PANIC
	}
}