package com.mraof.minestuck.entity.underling;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import com.mraof.minestuck.entity.ai.EntityAIAttackOnCollideWithRate;
import com.mraof.minestuck.grist.GristHelper;
import com.mraof.minestuck.grist.GristSet;
import com.mraof.minestuck.grist.GristType;

//Makes non-stop ogre puns
public class EntityOgre extends EntityUnderling 
{
	private EntityAIAttackOnCollideWithRate entityAIAttackOnCollideWithRate;
	public EntityOgre(World world)
	{
		this(world, GristHelper.getPrimaryGrist());
	}
	public EntityOgre(World par1World, GristType gristType) 
	{
		super(par1World, gristType, "Ogre");
		setSize(3.0F, 4.5F);
		this.experienceValue = (int) (5 * gristType.getPower() + 4);
		this.stepHeight = 1.0F;
	}

	@Override
	public GristSet getGristSpoils()
	{
		return GristHelper.getRandomDrop(type,3);
	}
	@Override
	public boolean attackEntityAsMob(Entity par1Entity) 
	{
		return par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), (this.type.getPower() + 1) * 2);
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
	protected float getWanderSpeed() 
	{
		return .1F;
	}
	@Override
	protected float getMaximumHealth() 
	{
		return 16 * (type.getPower() + 1) + 8;
	}
	@Override
	protected boolean useAltName() 
	{
		if(type.equals(GristType.Tar))
			return true;
		return super.useAltName();
	}

}
