package com.mraof.minestuck.entity.underling;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import com.mraof.minestuck.entity.ai.EntityAIAttackOnCollideWithRate;
import com.mraof.minestuck.util.GristHelper;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.GristType;

public class EntityImp extends EntityUnderling
{
	private EntityAIAttackOnCollideWithRate entityAIAttackOnCollideWithRate = new EntityAIAttackOnCollideWithRate(this, .4F, 20, false);

	public EntityImp(World world) 
	{
		super(world, "Imp");
		setSize(0.5F, 1.0F);
	}


	@Override
	public void onLivingUpdate() 
	{
//		if(GristType.Uranium == this.type && this.rand.nextDouble() < .0001)	I think someone misunderstood where the uranium imp's teleporting powers came from
//		{
//			this.motionX += rand.nextInt(33) - 16;
//			this.motionZ += rand.nextInt(33) - 16;
//		}
		super.onLivingUpdate();
	}

	@Override
	public GristSet getGristSpoils()
	{
		return GristHelper.getRandomDrop(type,1);
	}

	@Override
	public boolean attackEntityAsMob(Entity par1Entity) 
	{
		boolean flag = par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), (int) Math.ceil((double)(this.type.getPower() + 1) / 2));
		return flag;
	}
	@Override
	protected void setCombatTask() 
	{
		if(entityAIAttackOnCollideWithRate == null)
			entityAIAttackOnCollideWithRate = new EntityAIAttackOnCollideWithRate(this, .4F, 20, false);
		this.tasks.removeTask(this.entityAIAttackOnCollideWithRate);
		this.tasks.addTask(4, entityAIAttackOnCollideWithRate);
	}
	@Override
	protected double getWanderSpeed() 
	{
		return 0.3;
	}
	@Override
	protected float getMaximumHealth() 
	{
		return type != null ? 4 * (type.getPower() + 1) + 2 : 0;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompund)
	{
		super.readFromNBT(tagCompund);
		this.experienceValue = (int) (3 * type.getPower() + 1);
	}
	
	@Override
	public void readSpawnData(ByteBuf additionalData)
	{
		super.readSpawnData(additionalData);
		this.experienceValue = (int) (3 * type.getPower() + 1);
	}
	
	@Override
	public IEntityLivingData func_180482_a(DifficultyInstance difficulty, IEntityLivingData livingData)
	{
		livingData = super.func_180482_a(difficulty, livingData);
		this.experienceValue = (int) (3 * type.getPower() + 1);
		return livingData;
	}
}