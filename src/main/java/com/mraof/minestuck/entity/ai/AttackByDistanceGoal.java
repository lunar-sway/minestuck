package com.mraof.minestuck.entity.ai;

import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;

import java.util.EnumSet;

public class AttackByDistanceGoal extends Goal
{
	/** The entity the AI instance has been applied to */
	private final MobEntity entityHost;

	/**
	 * The entity (as a RangedAttackMob) the AI instance has been applied to.
	 */
	private final MobEntity attacker;
	private LivingEntity attackTarget;

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

	public AttackByDistanceGoal(IRangedAttackMob par1IRangedAttackMob, float par2, int par3, float par4)
	{
		this(par1IRangedAttackMob, par2, par3, par3, par4);
	}

	public AttackByDistanceGoal(IRangedAttackMob par1IRangedAttackMob, float par2, int par3, int par4, float par5)
	{
		this.rangedAttackTime = -1;
		this.ticksSeeingTarget = 0;

		if(!(par1IRangedAttackMob instanceof MobEntity))
		{
			throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
		}
		else
		{
			this.attacker = (MobEntity) par1IRangedAttackMob;
			this.entityHost = (MobEntity)par1IRangedAttackMob;
			this.entityMoveSpeed = par2;
			this.field_96561_g = par3;
			this.maxRangedAttackTime = par4;
			this.field_96562_i = par5;
			this.field_82642_h = par5 * par5;
			this.setMutexFlags(EnumSet.of(Flag.TARGET, Flag.MOVE));
		}
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute()
	{
		LivingEntity entityliving = this.entityHost.getAttackTarget();

		if (entityliving == null)
			return false;
		else
		{
			if(entityliving.isAlive())
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
	@Override
	public boolean shouldContinueExecuting()
	{
		return this.shouldExecute() || !this.entityHost.getNavigator().noPath();
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask()
	{
		this.attackTarget = null;
		this.ticksSeeingTarget = 0;
		this.rangedAttackTime = -1;
	}
	
	
	@Override
	public void tick()
	{
		double d0 = this.entityHost.getDistanceSq(this.attackTarget.posX, this.attackTarget.getBoundingBox().minY, this.attackTarget.posZ);
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
			this.entityHost.getNavigator().clearPath();
		}
		else
		{
			this.entityHost.getNavigator().tryMoveToEntityLiving(this.attackTarget, this.entityMoveSpeed);
		}

		this.entityHost.getLookController().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);
		float f;
		double meleeRange = (double)(this.attacker.getWidth() * 2.0F * this.attacker.getWidth() * 2.0F);
		if(d0 > meleeRange)
		{
			if (--this.rangedAttackTime == 0)
			{
				if (d0 > (double)this.field_82642_h || !flag)
				{
					return;
				}
	
				f = MathHelper.sqrt(d0) / this.field_96562_i;
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
				this.rangedAttackTime = MathHelper.floor(f * (float)(this.maxRangedAttackTime - this.field_96561_g) + (float)this.field_96561_g);
			}
			else if (this.rangedAttackTime < 0)
			{
				f = MathHelper.sqrt(d0) / this.field_96562_i;
				this.rangedAttackTime = MathHelper.floor(f * (float)(this.maxRangedAttackTime - this.field_96561_g) + (float)this.field_96561_g);
			}
		}
		else
		{
			this.rangedAttackTime = Math.max(this.rangedAttackTime - 1, 0);
			if(this.attacker.getDistanceSq(this.attackTarget.posX, this.attackTarget.getBoundingBox().minY, this.attackTarget.posZ) <= d0)
			{
				if(this.rangedAttackTime <= 0)
				{
					this.rangedAttackTime = 20;
	
					if(!this.attacker.getItemStackFromSlot(EquipmentSlotType.MAINHAND).isEmpty())
					{
						this.attacker.swingArm(Hand.MAIN_HAND);
					}
	
					this.attacker.attackEntityAsMob(this.attackTarget);
				}
			}
		}
	}
}

