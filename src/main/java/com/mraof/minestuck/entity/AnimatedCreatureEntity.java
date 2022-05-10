package com.mraof.minestuck.entity;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public abstract class AnimatedCreatureEntity extends CreatureEntity implements IAnimatable
{
	private final AnimationFactory factory = new AnimationFactory(this);
	protected static final DataParameter<Boolean> IS_ATTACKING = EntityDataManager.defineId(AnimatedCreatureEntity.class, DataSerializers.BOOLEAN);

	private int heavyAttackTicks = 0;
	private int recoveryTicks = 0;
	private int attackDelay;
	private int attackRecovery;

	protected AnimatedCreatureEntity(EntityType<? extends CreatureEntity> type, World world) {
		super(type, world);
	}

	@Override
	protected void registerGoals()
	{
		super.registerGoals();
		goalSelector.addGoal(3, new DelayedAttackGoal(this, 1F, false));
	}

	@Override
	public void tick() {
		super.tick();
		if (heavyAttackTicks > 0) {
			heavyAttackTicks--;
			if (heavyAttackTicks == 0) {
				recoveryTicks = attackRecovery;
				if (getTarget() != null && isInRange(getTarget())) {
					doHurtTarget(getTarget());
				}
			}
		}
		if (recoveryTicks > 0) {
			recoveryTicks--;
			if (recoveryTicks == 0) {
				entityData.set(IS_ATTACKING, false);
			}
		}
	}

	private boolean isInRange(LivingEntity target) {
		double reach = this.getBbWidth() * 2.0F * this.getBbWidth() * 2.0F + target.getBbWidth();
		return this.distanceToSqr(target) <= reach;
	}

	private void startAttack() {
		if (heavyAttackTicks <= 0 && recoveryTicks <= 0) {
			heavyAttackTicks = attackDelay;
			entityData.set(IS_ATTACKING, true);
		}
	}

	protected AnimationController<AnimatedCreatureEntity> createAnimation(String name, double speed, AnimationController.IAnimationPredicate<AnimatedCreatureEntity> predicate) {
		AnimationController<AnimatedCreatureEntity> controller = new AnimationController<>(this, name, 0, predicate);
		controller.setAnimationSpeed(speed);
		return controller;
	}

	public AnimationFactory getFactory() {
		return this.factory;
	}

	public void setAttackRecovery(int attackRecovery) {
		this.attackRecovery = attackRecovery;
	}

	public void setAttackDelay(int attackDelay) {
		this.attackDelay = attackDelay;
	}

	public boolean isAttacking() {
		return this.entityData.get(IS_ATTACKING);
	}

	private static class DelayedAttackGoal extends MeleeAttackGoal
	{
		private final AnimatedCreatureEntity entity;

		public DelayedAttackGoal(AnimatedCreatureEntity entity, float speed, boolean useMemory)
		{
			super(entity, speed, useMemory);
			this.entity = entity;
		}

		@Override
		protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
			double reach = this.getAttackReachSqr(enemy);
			if (distToEnemySqr <= reach && this.ticksUntilNextAttack <= 0) {
				this.resetAttackCooldown();
				this.mob.swing(Hand.MAIN_HAND);
				this.mob.getNavigation().stop();
				entity.startAttack();
			}
		}
	}
}