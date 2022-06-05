package com.mraof.minestuck.entity;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationFactory;

/**
 * Abstract class that provide different ways to manage timings and delays for animated entities.
 */
public abstract class AnimatedCreatureEntity extends CreatureEntity implements IAnimatable
{
	private final AnimationFactory factory = new AnimationFactory(this);
	private final AttributeModifier knockback;
	private static final DataParameter<Integer> CURRENT_ACTION = EntityDataManager.defineId(AnimatedCreatureEntity.class, DataSerializers.INT);
	
	private int animationTicks = 0;
	
	/**
	 * The delay between the start of the animation and the moment the damage lands
	 */
	protected int attackDelay;
	
	/**
	 * The delay after an attack and before another start
	 */
	protected int attackRecovery;
	
	/**
	 * Will stop the entity while performing its attack animation
	 */
	protected boolean canMoveWhileAttacking = false;
	
	protected AnimatedCreatureEntity(EntityType<? extends CreatureEntity> type, World world)
	{
		this(type, world, 0);
	}
	
	/**
	 * @param knockbackResist Extra knockback resistance while attacking
	 */
	protected AnimatedCreatureEntity(EntityType<? extends CreatureEntity> type, World world, double knockbackResist)
	{
		super(type, world);
		knockback = new AttributeModifier("attack.knockback", knockbackResist, AttributeModifier.Operation.ADDITION);
	}
	
	@Override
	protected void registerGoals()
	{
		super.registerGoals();
		goalSelector.addGoal(3, new DelayedAttackGoal(this, 1F, false));
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
			if(animationTicks == 0 && getCurrentAction() == Actions.ATTACK)
			{
				animationTicks = attackRecovery;
				this.entityData.set(CURRENT_ACTION, Actions.ATTACK_RECOVERY.ordinal());
				performAttack();
			} else if(animationTicks == 0)
			{
				this.entityData.set(CURRENT_ACTION, Actions.NONE.ordinal());
			}
		}
	}
	
	private void performAttack()
	{
		ModifiableAttributeInstance instance = getAttributes().getInstance(Attributes.KNOCKBACK_RESISTANCE);
		if(instance != null) {
			instance.removeModifier(knockback);
		}
		
		if(getTarget() != null && isInRange(getTarget()))
		{
			doHurtTarget(getTarget());
			// TODO: AOE bounding box collision checks + aoe flag
		}
	}
	
	private boolean isInRange(LivingEntity target)
	{
		double reach = this.getBbWidth() * 2.0F * this.getBbWidth() * 2.0F + target.getBbWidth();
		return this.distanceToSqr(target) <= reach;
	}
	
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
	protected AnimationController<AnimatedCreatureEntity> createAnimation(String name, double speed, AnimationController.IAnimationPredicate<AnimatedCreatureEntity> predicate)
	{
		AnimationController<AnimatedCreatureEntity> controller = new AnimationController<>(this, name, 0, predicate);
		controller.setAnimationSpeed(speed);
		return controller;
	}
	
	/**
	 * Starts a long attack against the current target
	 * Useful to sync animations and add extra delays
	 */
	public void startAttack()
	{
		if(animationTicks <= 0)
		{
			animationTicks = attackDelay;
			this.entityData.set(CURRENT_ACTION, Actions.ATTACK.ordinal());
			
			ModifiableAttributeInstance instance = getAttributes().getInstance(Attributes.KNOCKBACK_RESISTANCE);
			if(instance != null && !instance.hasModifier(knockback)) {
				instance.addTransientModifier(knockback);
			}
		}
	}
	
	/**
	 * Used to start animations
	 *
	 * @return true if the entity performing an attack
	 */
	protected boolean isAttacking()
	{
		return Actions.ATTACK.isEqual(this.entityData.get(CURRENT_ACTION)) || Actions.ATTACK_RECOVERY.isEqual(this.entityData.get(CURRENT_ACTION));
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
		ATTACK,
		ATTACK_RECOVERY,
		TALK,
		PANIC;
		
		public boolean isEqual(int ordinal)
		{
			return this.ordinal() == ordinal;
		}
	}
	
	private static class DelayedAttackGoal extends MeleeAttackGoal
	{
		private final AnimatedCreatureEntity entity;
		
		/**
		 * The same as MeleeAttackGoal but it does not apply damage immediately when performing an attack
		 * Should be used only internally by AnimatedCreatureEntity
		 */
		private DelayedAttackGoal(AnimatedCreatureEntity entity, float speed, boolean useMemory)
		{
			super(entity, speed, useMemory);
			this.entity = entity;
		}
		
		@Override
		protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr)
		{
			double reach = this.getAttackReachSqr(enemy);
			if(distToEnemySqr <= reach && this.ticksUntilNextAttack <= 0)
			{
				this.resetAttackCooldown();
				if(!this.entity.canMoveWhileAttacking)
				{
					this.mob.getNavigation().stop();
				}
				entity.startAttack();
			}
		}
	}
}