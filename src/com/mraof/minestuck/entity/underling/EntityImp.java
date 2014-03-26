package com.mraof.minestuck.entity.underling;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
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
	protected float getMaximumHealth() 
	{
		return 4 * (type.getPower() + 1) + 2;
	}

}
