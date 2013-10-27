package com.mraof.minestuck.entity.underling;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.mraof.minestuck.entity.ai.EntityAIAttackOnCollideWithRate;
import com.mraof.minestuck.grist.GristHelper;
import com.mraof.minestuck.grist.GristSet;
import com.mraof.minestuck.grist.GristType;

public class EntityGiclops extends EntityUnderling 
{
	public float sizeMultiplier;
	private EntityAIAttackOnCollideWithRate entityAIAttackOnCollideWithRate;

	public EntityGiclops(World world)
	{
		this(world, GristHelper.getPrimaryGrist());
	}
	public EntityGiclops(World par1World, GristType gristType) 
	{
		super(par1World, gristType, "Giclops");
		this.sizeMultiplier = 1F;
//		this.sizeMultiplier = gristType.getPower() / 10 + 1; 
		//TODO make giclops of variable size
		setSize(8.0F * this.sizeMultiplier, 12.0F * this.sizeMultiplier);
		this.experienceValue = (int) (5 * gristType.getPower() + 4);
//		this.health = this.maxHealth;
		this.stepHeight = 2;
	}

	@Override
	public GristSet getGristSpoils()
	{
		return GristHelper.getRandomDrop(type, 5);
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
	public void writeSpawnData(ByteArrayDataOutput data) 
	{
		super.writeSpawnData(data);
	}
	@Override
	public void readSpawnData(ByteArrayDataInput data) 
	{
		super.readSpawnData(data);
		this.sizeMultiplier = 1F;
//		this.sizeMultiplier = this.type.getPower() / 10 + 1;
//		Debug.print(this.type + " has a size multiplier of " + this.sizeMultiplier);
		setSize(8.0F * this.sizeMultiplier, 12.0F * this.sizeMultiplier);
	}
	@Override
	public boolean attackEntityAsMob(Entity par1Entity) 
	{
		return par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), (int) ((this.type.getPower() + 1) * 2.5 + 2));
	}
	@Override
	protected float getMaximumHealth() 
	{
		return 19 * (type.getPower() + 1) + 48;
	}

}
