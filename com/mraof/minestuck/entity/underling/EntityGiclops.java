package com.mraof.minestuck.entity.underling;

import com.mraof.minestuck.entity.ai.EntityAIAttackOnCollideWithRate;
import com.mraof.minestuck.entity.item.EntityGrist;
import com.mraof.minestuck.util.GristAmount;
import com.mraof.minestuck.util.GristHelper;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.GristType;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityGiclops extends EntityUnderling 
{


	private EntityAIAttackOnCollideWithRate entityAIAttackOnCollideWithRate;

	public EntityGiclops(World world)
	{
		this(world, GristHelper.getPrimaryGrist());
	}
	public EntityGiclops(World par1World, GristType gristType) 
	{
		super(par1World, gristType, "Giclops");
		setSize(8.0F, 12.0F);
		this.experienceValue = (int) (5 * gristType.getPower() + 4);
//		this.health = this.maxHealth;
		this.stepHeight = 2;
	}

	@Override
	protected void onDeathUpdate() 
	{
		super.onDeathUpdate();
		if(this.deathTime == 20 && !this.worldObj.isRemote)
		{
			for(Object gristType : this.getGristSpoils().getArray())
				this.worldObj.spawnEntityInWorld(new EntityGrist(worldObj, this.posX + this.rand.nextDouble() * this.width - this.width / 2, this.posY, this.posZ + this.rand.nextDouble() * this.width - this.width / 2, (GristAmount) gristType));		}
	}
	
	@Override
	public GristSet getGristSpoils()
	{
		return GristHelper.getRandomDrop(type,5);
	}

	@Override
	protected void setCombatTask() 
	{
		if(entityAIAttackOnCollideWithRate == null)
			entityAIAttackOnCollideWithRate = new EntityAIAttackOnCollideWithRate(this, .3F, 50, false);
		entityAIAttackOnCollideWithRate.setDistanceMultiplier(1.1F);
		this.tasks.removeTask(this.entityAIAttackOnCollideWithRate);
		this.tasks.addTask(4, entityAIAttackOnCollideWithRate);
	}

	@Override
	protected float getWanderSpeed() 
	{
		return 0.1F;
	}
	@Override
	public boolean attackEntityAsMob(Entity par1Entity) 
	{
		return par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), (int) ((this.type.getPower() + 1) * 2.5 + 2));
	}
	@Override
	protected float getMaxHealth() 
	{
		return 28 * (type.getPower() + 1) + 18;
	}

}
