package com.mraof.minestuck.entity.underling;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import com.mraof.minestuck.entity.IEntityMultiPart;
import com.mraof.minestuck.entity.ai.EntityAIAttackOnCollideWithRate;
import com.mraof.minestuck.util.GristHelper;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.GristType;

public class EntityBasilisk extends EntityUnderling implements IEntityMultiPart 
{
	private EntityAIAttackOnCollideWithRate entityAIAttackOnCollideWithRate;
	EntityUnderlingPart tail;
	
	public EntityBasilisk(World world) 
	{
		super(world, "Basilisk");
		this.setSize(3F, 2F);
		tail = new EntityUnderlingPart(this, 0, 3F, 2F);
		world.spawnEntityInWorld(tail);
	}

	@Override
	public GristSet getGristSpoils() 
	{
		return GristHelper.getRandomDrop(type, 4);
	}

	@Override
	protected void setCombatTask() 
	{
		if(entityAIAttackOnCollideWithRate == null)
			entityAIAttackOnCollideWithRate = new EntityAIAttackOnCollideWithRate(this, .3F, 40, false);
		entityAIAttackOnCollideWithRate.setDistanceMultiplier(1.2F);
		this.tasks.removeTask(this.entityAIAttackOnCollideWithRate);
		this.tasks.addTask(4, entityAIAttackOnCollideWithRate);
	}
	@Override
	public boolean attackEntityAsMob(Entity par1Entity) 
	{
		return par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), (this.type.getPower() + 1) * 2);
	}
	
	@Override
	protected float getMaximumHealth() 
	{
		return type != null ? 13 * (type.getPower() + 1) + 37 : 0;
	}

	@Override
	protected double getWanderSpeed()
	{
		return 0.7;
	}

	@Override
	public World getWorld() 
	{
		return this.worldObj;
	}
	
	@Override
	public void onEntityUpdate() 
	{
		super.onEntityUpdate();
		this.updatePartPositions();
	}

	@Override
	public boolean attackEntityFromPart(Entity entityPart, DamageSource source, float damage) 
	{
		boolean flag = this.attackEntityFrom(source, damage);
		
		return flag;
	}
	@Override
	public Entity[] getParts() 
	{
		return new Entity[] {tail};
	}
	@Override
	protected void collideWithEntity(Entity par1Entity) 
	{
		if(par1Entity != this.tail)
			super.collideWithEntity(par1Entity);
	}
	@Override
	public void setPositionAndRotation(double par1, double par3, double par5, float par7, float par8) {
		super.setPositionAndRotation(par1, par3, par5, par7, par8);
		this.updatePartPositions();
	}
	
	@Override
	public void updatePartPositions() 
	{
		if(tail == null)
			return;
		float f1 = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw);
		double tailPosX = (this.posX +  Math.sin(f1 / 180.0 * Math.PI) * tail.width);
		double tailPosZ = (this.posZ + -Math.cos(f1 / 180.0 * Math.PI) * tail.width);

		tail.setPositionAndRotation(tailPosX, this.posY, tailPosZ, this.rotationYaw, this.rotationPitch);
	}

	@Override
	public void addPart(Entity entityPart, int id) 
	{
		this.tail = (EntityUnderlingPart) entityPart;
		this.tail.setSize(3F, 2F);
	}

	@Override
	public void onPartDeath(Entity entityPart, int id) 
	{

	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompund)
	{
		super.readFromNBT(tagCompund);
		this.experienceValue = (int) (6 * type.getPower() + 4);
	}
	
	@Override
	public void readSpawnData(ByteBuf additionalData)
	{
		super.readSpawnData(additionalData);
		this.experienceValue = (int) (6 * type.getPower() + 4);
	}
	
	@Override
	public IEntityLivingData func_180482_a(DifficultyInstance difficulty, IEntityLivingData livingData)
	{
		livingData = super.func_180482_a(difficulty, livingData);
		this.experienceValue = (int) (6 * type.getPower() + 4);
		return livingData;
	}
	
}