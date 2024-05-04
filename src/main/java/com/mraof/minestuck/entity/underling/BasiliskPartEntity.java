package com.mraof.minestuck.entity.underling;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.neoforged.neoforge.entity.PartEntity;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BasiliskPartEntity extends PartEntity<BasiliskEntity>
{
	private final BasiliskEntity parentMob;
	private final EntityDimensions size;
	
	public BasiliskPartEntity(BasiliskEntity parent, float width, float height)
	{
		super(parent);
		this.size = EntityDimensions.scalable(width, height);
		this.refreshDimensions();
		this.parentMob = parent;
	}
	
	@Override
	protected void defineSynchedData()
	{
	}
	
	@Override
	protected void readAdditionalSaveData(CompoundTag compound)
	{
	}
	
	@Override
	protected void addAdditionalSaveData(CompoundTag compound)
	{
	}
	
	@Override
	public boolean isPickable()
	{
		return true;
	}
	
	@Override
	public boolean canChangeDimensions()
	{
		return false;
	}
	
	@Override
	public boolean hurt(DamageSource source, float amount)
	{
		return !this.isInvulnerableTo(source) && this.parentMob.hurt(source, amount);
	}
	
	@Override
	public boolean is(Entity pEntity)
	{
		return this == pEntity || this.parentMob == pEntity;
	}
	
	@Override
	public EntityDimensions getDimensions(Pose pPose)
	{
		return this.size;
	}
}
