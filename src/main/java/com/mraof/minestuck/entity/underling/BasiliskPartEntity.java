package com.mraof.minestuck.entity.underling;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraftforge.entity.PartEntity;

public class BasiliskPartEntity extends PartEntity<BasiliskEntity>
{
	public final BasiliskEntity parentMob;
	public final String name;
	private final EntityDimensions size;
	
	public BasiliskPartEntity(BasiliskEntity parent, String name, float width, float height)
	{
		super(parent);
		this.size = EntityDimensions.scalable(width, height);
		this.refreshDimensions();
		this.parentMob = parent;
		this.name = name;
	}
	
	@Override
	protected void defineSynchedData()
	{
	}
	
	@Override
	protected void readAdditionalSaveData(CompoundTag pCompound)
	{
	}
	
	@Override
	protected void addAdditionalSaveData(CompoundTag pCompound)
	{
	}
	
	@Override
	public boolean isPickable()
	{
		return true;
	}
	
	public boolean hurt(DamageSource pSource, float pAmount)
	{
		return this.isInvulnerableTo(pSource) ? false : this.parentMob.hurt(pSource, pAmount);
	}
	
	public boolean is(Entity pEntity)
	{
		return this == pEntity || this.parentMob == pEntity;
	}
	
	public Packet<?> getAddEntityPacket()
	{
		throw new UnsupportedOperationException();
	}
	
	public EntityDimensions getDimensions(Pose pPose)
	{
		return this.size;
	}
}
