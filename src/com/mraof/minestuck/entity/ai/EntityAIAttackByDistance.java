package com.mraof.minestuck.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.MathHelper;

public class EntityAIAttackByDistance extends EntityAIBase
{
	/** The entity the AI instance has been applied to */
	private final EntityLiving entityHost;

	/**
	 * The entity (as a RangedAttackMob) the AI instance has been applied to.
	 */
	private final EntityLiving attacker;
	private EntityLivingBase attackTarget;

	/**
	 * A decrementing tick that spawns a ranged attack once this value reaches 0. It is then set back to the
	 * maxRangedAttackTime.
	 */
	private int rangedAttackTime;
	private float entityMoveSpeed;
	private int ticksSeeingTarget;
	private int field_96561_g;

	/**
	 * The maximum time the AI has to wait before peforming another ranged attack.
	 */
	private int maxRangedAttackTime;
	private float field_96562_i;
	private float field_82642_h;

	public EntityAIAttackByDistance(IRangedAttackMob par1IRangedAttackMob, float par2, int par3, float par4)
	{
		this(par1IRangedAttackMob, par2, par3, par3, par4);
	}

	public EntityAIAttackByDistance(IRangedAttackMob par1IRangedAttackMob, float par2, int par3, int par4, float par5)
	{
		this.rangedAttackTime = -1;
		this.ticksSeeingTarget = 0;

		if (!(par1IRangedAttackMob instanceof EntityLiving))
		{
			throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
		}
		else
		{
			this.attacker = (EntityLiving) par1IRangedAttackMob;
			this.entityHost = (EntityLiving)par1IRangedAttackMob;
			this.entityMoveSpeed = par2;
			this.field_96561_g = par3;
			this.maxRangedAttackTime = par4;
			this.field_96562_i = par5;
			this.field_82642_h = par5 * par5;
			this.setMutexBits(3);
		}
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute()
	{
		EntityLivingBase entityliving = this.entityHost.getAttackTarget();

		if (entityliving == null)
			return false;
		else
		{
			if(entityliving.isEntityAlive())
			{
				this.attackTarget = entityliving;
				return true;
			}
			else
				return false;
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting()
	{
		return this.shouldExecute() || !this.entityHost.getNavigator().noPath();
	}

	/**
	 * Resets the task
	 */
	public void resetTask()
	{
		this.attackTarget = null;
		this.ticksSeeingTarget = 0;
		this.rangedAttackTime = -1;
	}

	/**
	 * Updates the task
	 */
	public void updateTask()
	{
		double d0 = this.entityHost.getDistanceSq(this.attackTarget.posX, this.attackTarget.getEntityBoundingBox().minY, this.attackTarget.posZ);
		boolean flag = this.entityHost.getEntitySenses().canSee(this.attackTarget);

		if (flag)
		{
			++this.ticksSeeingTarget;
		}
		else
		{
			this.ticksSeeingTarget = 0;
		}

		if (d0 <= (double)this.field_82642_h && this.ticksSeeingTarget >= 20)
		{
			this.entityHost.getNavigator().clearPathEntity();
		}
		else
		{
			this.entityHost.getNavigator().tryMoveToEntityLiving(this.attackTarget, this.entityMoveSpeed);
		}

		this.entityHost.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);
		float f;
		double meleeRange = (double)(this.attacker.width * 2.0F * this.attacker.width * 2.0F);
		if(d0 > meleeRange)
		{
			if (--this.rangedAttackTime == 0)
			{
				if (d0 > (double)this.field_82642_h || !flag)
				{
					return;
				}
	
				f = MathHelper.sqrt_double(d0) / this.field_96562_i;
				float f1 = f;
	
				if (f < 0.1F)
				{
					f1 = 0.1F;
				}
	
				if (f1 > 1.0F)
				{
					f1 = 1.0F;
				}
	
				((IRangedAttackMob) this.attacker).attackEntityWithRangedAttack(this.attackTarget, f1);
				this.rangedAttackTime = MathHelper.floor_float(f * (float)(this.maxRangedAttackTime - this.field_96561_g) + (float)this.field_96561_g);
			}
			else if (this.rangedAttackTime < 0)
			{
				f = MathHelper.sqrt_double(d0) / this.field_96562_i;
				this.rangedAttackTime = MathHelper.floor_float(f * (float)(this.maxRangedAttackTime - this.field_96561_g) + (float)this.field_96561_g);
			}
		}
		else
		{
			this.rangedAttackTime = Math.max(this.rangedAttackTime - 1, 0);
			if (this.attacker.getDistanceSq(this.attackTarget.posX, this.attackTarget.getEntityBoundingBox().minY, this.attackTarget.posZ) <= d0)
			{
				if (this.rangedAttackTime <= 0)
				{
					this.rangedAttackTime = 20;
	
					if (this.attacker.getHeldItem() != null)
					{
						this.attacker.swingItem();
					}
	
					this.attacker.attackEntityAsMob(this.attackTarget);
				}
			}
		}
	}
}

