package com.mraof.minestuck.entity.underling;

import com.mraof.minestuck.entity.IEntityMultiPart;
import com.mraof.minestuck.entity.ModEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityUnderlingPart extends EntityLiving implements IEntityAdditionalSpawnData
{
	public IEntityMultiPart baseEntity;

	public int id = -1;
	private int headId = -1;
	
	public EntityUnderlingPart(IEntityMultiPart entityMultiPart, int id, float par3, float par4)
	{
		super(ModEntityTypes.UNDERLING_PART, entityMultiPart.getWorld());
		this.setSize(par3, par4);
		this.baseEntity = entityMultiPart;
		this.id = id;
	}
	
	@Override
	public ITextComponent getName()
	{
		return ((Entity) baseEntity).getName();
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
		if(this.baseEntity == null || par1DamageSource == DamageSource.IN_WALL || par1DamageSource == DamageSource.DROWN || par1DamageSource == DamageSource.FALL)
			return false;
		return this.baseEntity.attackEntityFromPart(this, par1DamageSource, par2);
	}
	
	@Override
	public void tick()
	{
		super.tick();
		if(this.baseEntity == null || ((Entity)this.baseEntity).removed)
		{
			this.remove();
		}
		else
			this.setBaseById(headId);
	}

	@Override
	public void remove()
	{
		super.remove();
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
	public boolean canDespawn()
	{
		return false;
	}
	
	@Override
	public void writeSpawnData(PacketBuffer buffer)
	{
		buffer.writeInt(this.id);
		if(this.baseEntity != null)
			buffer.writeInt(((Entity)this.baseEntity).getEntityId());
		else
			buffer.writeInt(-1);
	}
	
	@Override
	public void readSpawnData(PacketBuffer additionalData)
	{
		setBaseById(additionalData.readInt());
	}
	
	public void setBaseById(int baseId)
	{
		Entity base = this.world.getEntityByID(baseId);
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