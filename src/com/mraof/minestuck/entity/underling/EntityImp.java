package com.mraof.minestuck.entity.underling;

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
	public GristSet getGristSpoils()
	{
		return GristHelper.getRandomDrop(type,1);
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
		return 0.6;
	}
	@Override
	protected float getMaximumHealth() 
	{
		return type != null ? 4 * (type.getPower() + 1) + 2 : 0;
	}
	
	@Override
	protected float getKnockbackResistance()
	{
		return 0;
	}
	
	@Override
	protected double getAttackDamage()
	{
		return Math.ceil((double)(this.type.getPower() + 1) / 2);
	}
	
	@Override
	protected void applyGristType(GristType type, boolean fullHeal)
	{
		super.applyGristType(type, fullHeal);
		this.experienceValue = (int) (3 * type.getPower() + 1);
	}
	
}