package com.mraof.minestuck.entity.underling;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import com.mraof.minestuck.entity.ai.EntityAIAttackOnCollideWithRate;
import com.mraof.minestuck.entity.item.EntityGrist;
import com.mraof.minestuck.util.GristAmount;
import com.mraof.minestuck.util.GristHelper;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.GristType;

public class EntityImp extends EntityUnderling
{
	private EntityAIAttackOnCollideWithRate entityAIAttackOnCollideWithRate = new EntityAIAttackOnCollideWithRate(this, .4F, 20, false);

	public EntityImp(World world) 
	{
		this(world, GristHelper.getPrimaryGrist());
	}
	public EntityImp(World world, GristType type) 
	{
		super(world, type, "Imp");
		setSize(0.5F, 1.0F);
		this.experienceValue = (int) (3 * type.getPower() + 1);
	}


	@Override
	public void onLivingUpdate() 
	{
		if(GristType.Uranium == this.type && this.rand.nextDouble() < .0001)
		{
			this.motionX += rand.nextInt(33) - 16;
			this.motionZ += rand.nextInt(33) - 16;
		}
		super.onLivingUpdate();
	}
	@Override
	protected void onDeathUpdate() 
	{
		super.onDeathUpdate();
		if(this.deathTime == 20 && !this.worldObj.isRemote)
		{
			//For spawning all grist in debugging
			//			for(String gristType : GristType.values()[GristType.values()[EntityGrist.gristTypes)].getName()].getName()
			//				for(int i = 0; i < rand.nextInt(5) + 1; i++)
			//				this.worldObj.spawnEntityInWorld(new EntityGrist(worldObj, this.posX + this.rand.nextDouble() * 10 - 5, this.posY, this.posZ + this.rand.nextDouble() * 10 - 5, gristType, rand.nextInt(32) * rand.nextInt(32) + 1));
			for(Object gristType : this.getGristSpoils().getArray())
				this.worldObj.spawnEntityInWorld(new EntityGrist(worldObj, this.posX + this.rand.nextDouble() * this.width - this.width / 2, this.posY, this.posZ + this.rand.nextDouble() * this.width - this.width / 2, (GristAmount) gristType));
		}
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
	protected float getWanderSpeed() 
	{
		return .3F;
	}
	@Override
	protected float getMaxHealth() 
	{
		return 4 * (type.getPower() + 1) + 2;
	}

}
