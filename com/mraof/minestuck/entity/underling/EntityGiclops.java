package com.mraof.minestuck.entity.underling;

import com.mraof.minestuck.entity.ai.EntityAIAttackOnCollideWithRate;
import com.mraof.minestuck.entity.item.EntityGrist;
import com.mraof.minestuck.entity.underling.EntityOgre.Type;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityGiclops extends EntityUnderling 
{

	public static enum Type implements IType
	{
		AMBER("Amber", 0),
		RUST("Rust", 0),
		QUARTZ("Quartz", 0),
		RUBY("Ruby", 1),
		COPPER("Copper", 1),
		;
		final String typeString;
		final int strength;
		Type(String typeString, int strength)
		{
			this.typeString = typeString;
			this.strength = strength;
		}
		@Override
		public IType getTypeFromString(String string) 
		{
			for(Type current : this.values())
				if(current.typeString.equals(string))
					return current;
			return null;
		}

		@Override
		public String getTypeString() 
		{
			return typeString;
		}

		@Override
		public int getStrength() 
		{
			return strength;
		}
	}
	private EntityAIAttackOnCollideWithRate entityAIAttackOnCollideWithRate;

	public EntityGiclops(World world)
	{
		this(world, Type.values()[randStatic.nextInt(Type.values().length)]);
	}
	public EntityGiclops(World par1World, Type type) 
	{
		super(par1World, type, "Giclops");
		setSize(8.0F, 12.0F);
		this.experienceValue = 5 * type.strength + 4;
		this.maxHealth = 28 * (type.strength + 1) + 18;
		this.health = this.maxHealth;
	}

	@Override
	protected void onDeathUpdate() 
	{
		super.onDeathUpdate();
		if(this.deathTime == 20 && !this.worldObj.isRemote)
		{
			for(String gristType : this.getGristSpoils())
				this.worldObj.spawnEntityInWorld(new EntityGrist(worldObj, this.posX + this.rand.nextDouble() * this.width - this.width / 2, this.posY, this.posZ + this.rand.nextDouble() * this.width - this.width / 2, gristType, rand.nextInt(30 + type.getStrength() * 8) + 16));
		}
	}
	
	@Override
	public String[] getGristSpoils() 
	{
		return new String[] {"Build"};
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
		return par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), (int) ((this.type.getStrength() + 1) * 2.5 + 2));
	}

}
