package com.mraof.minestuck.entity;

import com.mraof.minestuck.entity.ai.attack.AttackState;
import com.mraof.minestuck.entity.animation.MSMobAnimations;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * A base class for animated entities with a potentially delayed attack.
 */
public abstract class AttackingAnimatedEntity extends AnimatedPathfinderMob implements AttackState.Holder
{
	private static final EntityDataAccessor<Integer> CURRENT_ACTION = SynchedEntityData.defineId(AttackingAnimatedEntity.class, EntityDataSerializers.INT);
	
	protected AttackingAnimatedEntity(EntityType<? extends AttackingAnimatedEntity> type, Level level)
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
	public AttackState getAttackState()
	{
		return AttackState.values()[this.entityData.get(CURRENT_ACTION)];
	}
	
	@Override
	public void setAttackState(AttackState state)
	{
		this.entityData.set(CURRENT_ACTION, state.ordinal());
	}
}
