package com.mraof.minestuck.entity;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public abstract class AnimatedCreatureEntity extends CreatureEntity implements IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);
    private static final Multimap<Attribute, AttributeModifier> attributes = HashMultimap.create();
    protected static final DataParameter<Integer> CURRENT_ACTION = EntityDataManager.defineId(AnimatedCreatureEntity.class, DataSerializers.INT);

    private int heavyAttackTicks = 0;
    private int recoveryTicks = 0;
    private int actionResetTicks;

    protected int attackDelay;
    protected int attackRecovery;
    protected boolean canMoveWhileAttacking = false;
    protected double knockbackResistWhileAttacking = 0;

    protected AnimatedCreatureEntity(EntityType<? extends CreatureEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(3, new DelayedAttackGoal(this, 1F, false));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(CURRENT_ACTION, Actions.NONE.ordinal());
    }

    @Override
    public void tick() {
        super.tick();
        if (heavyAttackTicks > 0) {
            heavyAttackTicks--;
            if (heavyAttackTicks == 0) {
                recoveryTicks = attackRecovery;
                getAttributes().removeAttributeModifiers(attributes);
                if (getTarget() != null && isInRange(getTarget())) { // TODO: AOE bounding box collision checks + aoe flag
                    doHurtTarget(getTarget());
                }
            }
        }
        if (recoveryTicks > 0) {
            recoveryTicks--;
            if (recoveryTicks == 0) {
                setAttacking(false);
            }
        }
        if (getCurrentAction() != Actions.NONE && actionResetTicks > 0) {
            actionResetTicks--;
            if (actionResetTicks <= 0) {
                setCurrentAction(Actions.NONE, 0);
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
            setAttacking(true);
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

    public void setAttacking(boolean attacking) {
        if (attacking) {
            this.entityData.set(CURRENT_ACTION, Actions.ATTACK.ordinal());
        } else {
            this.entityData.set(CURRENT_ACTION, Actions.NONE.ordinal());
        }
    }

    public boolean isAttacking() {
        return Actions.ATTACK.isEqual(this.entityData.get(CURRENT_ACTION));
    }

    public Actions getCurrentAction() {
        return Actions.values()[this.entityData.get(CURRENT_ACTION)];
    }

    /**
     * Used to set the entity's action
     *
     * @param action     The action to be performed
     * @param tickLength -1 to for infinite action or any positive value to set the tick length
     */
    public void setCurrentAction(Actions action, int tickLength) {
        this.entityData.set(CURRENT_ACTION, action.ordinal());
        this.actionResetTicks = tickLength;
    }

    /**
     * Used to set the entity's action - will repeat indefinitely
     *
     * @param action The action to be performed
     */
    public void setCurrentAction(Actions action) {
        setCurrentAction(action, -1);
    }

    public enum Actions {
        NONE,
        ATTACK,
        TALK,
        PANIC;

        public boolean isEqual(int ordinal) {
            return this.ordinal() == ordinal;
        }
    }

    private static class DelayedAttackGoal extends MeleeAttackGoal {
        private final AnimatedCreatureEntity entity;

        public DelayedAttackGoal(AnimatedCreatureEntity entity, float speed, boolean useMemory) {
            super(entity, speed, useMemory);
            this.entity = entity;
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
            double reach = this.getAttackReachSqr(enemy);
            if (distToEnemySqr <= reach && this.ticksUntilNextAttack <= 0) {
                this.resetAttackCooldown();
                if (!this.entity.canMoveWhileAttacking) {
                    this.mob.getNavigation().stop();
                }
                if (this.entity.knockbackResistWhileAttacking >= 0.01) {
                    attributes.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier("attack.knockback", 1.0, AttributeModifier.Operation.ADDITION));
                    this.entity.getAttributes().addTransientAttributeModifiers(attributes);
                }
                entity.startAttack();
            }
        }
    }
}