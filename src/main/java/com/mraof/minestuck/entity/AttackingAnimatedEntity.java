package com.mraof.minestuck.entity;

import com.mraof.minestuck.entity.ai.MoveToTargetGoal;
import com.mraof.minestuck.entity.ai.SlowAttackWhenInRangeGoal;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

/**
 * A base class for animated entities with a potentially delayed attack.
 */
public abstract class AttackingAnimatedEntity extends CreatureEntity
{
	private static final DataParameter<Integer> CURRENT_ACTION = EntityDataManager.defineId(AttackingAnimatedEntity.class, DataSerializers.INT);
	
	protected AttackingAnimatedEntity(EntityType<? extends AttackingAnimatedEntity> type, World world)
	{
		super(type, world);
	}
	
	@Override
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		entityData.define(CURRENT_ACTION, AnimatedCreatureEntity.Actions.NONE.ordinal());
	}
	
	/**
	 * Used to start animations
	 *
	 * @return true if the entity performing an attack
	 */
	protected boolean isAttacking()
	{
		SlowAttackWhenInRangeGoal.AttackState state = this.getAttackState();
		return state == SlowAttackWhenInRangeGoal.AttackState.ATTACK || state == SlowAttackWhenInRangeGoal.AttackState.ATTACK_RECOVERY;
	}
	
	/**
	 * @return true during the attack animation right before an attack lands.
	 */
	protected boolean isPreparingToAttack()
	{
		return this.getAttackState() == SlowAttackWhenInRangeGoal.AttackState.ATTACK;
	}
	
	/**
	 * @return the current state of the entity's melee attack
	 */
	protected SlowAttackWhenInRangeGoal.AttackState getAttackState()
	{
		return SlowAttackWhenInRangeGoal.AttackState.values()[this.entityData.get(CURRENT_ACTION)];
	}
	
	/**
	 * Used to set the entity's attack state.
	 * The attack state is meant to be set exclusively by {@link MoveToTargetGoal}.
	 *
	 * @param state The new state of the entity's melee attack
	 */
	public void setAttackState(SlowAttackWhenInRangeGoal.AttackState state)
	{
		this.entityData.set(CURRENT_ACTION, state.ordinal());
	}
}
