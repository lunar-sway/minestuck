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
public abstract class AttackingAnimatedEntity extends CreatureEntity implements SlowAttackWhenInRangeGoal.AttackStateHolder
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
	
	@Override
	public SlowAttackWhenInRangeGoal.AttackState getAttackState()
	{
		return SlowAttackWhenInRangeGoal.AttackState.values()[this.entityData.get(CURRENT_ACTION)];
	}
	
	@Override
	public void setAttackState(SlowAttackWhenInRangeGoal.AttackState state)
	{
		this.entityData.set(CURRENT_ACTION, state.ordinal());
	}
}
