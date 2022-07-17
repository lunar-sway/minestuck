package com.mraof.minestuck.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;

import java.util.ArrayList;

/**
 * Created by mraof on 2017 January 26 at 9:31 PM.
 */
public class EntityBigPart extends Entity implements IEntityAdditionalSpawnData
{
	PartGroup group;
	private int partId;
	EntityDimensions size;
	
	EntityBigPart(EntityType<?> type, Level level, PartGroup group, float width, float height)
	{
		super(type, level);
		this.group = group;
		this.size = EntityDimensions.scalable(width, height);
	}

	void setPartId(int id)
	{
		this.partId = id;
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
	public void baseTick()
	{
		if(this.group == null || this.group.parent == null || this.group.parent.isRemoved())
		{
			this.discard();
		}
		super.baseTick();
		//world.getHeight(this.getPosition());
	}

	@Override
	public boolean hurt(DamageSource damageSource, float amount)
	{
		return this.group != null && this.group.attackFrom(damageSource, amount);
	}
	
	@Override
	public EntityDimensions getDimensions(Pose poseIn)
	{
		return super.getDimensions(poseIn);
	}
	
	@Override
	public void writeSpawnData(FriendlyByteBuf buffer)
	{
		buffer.writeInt(this.group.parent.getId());
		buffer.writeFloat(this.getBbWidth());
		buffer.writeFloat(this.getBbHeight());
	}

	@Override
	public void readSpawnData(FriendlyByteBuf additionalData)
	{
		Entity entity = level.getEntity(additionalData.readInt());
		if(entity instanceof IBigEntity)
		{
			ArrayList<EntityBigPart> parts = ((IBigEntity) entity).getGroup().parts;
			int index = parts.size() - 1;
			while(index > 0 && parts.get(index).partId > this.partId)
			{
				index--;
			}
			parts.add(index, this);
		}
	}

	@Override
	public boolean is(Entity entityIn)
	{
		return entityIn == this || this.group != null && (entityIn == this.group.parent || entityIn instanceof EntityBigPart && ((EntityBigPart) entityIn).group == this.group);
	}

	@Override
	public boolean isPickable()
	{
		return true;
	}

	@Override
	public boolean isInWall()
	{
		return false;
	}
	
	@Override
	public void move(MoverType typeIn, Vec3 pos)
	{
		this.setPos(this.getX() + pos.x, this.getY() + pos.y, this.getZ() + pos.z);
	}
	
	@Override
	public Packet<?> getAddEntityPacket()
	{
		throw new UnsupportedOperationException();
	}
}