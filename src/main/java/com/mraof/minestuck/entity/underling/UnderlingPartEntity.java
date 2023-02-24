package com.mraof.minestuck.entity.underling;

import com.mraof.minestuck.entity.IEntityMultiPart;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;

public class UnderlingPartEntity extends Entity implements IEntityAdditionalSpawnData
{
	public IEntityMultiPart baseEntity;
	
	public int id = -1;
	private int headId = -1;
	private EntityDimensions size;
	
	public UnderlingPartEntity(IEntityMultiPart entityMultiPart, int id, float width, float height)
	{
		super(entityMultiPart.asEntity().getType(), entityMultiPart.asEntity().getLevel());
		size = EntityDimensions.scalable(width, height);
		this.baseEntity = entityMultiPart;
		this.id = id;
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
	public Component getName()
	{
		return baseEntity.asEntity().getName();
	}
	
	/**
	 * Returns true if other Entities should be prevented from moving through this Entity.
	 */
	@Override
	public boolean isPickable()
	{
		return true;
	}
	
	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean hurt(DamageSource par1DamageSource, float par2)
	{
		if(this.baseEntity == null || par1DamageSource == DamageSource.IN_WALL || par1DamageSource == DamageSource.DROWN || par1DamageSource == DamageSource.FALL)
			return false;
		return this.baseEntity.attackEntityFromPart(this, par1DamageSource, par2);
	}
	
	@Override
	public void tick()
	{
		super.tick();
		if(this.baseEntity == null || this.baseEntity.asEntity().isRemoved())
		{
			this.discard();
		} else
			this.setBaseById(headId);
	}
	
	@Override
	public void remove(Entity.RemovalReason reason)
	{
		super.remove(reason);
		if(this.baseEntity != null)
			baseEntity.onPartDeath(this, this.id);
	}
	
	/**
	 * Returns true if Entity argument is equal to this Entity
	 */
	@Override
	public boolean is(Entity par1Entity)
	{
		return this == par1Entity || this.baseEntity == par1Entity;
	}
	
	@Override
	public boolean updateFluidHeightAndDoFluidPushing(TagKey<Fluid> fluidTag, double fluidFactor)
	{
		return false;
	}
	
	@Override
	public void writeSpawnData(FriendlyByteBuf buffer)
	{
		buffer.writeInt(this.id);
		if(this.baseEntity != null)
			buffer.writeInt(((Entity) this.baseEntity).getId());
		else
			buffer.writeInt(-1);
	}
	
	@Override
	public void readSpawnData(FriendlyByteBuf additionalData)
	{
		setBaseById(additionalData.readInt());
	}
	
	public void setBaseById(int baseId)
	{
		Entity base = this.level.getEntity(baseId);
		if(base != null)
		{
			this.baseEntity = (IEntityMultiPart) base;
			this.baseEntity.addPart(this, this.id);
		}
	}
	
	@Override
	public EntityDimensions getDimensions(Pose poseIn)
	{
		return this.size;
	}
	
	@Override
	public Packet<?> getAddEntityPacket()
	{
		throw new UnsupportedOperationException();
	}
}