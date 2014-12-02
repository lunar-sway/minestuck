package com.mraof.minestuck.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityAIAttackOnCollideWithRate extends EntityAIBase
{
	World worldObj;
	EntityCreature attacker;
	EntityLivingBase entityTarget;

	/**
	 * An amount of decrementing ticks that allows the entity to attack once the tick reaches 0.
	 */
	int attackTick;
	float movementSpeed;
	boolean willSearch;

	/** The PathEntity of our entity. */
	PathEntity entityPathEntity;
	Class<? extends Entity> classTarget;
	private int movementTime;
	private int attackRate;
	private float distanceMultiplier = 2.0F;

	public EntityAIAttackOnCollideWithRate(EntityCreature par1EntityLiving, Class<? extends Entity> par2Class, float par3, int attackRate, boolean par4)
	{
		this(par1EntityLiving, par3, attackRate, par4);
		this.classTarget = par2Class;
	}

	public EntityAIAttackOnCollideWithRate(EntityCreature par1EntityLiving, float par2, int attackRate, boolean par3)
	{
		this.attackTick = 0;
		this.attacker = par1EntityLiving;
		this.worldObj = par1EntityLiving.worldObj;
		this.movementSpeed = par2;
		this.attackRate = attackRate;
		this.willSearch = par3;
		this.setMutexBits(3);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute()
	{
		EntityLivingBase entityliving = this.attacker.getAttackTarget();

		if (entityliving == null)
		{
			return false;
		}
		else if (this.classTarget != null && !this.classTarget.isAssignableFrom(entityliving.getClass()))
		{
			return false;
		}
		else
		{
			this.entityTarget = entityliving;
			this.entityPathEntity = this.attacker.getNavigator().getPathToEntityLiving(this.entityTarget);
			return this.entityPathEntity != null;
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting()
	{
		EntityLivingBase entityliving = this.attacker.getAttackTarget();
		return entityliving == null ? false : (!this.entityTarget.isEntityAlive() ? false : (!this.willSearch ? !this.attacker.getNavigator().noPath() : this.attacker.func_180485_d(new BlockPos(MathHelper.floor_double(this.entityTarget.posX), MathHelper.floor_double(this.entityTarget.posY), MathHelper.floor_double(this.entityTarget.posZ)))));
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting()
	{
		this.attacker.getNavigator().setPath(this.entityPathEntity, this.movementSpeed);
		this.movementTime = 0;
	}

	/**
	 * Resets the task
	 */
	public void resetTask()
	{
		this.entityTarget = null;
		this.attacker.getNavigator().clearPathEntity();
	}

	/**
	 * Updates the task
	 */
	public void updateTask()
	{
		this.attacker.getLookHelper().setLookPositionWithEntity(this.entityTarget, 30.0F, 30.0F);

		if ((this.willSearch || this.attacker.getEntitySenses().canSee(this.entityTarget)) && --this.movementTime <= 0)
		{
			this.movementTime = 4 + this.attacker.getRNG().nextInt(7);
			this.attacker.getNavigator().tryMoveToEntityLiving(this.entityTarget, this.movementSpeed);
		}

		this.attackTick = Math.max(this.attackTick - 1, 0);
		double d0 = (double)(this.attacker.width * distanceMultiplier * this.attacker.width * distanceMultiplier);

		if (this.attacker.getDistanceSq(this.entityTarget.posX, this.entityTarget.getBoundingBox().minY, this.entityTarget.posZ) - (entityTarget.width / 2 ) <= d0)
		{
			if (this.attackTick <= 0)
			{
				this.attackTick = this.attackRate;

				if (this.attacker.getHeldItem() != null)
				{
					this.attacker.swingItem();
				}

				this.attacker.attackEntityAsMob(this.entityTarget);
			}
		}
	}
	public void setDistanceMultiplier(float distanceMultiplier) 
	{
		this.distanceMultiplier = distanceMultiplier;
	}
}
