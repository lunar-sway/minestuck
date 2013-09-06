package com.mraof.minestuck.entity.underling;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import com.mraof.minestuck.entity.IEntityMultiPart;
import com.mraof.minestuck.entity.ai.EntityAIAttackOnCollideWithRate;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.GristHelper;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.GristType;

public class EntityBasilisk extends EntityUnderling implements IEntityMultiPart 
{
	private EntityAIAttackOnCollideWithRate entityAIAttackOnCollideWithRate;
	EntityUnderlingPart tail;
	public EntityBasilisk(World world) 
	{
		this(world, GristType.Tar);
//		this(world, GristHelper.getPrimaryGrist());
	}
	public EntityBasilisk(World par1World, GristType type) 
	{
		super(par1World, type, "Basilisk");
		this.setSize(3F, 2F);
		tail = new EntityUnderlingPart(this, "tail", 3F, 2F);
		par1World.spawnEntityInWorld(tail);
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
	protected float getMaxHealth() 
	{
		return 13 * (type.getPower() + 1) + 37;
	}

	@Override
	protected float getWanderSpeed() 
	{
		return .3F;
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
		Debug.printf("Damage from %s, source is %s, amount of damage is %f, success is %b, isRemote is %b", entityPart, source, damage, flag, this.worldObj.isRemote);
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
	protected void updateFallState(double par1, boolean par3) 
	{
		if((tail.fallDistance > 0))
			super.updateFallState(par1, par3 || tail.onGround);
	}
	@Override
	public void updatePartPositions() 
	{
		float f1 = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw);
		double tailPosX = (this.posX +  Math.sin(f1 / 180.0 * Math.PI) * tail.width);
		double tailPosZ = (this.posZ + -Math.cos(f1 / 180.0 * Math.PI) * tail.width);
//		double tailPosY = (tail.posY - this.posY) < -2  ? this.posY : tail.posY;
//		if((tailPosY - this.posY) > 2)
//		{
//			this.posY += tailPosY;
//			this.motionY = 0;
//		}
		if(tail.entityUnderlingObj == null)
			tail.entityUnderlingObj = this;
		tail.setPositionAndRotation(tailPosX, this.posY, tailPosZ, this.rotationYaw, this.rotationPitch);
	}

}
