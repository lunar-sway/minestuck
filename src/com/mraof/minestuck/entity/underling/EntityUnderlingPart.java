package com.mraof.minestuck.entity.underling;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import com.mraof.minestuck.entity.IEntityMultiPart;

public class EntityUnderlingPart extends EntityLiving implements IEntityAdditionalSpawnData
{
	public IEntityMultiPart baseEntity;

	public int id = -1;
	private int headId = -1;

	public EntityUnderlingPart(World world)
	{
		super(world);
	}
	public EntityUnderlingPart(IEntityMultiPart par1IEntityMultiPart, int id, float par3, float par4)
	{
		super(par1IEntityMultiPart.getWorld());
		this.setSize(par3, par4);
		this.baseEntity = par1IEntityMultiPart;
		this.id = id;
	}
	
	@Override
	public String getName()
	{
		return ((Entity) baseEntity).getName();
	}
	
	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) 
	{
		if(this.baseEntity != null)
			super.writeEntityToNBT(par1NBTTagCompound);
		else
			this.setDead();
	}

	/**
	 * Returns true if other Entities should be prevented from moving through this Entity.
	 */
	@Override
	public boolean canBeCollidedWith()
	{
		return true;
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
	{
		if(this.baseEntity == null || par1DamageSource == DamageSource.inWall || par1DamageSource == DamageSource.drown || par1DamageSource == DamageSource.fall)
			return false;
		return ((IEntityMultiPart) this.baseEntity).attackEntityFromPart(this, par1DamageSource, par2);
	}
	@Override
	public void onUpdate() 
	{
		super.onUpdate();
		if(this.baseEntity == null || ((Entity)this.baseEntity).isDead)
		{
			this.setDead();
		}
		else
			this.setBaseById(headId);
	}

	@Override
	public void setDead() 
	{
		super.setDead();
		if(this.baseEntity != null)
			baseEntity.onPartDeath(this, this.id);
	}
	/**
	 * Returns true if Entity argument is equal to this Entity
	 */
	@Override
	public boolean isEntityEqual(Entity par1Entity)
	{
		return this == par1Entity || this.baseEntity == par1Entity;
	}
	@Override
	public boolean handleWaterMovement() 
	{
		return false;
	}
	
	@Override
	 protected boolean canDespawn()
	 {
	     return false;
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) 
	{
		buffer.writeInt(this.id);
		if(this.baseEntity != null)
			buffer.writeInt(((Entity)this.baseEntity).getEntityId());
		else
			buffer.writeInt(-1);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) 
	{
		setBaseById(additionalData.readInt());
	}
	
	public void setBaseById(int baseId)
	{
		Entity base = this.worldObj.getEntityByID(baseId);
		if(base != null)
		{
			this.baseEntity = (IEntityMultiPart) base;
			this.baseEntity.addPart(this, this.id);
		}
	}

	@Override
	public void setSize(float width, float height)
	{
		super.setSize(width, height);
	}

	@Override
	public void collideWithEntity(Entity par1Entity)
	{
	}
}
