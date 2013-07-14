package com.mraof.minestuck.entity.underling;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import com.mraof.minestuck.entity.ai.EntityAIAttackOnCollideWithRate;
import com.mraof.minestuck.entity.item.EntityGrist;

//Makes non-stop ogre puns
public class EntityOgre extends EntityUnderling 
{

	public static enum Type implements IType
	{
		CRUDE("Crude", 0),
		LIME("Lime", 0),
		SULFUR("Sulfur", 1), 
		RUST("Rust", 1),
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
	public EntityOgre(World world)
	{
		this(world, Type.values()[randStatic.nextInt(Type.values().length)]);
	}
	public EntityOgre(World par1World, Type type) 
	{
		super(par1World, type, "Ogre");
		setSize(3.0F, 4.5F);
		this.experienceValue = 5 * type.strength + 4;
		this.stepHeight = 1.0F;
	}
	@Override
	protected void onDeathUpdate() 
	{
		super.onDeathUpdate();
		if(this.deathTime == 20 && !this.worldObj.isRemote)
		{
			for(String gristType : this.getGristSpoils())
				this.worldObj.spawnEntityInWorld(new EntityGrist(worldObj, this.posX + this.rand.nextDouble() * this.width - this.width / 2, this.posY, this.posZ + this.rand.nextDouble() * this.width - this.width / 2, gristType, rand.nextInt(10 + type.getStrength() * 4) + 8));
		}
	}
	@Override
	public String[] getGristSpoils() 
	{
		switch((Type) this.type)
		{
		case CRUDE:
			return new String[] {"Build", "Tar", "Mercury", "Shale"};
		case LIME:
			return new String[] {"Build", "Quartz", "Marble"};
		case SULFUR:
			return new String[] {"Build", "Sulfur"};
		case RUST:
			return new String[] {"Build", "Sulfur"};
		default:
			return new String[] {"Build"};
		}
	}
	@Override
	public boolean attackEntityAsMob(Entity par1Entity) 
	{
		return par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), (this.type.getStrength() + 1) * 2);
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
	protected float getMaxHealth() 
	{
		return 16 * (type.getStrength() + 1) + 8;
	}

}
